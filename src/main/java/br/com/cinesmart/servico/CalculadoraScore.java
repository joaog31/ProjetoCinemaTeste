package br.com.cinesmart.servico;

import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.PerfilCinefilo;

/**
 * Responsável pelo cálculo de compatibilidade entre um filme e o perfil de um usuário.
 * Calcula um score de 0 a 100 baseado em:
 * - Compatibilidade de gênero (50%)
 * - Aderência à duração preferida (20%)
 * - Popularidade do filme (15%)
 * - Bônus de afinidade histórica (15%)
 * 
 * Lógica pura: não tem dependências externas.
 */
public class CalculadoraScore {
    
    // Constantes da fórmula (nenhum número mágico)
    private static final double PESO_GENERO = 0.50;
    private static final double PESO_DURACAO = 0.20;
    private static final double PESO_POPULARIDADE = 0.15;
    private static final double PESO_AFINIDADE = 0.15;
    
    // Constantes auxiliares
    private static final double SCORE_MAXIMO = 100.0;
    private static final double SCORE_MINIMO = 0.0;
    private static final int TOLERANCIA_DURACAO_MINUTOS = 30;

    /**
     * Calcula o score de compatibilidade entre um filme e o perfil do usuário.
     * @param filme o filme a avaliar
     * @param perfil o perfil do usuário
     * @return score entre 0.0 e 100.0
     */
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

        return Math.max(SCORE_MINIMO, Math.min(SCORE_MAXIMO, scoreTotal));
    }

    /**
     * Calcula o componente de compatibilidade de gênero (50%).
     * Média ponderada dos pesos dos gêneros do filme.
     * @param filme o filme
     * @param perfil o perfil do usuário
     * @return score 0-100
     */
    private double calcularComponenteGenero(Filme filme, PerfilCinefilo perfil) {
        if (filme.getGeneros().isEmpty()) {
            return 0.0;
        }

        double somaPesos = 0.0;
        for (Genero genero : filme.getGeneros()) {
            somaPesos += perfil.getPeso(genero);
        }

        double mediaPesos = somaPesos / filme.getGeneros().size();
        return mediaPesos * SCORE_MAXIMO; // normaliza para 0-100
    }

    /**
     * Calcula o componente de aderência à duração (20%).
     * 100 se está na faixa preferida; reduz proporcionalmente se está fora.
     * @param filme o filme
     * @param perfil o perfil do usuário
     * @return score 0-100
     */
    private double calcularComponenteDuracao(Filme filme, PerfilCinefilo perfil) {
        int duracao = filme.getDuracao();
        int minima = perfil.getDuracaoMinima();
        int maxima = perfil.getDuracaoMaxima();

        // Se está dentro da faixa preferida
        if (duracao >= minima && duracao <= maxima) {
            return SCORE_MAXIMO;
        }

        // Se está acima: penalizar proporcionalmente (até tolerância)
        if (duracao > maxima) {
            int excesso = duracao - maxima;
            if (excesso > TOLERANCIA_DURACAO_MINUTOS) {
                return Math.max(0, SCORE_MAXIMO - (excesso * 2)); // penalidade de 2 pontos por minuto
            }
            return SCORE_MAXIMO - (excesso * 1.5);
        }

        // Se está abaixo: penalizar proporcionalmente
        int deficit = minima - duracao;
        return Math.max(0, SCORE_MAXIMO - (deficit * 1.5));
    }

    /**
     * Calcula o componente de bônus de afinidade (15%).
     * Se o usuário deu notas altas a filmes do mesmo gênero, soma bônus.
     * @param filme o filme
     * @param perfil o perfil do usuário
     * @return score 0-100
     */
    private double calcularComponenteAfinidade(Filme filme, PerfilCinefilo perfil) {
        // Se não há notas registradas, retorna 50 (neutro)
        if (perfil.getNotas().isEmpty()) {
            return 50.0;
        }

        double somaNotasAltas = 0.0;
        int filmesSimilaresAvaliados = 0;

        // Procura filmes semelhantes que foram avaliados
        for (String filmeId : perfil.getNotas().keySet()) {
            Integer nota = perfil.getNotaPara(filmeId);
            if (nota != null && nota >= 4) { // consideramos notas >= 4 como altas
                // Nota: em uma implementação real, buscaria os gêneros deste filme também
                // Por enquanto, somamos as notas altas registradas
                somaNotasAltas += nota;
                filmesSimilaresAvaliados++;
            }
        }

        if (filmesSimilaresAvaliados == 0) {
            return 50.0; // sem histórico de notas altas, retorna neutro
        }

        double mediaNotasAltas = somaNotasAltas / filmesSimilaresAvaliados;
        return (mediaNotasAltas / 5.0) * SCORE_MAXIMO; // normaliza para 0-100
    }

    /**
     * Normaliza a popularidade do filme (0-100) para usar na fórmula.
     * @param popularidade valor de popularidade do filme
     * @return score 0-100
     */
    private double normalizarPopularidade(int popularidade) {
        return Math.max(SCORE_MINIMO, Math.min(SCORE_MAXIMO, (double) popularidade));
    }
}
