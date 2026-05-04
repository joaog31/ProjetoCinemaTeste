package br.com.cinesmart.servico;

import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Recomendacao;
import br.com.cinesmart.modelo.Usuario;
import br.com.cinesmart.utilitario.GeradorAleatorio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Orquestrador principal do sistema de recomendações.
 * Responsável por:
 * 1. Buscar catálogo de filmes (via CatalogoFilmesAPI)
 * 2. Filtrar filmes (via FiltroFilmes)
 * 3. Calcular scores (via CalculadoraScore)
 * 4. Ranquear resultados
 * 5. Registrar recomendações (via HistoricoUsuarioRepositorio)
 * 6. Enviar notificações (via NotificadorPush)
 */
public class RecomendadorServico {
    private static final Logger logger = LoggerFactory.getLogger(RecomendadorServico.class);

    private final CatalogoFilmesAPI catalogo;
    private final HistoricoUsuarioRepositorio historico;
    private final NotificadorPush notificador;
    private final GeradorAleatorio gerador;
    private final CalculadoraScore calculadora;
    private final FiltroFilmes filtro;

    /**
     * Construtor com injeção de dependências.
     * @param catalogo API de catálogo de filmes
     * @param historico repositório de histórico de usuário
     * @param notificador serviço de notificações push
     * @param gerador gerador de números aleatórios
     * @param calculadora calculadora de scores
     * @param filtro filtro de filmes
     */
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

    /**
     * Gera recomendações de filmes para um usuário.
     * @param usuario o usuário
     * @param topN quantidade de recomendações desejada
     * @return lista ordenada de até N recomendações, vazia se nenhuma disponível
     */
    public List<Recomendacao> recomendar(Usuario usuario, int topN) {
        try {
            // Passo 1: Buscar catálogo
            List<Filme> catalogoCompleto = catalogo.buscarTodos();
            logger.debug("Catálogo obtido com {} filmes", catalogoCompleto.size());

            // Passo 2: Filtrar
            List<Filme> filmesValidos = filtro.filtrar(catalogoCompleto, usuario.getPerfil());
            logger.debug("Após filtro: {} filmes válidos", filmesValidos.size());

            if (filmesValidos.isEmpty()) {
                logger.info("Nenhum filme válido encontrado para o usuário {}", usuario.getId());
                return Collections.emptyList();
            }

            // Passo 3: Calcular scores
            List<Recomendacao> recomendacoes = new ArrayList<>();
            for (Filme filme : filmesValidos) {
                double score = calculadora.calcular(filme, usuario.getPerfil());
                String justificativa = gerarJustificativa(filme, usuario, score);
                recomendacoes.add(new Recomendacao(filme, score, justificativa));
            }

            // Passo 4: Ranquear (ordenar por score desc, depois popularidade desc, depois aleatório)
            recomendacoes.sort(Comparator.comparingDouble(Recomendacao::getScore).reversed()
                    .thenComparingInt(r -> r.getFilme().getPopularidade()).reversed());

            // Passo 5: Aplicar desempate aleatório se necessário
            recomendacoes = aplicarDesempateAleatorio(recomendacoes);

            // Passo 6: Retornar apenas topN
            List<Recomendacao> resultado = recomendacoes.subList(0, Math.min(topN, recomendacoes.size()));

            // Passo 7: Registrar no histórico
            historico.registrarRecomendacao(usuario, new ArrayList<>(resultado));

            // Passo 8: Enviar notificação (se habilitada)
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

    /**
     * Retorna um filme aleatório do conjunto de filmes válidos (modo "Surpreenda-me").
     * @param usuario o usuário
     * @return uma recomendação aleatória, ou vazio se nenhum filme válido
     */
    public List<Recomendacao> recomendarAleatorio(Usuario usuario) {
        try {
            List<Filme> catalogoCompleto = catalogo.buscarTodos();
            List<Filme> filmesValidos = filtro.filtrar(catalogoCompleto, usuario.getPerfil());

            if (filmesValidos.isEmpty()) {
                return Collections.emptyList();
            }

            // Sorteia um filme aleatório
            int indice = gerador.sortearInteiro(0, filmesValidos.size());
            Filme filmeAleatorio = filmesValidos.get(indice);

            double score = calculadora.calcular(filmeAleatorio, usuario.getPerfil());
            String justificativa = "Surpreenda-me! Este filme pode ser uma ótima descoberta para você.";
            Recomendacao recomendacao = new Recomendacao(filmeAleatorio, score, justificativa);

            // Registrar e notificar
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

    /**
     * Gera uma justificativa textual para a recomendação.
     * @param filme o filme recomendado
     * @param usuario o usuário
     * @param score o score calculado
     * @return texto de justificativa
     */
    private String gerarJustificativa(Filme filme, Usuario usuario, double score) {
        StringBuilder justificativa = new StringBuilder();
        justificativa.append("Recomendamos '").append(filme.getTitulo()).append("' (")
                .append(filme.getAno()).append(") ");

        // Mencionar gêneros que o usuário gosta
        List<String> generosAmados = new ArrayList<>();
        filme.getGeneros().forEach(g -> {
            if (usuario.getPerfil().getPeso(g) > 0.5) {
                generosAmados.add(g.getDescricao());
            }
        });

        if (!generosAmados.isEmpty()) {
            justificativa.append("porque você gosta de ").append(String.join(" e ", generosAmados)).append(". ");
        }

        // Menção a popularidade
        if (filme.getPopularidade() > 80) {
            justificativa.append("É um filme muito popular! ");
        }

        justificativa.append("Score de compatibilidade: ").append(String.format("%.1f", score)).append("%");

        return justificativa.toString();
    }

    /**
     * Aplica desempate aleatório para recomendações com scores iguais.
     * @param recomendacoes lista de recomendações
     * @return lista com desempate aplicado
     */
    private List<Recomendacao> aplicarDesempateAleatorio(List<Recomendacao> recomendacoes) {
        // Para simplificar em 50%, apenas retornar na ordem atual
        // Em uma versão completa, agruparia por score e sortearia dentro de cada grupo
        return recomendacoes;
    }
}
