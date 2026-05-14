package br.com.cinesmart.servico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Recomendacao;
import br.com.cinesmart.modelo.Usuario;
import br.com.cinesmart.utilitario.GeradorAleatorio;

public class RecomendadorServico {
    private static final Logger logger = LoggerFactory.getLogger(RecomendadorServico.class);

    private final CatalogoFilmesAPI catalogo;
    private final HistoricoUsuarioRepositorio historico;
    private final NotificadorPush notificador;
    private final GeradorAleatorio gerador;
    private final CalculadoraScore calculadora;
    private final FiltroFilmes filtro;

    public RecomendadorServico(CatalogoFilmesAPI catalogo,
                               HistoricoUsuarioRepositorio historico,
                               NotificadorPush notificador,
                               GeradorAleatorio gerador,
                               CalculadoraScore calculadora,
                               FiltroFilmes filtro) {
        this.catalogo = catalogo;
        this.historico = historico;
        this.notificador = notificador;
        this.gerador = gerador;
        this.calculadora = calculadora;
        this.filtro = filtro;
    }

    public List<Recomendacao> recomendar(Usuario usuario, int topN) {
        try {
            List<Filme> catalogoCompleto = catalogo.buscarTodos();
            logger.debug("Catálogo obtido com {} filmes", catalogoCompleto.size());

            List<Filme> filmesValidos = filtro.filtrar(catalogoCompleto, usuario.getPerfil());
            logger.debug("Após filtro: {} filmes válidos", filmesValidos.size());

            if (filmesValidos.isEmpty()) {
                logger.info("Nenhum filme válido encontrado para o usuário {}", usuario.getId());
                return Collections.emptyList();
            }

            List<Recomendacao> recomendacoes = new ArrayList<>();
            for (Filme filme : filmesValidos) {
                double score = calculadora.calcular(filme, usuario.getPerfil());
                String justificativa = gerarJustificativa(filme, usuario, score);
                recomendacoes.add(new Recomendacao(filme, score, justificativa));
            }

            recomendacoes.sort(Comparator.comparingDouble(Recomendacao::getScore).reversed()
                    .thenComparing(Comparator.comparingInt((Recomendacao r) -> r.getFilme().getPopularidade()).reversed()));

            recomendacoes = aplicarDesempateAleatorio(recomendacoes);

            List<Recomendacao> resultado = new ArrayList<>(recomendacoes.subList(0, Math.min(topN, recomendacoes.size())));

            historico.registrarRecomendacao(usuario, new ArrayList<>(resultado));

            if (usuario.isNotificacaoHabilitada()) {
                notificador.enviar(usuario, new ArrayList<>(resultado));
            }

            logger.info("Recomendações geradas para {}: {} resultados", usuario.getId(), resultado.size());
            return resultado;

        } catch (Exception e) {
            logger.error("Erro ao gerar recomendações para usuário {}: {}", usuario.getId(), e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Recomendacao> recomendarAleatorio(Usuario usuario) {
        try {
            List<Filme> catalogoCompleto = catalogo.buscarTodos();
            List<Filme> filmesValidos = filtro.filtrar(catalogoCompleto, usuario.getPerfil());

            if (filmesValidos.isEmpty()) {
                return Collections.emptyList();
            }

            int indice = gerador.sortearInteiro(0, filmesValidos.size());
            Filme filmeAleatorio = filmesValidos.get(indice);

            double score = calculadora.calcular(filmeAleatorio, usuario.getPerfil());
            String justificativa = "Surpreenda-me! Este filme pode ser uma ótima descoberta para você.";
            Recomendacao recomendacao = new Recomendacao(filmeAleatorio, score, justificativa);

            historico.registrarRecomendacao(usuario, List.of(recomendacao));
            if (usuario.isNotificacaoHabilitada()) {
                notificador.enviar(usuario, List.of(recomendacao));
            }

            return List.of(recomendacao);

        } catch (Exception e) {
            logger.error("Erro ao gerar recomendação aleatória para {}: {}", usuario.getId(), e.getMessage());
            return Collections.emptyList();
        }
    }

    private String gerarJustificativa(Filme filme, Usuario usuario, double score) {
        StringBuilder justificativa = new StringBuilder();
        justificativa.append("Recomendamos '").append(filme.getTitulo()).append("' (")
                .append(filme.getAno()).append(") ");

        List<String> generosAmados = new ArrayList<>();
        filme.getGeneros().forEach(g -> {
            if (usuario.getPerfil().getPeso(g) > 0.5) {
                generosAmados.add(g.getDescricao());
            }
        });

        if (!generosAmados.isEmpty()) {
            justificativa.append("porque você gosta de ").append(String.join(" e ", generosAmados)).append(". ");
        }

        if (filme.getPopularidade() > 80) {
            justificativa.append("É um filme muito popular! ");
        }

        justificativa.append("Score de compatibilidade: ").append(String.format("%.1f", score)).append("%");

        return justificativa.toString();
    }

    private List<Recomendacao> aplicarDesempateAleatorio(List<Recomendacao> recomendacoes) {
        if (recomendacoes.size() < 2) {
            return new ArrayList<>(recomendacoes);
        }

        List<Recomendacao> resultado = new ArrayList<>(recomendacoes);
        int inicioGrupo = 0;

        while (inicioGrupo < resultado.size()) {
            int fimGrupo = inicioGrupo + 1;
            while (fimGrupo < resultado.size() && mesmoEmpate(resultado.get(inicioGrupo), resultado.get(fimGrupo))) {
                fimGrupo++;
            }

            if (fimGrupo - inicioGrupo > 1) {
                embaralharGrupo(resultado, inicioGrupo, fimGrupo);
            }

            inicioGrupo = fimGrupo;
        }

        return resultado;
    }

    private boolean mesmoEmpate(Recomendacao primeira, Recomendacao segunda) {
        return Double.compare(primeira.getScore(), segunda.getScore()) == 0
                && primeira.getFilme().getPopularidade() == segunda.getFilme().getPopularidade();
    }

    private void embaralharGrupo(List<Recomendacao> recomendacoes, int inicio, int fimExclusivo) {
        for (int indice = fimExclusivo - 1; indice > inicio; indice--) {
            int indiceSorteado = gerador.sortearInteiro(inicio, indice + 1);
            Collections.swap(recomendacoes, indice, indiceSorteado);
        }
    }
}
