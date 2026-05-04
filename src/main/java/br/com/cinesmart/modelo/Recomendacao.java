package br.com.cinesmart.modelo;

import java.util.Objects;

/**
 * Representa uma recomendação de filme para um usuário.
 * Imutável: contém o filme recomendado, o score calculado e uma justificativa textual.
 */
public final class Recomendacao {
    private final Filme filme;
    private final double score;
    private final String justificativa;

    /**
     * Construtor para criar uma recomendação.
     * @param filme o filme recomendado
     * @param score score de compatibilidade (0.0 a 100.0)
     * @param justificativa explicação textual da recomendação
     */
    public Recomendacao(Filme filme, double score, String justificativa) {
        this.filme = Objects.requireNonNull(filme);
        this.score = Math.max(0.0, Math.min(100.0, score)); // garante 0-100
        this.justificativa = Objects.requireNonNull(justificativa);
    }

    public Filme getFilme() {
        return filme;
    }

    public double getScore() {
        return score;
    }

    public String getJustificativa() {
        return justificativa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recomendacao)) return false;
        Recomendacao that = (Recomendacao) o;
        return Objects.equals(filme, that.filme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filme);
    }

    @Override
    public String toString() {
        return "Recomendacao{" +
                "filme=" + filme.getTitulo() +
                ", score=" + String.format("%.2f", score) +
                '}';
    }
}
