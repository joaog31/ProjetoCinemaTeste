package br.com.cinesmart.modelo;

import java.util.Objects;

public final class Recomendacao {
    private final Filme filme;
    private final double score;
    private final String justificativa;

    public Recomendacao(Filme filme, double score, String justificativa) {
        this.filme = Objects.requireNonNull(filme);
        this.score = Math.max(0.0, Math.min(100.0, score));
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
