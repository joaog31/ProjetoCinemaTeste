package br.com.cinesmart;

import java.util.List;
import java.util.Random;
import java.util.Set;

import br.com.cinesmart.modelo.ClassificacaoEtaria;
import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.Idioma;
import br.com.cinesmart.modelo.PerfilCinefilo;
import br.com.cinesmart.modelo.Recomendacao;
import br.com.cinesmart.modelo.Usuario;
import br.com.cinesmart.servico.CalculadoraScore;
import br.com.cinesmart.servico.CatalogoFilmesAPI;
import br.com.cinesmart.servico.CatalogoMock;
import br.com.cinesmart.servico.FiltroFilmes;
import br.com.cinesmart.servico.HistoricoUsuarioRepositorio;
import br.com.cinesmart.servico.NotificadorPush;
import br.com.cinesmart.servico.RecomendadorServico;
import br.com.cinesmart.utilitario.GeradorAleatorio;

/**
 * Classe main que demonstra uma recomendação real de filme
 * para um perfil de exemplo.
 */
public class MainRecomendador {

    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("  CineSmart - Recomendador de Filmes");
        System.out.println("========================================\n");

        // Criar implementações dos serviços
        CatalogoFilmesAPI catalogo = new CatalogoMock();
        HistoricoUsuarioRepositorio historico = new HistoricoMockSimples();
        NotificadorPush notificador = new NotificadorMockSimples();
        GeradorAleatorio gerador = new GeradorAleatorioPadrao();

        // Criar serviços
        CalculadoraScore calculadora = new CalculadoraScore();
        FiltroFilmes filtro = new FiltroFilmes();
        RecomendadorServico recomendador = new RecomendadorServico(
            catalogo, historico, notificador, gerador, calculadora, filtro
        );

        // Criar perfil de exemplo: gosta de FC e Drama
    System.out.println("  CRIANDO PERFIL DE EXEMPLO:");
    System.out.println("   Preferências: Ficção Científica (80%) + Drama (70%)");
    System.out.println("   Duração ideal: 90 a 180 minutos");
    System.out.println("   Classificação máxima: 12 anos");
    System.out.println("   Idiomas aceitos: Português e Inglês");
    System.out.println("   Já assistiu: Interestelar (nota 5)");

    PerfilCinefilo perfil = new PerfilCinefilo(90, 180, ClassificacaoEtaria.DOZE,
        Set.of(Idioma.PORTUGUES, Idioma.INGLES));

    perfil.setPeso(Genero.FICCAO_CIENTIFICA, 0.80);
    perfil.setPeso(Genero.DRAMA, 0.70);
    perfil.setPeso(Genero.ACAO, 0.50);
    perfil.setPeso(Genero.AVENTURA, 0.30);
    perfil.marcarComoAssistido("F002");
    perfil.adicionarNota("F002", 5);

        // Criar usuário
        Usuario usuario = new Usuario("user_123", "João Silva", 28, perfil, true);

        // Gerar recomendações
        System.out.println("\n🎬 GERANDO RECOMENDAÇÕES...\n");
        List<Recomendacao> recomendacoes = recomendador.recomendar(usuario, 1);

        // Exibir resultado
        if (!recomendacoes.isEmpty()) {
            Recomendacao rec = recomendacoes.get(0);
            Filme filme = rec.getFilme();

            System.out.println("========================================");
            System.out.println("   RECOMENDAÇÃO GERADA COM SUCESSO!");
            System.out.println("========================================\n");

            System.out.println(" Filme: " + filme.getTitulo());
            System.out.println(" Ano: " + filme.getAno());
            System.out.println(" Duração: " + filme.getDuracao() + " minutos");
            System.out.println(" Gêneros: " + filme.getGeneros());
            System.out.println(" Classificação: " + filme.getClassificacao());
            System.out.println(" Idioma: " + filme.getIdioma());
            System.out.println(" Popularidade: " + filme.getPopularidade() + "%");
            System.out.println(" Score de Compatibilidade: " + String.format("%.1f", rec.getScore()) + "/100");

            System.out.println("\n JUSTIFICATIVA:");
            System.out.println("   \"" + rec.getJustificativa() + "\"");

            System.out.println("\n========================================\n");
        } else {
            System.out.println("Nenhuma recomendação disponível para este perfil.\n");
        }

        // ===== MODO SURPREENDA-ME =====
    System.out.println("\n MODO SURPREENDA-ME ");
    System.out.println("Gerando uma recomendação aleatória...\n");

    List<Recomendacao> surpresa = recomendador.recomendarAleatorio(usuario);
    if (!surpresa.isEmpty()) {
        Recomendacao recSurpresa = surpresa.get(0);
        Filme filmeSurpresa = recSurpresa.getFilme();
        System.out.println(" Filme sugerido: " + filmeSurpresa.getTitulo());
        System.out.println(" Justificativa: " + recSurpresa.getJustificativa());
        System.out.println(" Score: " + String.format("%.1f", recSurpresa.getScore()) + "/100");
    } else {
        System.out.println(" Nenhum filme disponível para o modo surpresa.");
}


    }

    // ===== Implementações Mock =====

    static class HistoricoMockSimples implements HistoricoUsuarioRepositorio {
        @Override
        public void registrarRecomendacao(Usuario usuario, List<Recomendacao> recomendacoes) {
            // Mock não faz nada
        }

        @Override
        public void registrarNota(String usuarioId, String filmeId, int nota) {
            // Mock não faz nada
        }
    }

    static class NotificadorMockSimples implements NotificadorPush {
        @Override
        public void enviar(Usuario usuario, List<Recomendacao> recomendacoes) {
            // Mock n??o faz nada
        }
    }

    static class GeradorAleatorioPadrao implements GeradorAleatorio {
        private final Random random = new Random();

        @Override
        public int sortearInteiro(int min, int max) {
            return min + random.nextInt(max - min);
        }

        @Override
        public double sortearDouble() {
            return random.nextDouble();
        }
    }
}
