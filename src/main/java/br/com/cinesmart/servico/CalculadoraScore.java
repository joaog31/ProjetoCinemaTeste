package br.com.cinesmart.servico;

import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.PerfilCinefilo;

public class CalculadoraScore {
    private static final double PESO_GENERO = 0.50;
    private static final double PESO_DURACAO = 0.20;
    private static final double PESO_POPULARIDADE = 0.15;
    private static final double PESO_AFINIDADE = 0.15;

    private static final double SCORE_MAXIMO = 100.0;
    private static final double SCORE_MINIMO = 0.0;
    private static final int TOLERANCIA_DURACAO_MINUTOS = 30;
    private static final double LIMIAR_BAIXA_ADERENCIA_GENERO = 20.0;
    private static final double TETO_SCORE_BAIXA_ADERENCIA_GENERO = 30.0;

    public double calcular(Filme filme, PerfilCinefilo perfil) {
        double scoreGenero = calcularComponenteGenero(filme, perfil);
        double scoreDuracao = calcularComponenteDuracao(filme, perfil);
        double scorePopularidade = normalizarPopularidade(filme.getPopularidade());
        double scoreAfinidade = calcularComponenteAfinidade(filme, perfil);

        double scoreTotal =
            (scoreGenero * PESO_GENERO) +
            (scoreDuracao * PESO_DURACAO) +
            (scorePopularidade * PESO_POPULARIDADE) +
            (scoreAfinidade * PESO_AFINIDADE);

        if (scoreGenero < LIMIAR_BAIXA_ADERENCIA_GENERO) {
            scoreTotal = Math.min(scoreTotal, TETO_SCORE_BAIXA_ADERENCIA_GENERO);
        }

        return Math.max(SCORE_MINIMO, Math.min(SCORE_MAXIMO, scoreTotal));
    }

    private double calcularComponenteGenero(Filme filme, PerfilCinefilo perfil) {
        if (filme.getGeneros().isEmpty()) {
            return 0.0;
        }

        double somaPesos = 0.0;
        for (Genero genero : filme.getGeneros()) {
            somaPesos += perfil.getPeso(genero);
        }

        double mediaPesos = somaPesos / filme.getGeneros().size();
        return mediaPesos * SCORE_MAXIMO;
    }

    private double calcularComponenteDuracao(Filme filme, PerfilCinefilo perfil) {
        int duracao = filme.getDuracao();
        int minima = perfil.getDuracaoMinima();
        int maxima = perfil.getDuracaoMaxima();

        if (duracao >= minima && duracao <= maxima) {
            return SCORE_MAXIMO;
        }

        if (duracao > maxima) {
            int excesso = duracao - maxima;
            if (excesso > TOLERANCIA_DURACAO_MINUTOS) {
                return Math.max(0, SCORE_MAXIMO - (excesso * 2));
            }
            return SCORE_MAXIMO - (excesso * 1.5);
        }

        int deficit = minima - duracao;
        return Math.max(0, SCORE_MAXIMO - (deficit * 1.5));
    }

    private double calcularComponenteAfinidade(Filme filme, PerfilCinefilo perfil) {
        if (perfil.getNotas().isEmpty()) {
            return 50.0;
        }

        double somaNotasAltas = 0.0;
        int filmesSimilaresAvaliados = 0;

        for (String filmeId : perfil.getNotas().keySet()) {
            Integer nota = perfil.getNotaPara(filmeId);
            if (nota != null && nota >= 4) {
                somaNotasAltas += nota;
                filmesSimilaresAvaliados++;
            }
        }

        if (filmesSimilaresAvaliados == 0) {
            return 50.0;
        }

        double mediaNotasAltas = somaNotasAltas / filmesSimilaresAvaliados;
        return (mediaNotasAltas / 5.0) * SCORE_MAXIMO;
    }

    private double normalizarPopularidade(int popularidade) {
        return Math.max(SCORE_MINIMO, Math.min(SCORE_MAXIMO, (double) popularidade));
    }
}
