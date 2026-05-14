package br.com.cinesmart.modelo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Filme {
    private final String id;
    private final String titulo;
    private final int ano;
    private final int duracao;
    private final Set<Genero> generos;
    private final ClassificacaoEtaria classificacao;
    private final Idioma idioma;
    private final int popularidade;

    public Filme(String id, String titulo, int ano, int duracao, Set<Genero> generos,
                 ClassificacaoEtaria classificacao, Idioma idioma, int popularidade) {
        this.id = Objects.requireNonNull(id, "ID do filme não pode ser null");
        this.titulo = Objects.requireNonNull(titulo, "Título do filme não pode ser null");
        this.ano = ano;
        this.duracao = duracao;
        this.generos = Collections.unmodifiableSet(new HashSet<>(generos));
        this.classificacao = Objects.requireNonNull(classificacao, "Classificação não pode ser null");
        this.idioma = Objects.requireNonNull(idioma, "Idioma não pode ser null");
        this.popularidade = popularidade;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getAno() {
        return ano;
    }

    public int getDuracao() {
        return duracao;
    }

    public Set<Genero> getGeneros() {
        return generos;
    }

    public ClassificacaoEtaria getClassificacao() {
        return classificacao;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public int getPopularidade() {
        return popularidade;
    }

    public boolean temGenero(Genero genero) {
        return generos.contains(genero);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Filme)) return false;
        Filme filme = (Filme) o;
        return Objects.equals(id, filme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Filme{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", ano=" + ano +
                ", duracao=" + duracao +
                ", classificacao=" + classificacao +
                ", idioma=" + idioma +
                '}';
    }
}
