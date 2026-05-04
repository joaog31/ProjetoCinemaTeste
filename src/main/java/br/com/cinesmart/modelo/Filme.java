package br.com.cinesmart.modelo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Representa um filme no catálogo do CineSmart.
 * Imutável: todos os valores são definidos no construtor e não podem ser alterados.
 */
public final class Filme {
    private final String id;
    private final String titulo;
    private final int ano;
    private final int duracao;
    private final Set<Genero> generos;
    private final ClassificacaoEtaria classificacao;
    private final Idioma idioma;
    private final int popularidade;

    /**
     * Construtor para criar um filme.
     * @param id identificador único do filme
     * @param titulo nome do filme
     * @param ano ano de lançamento
     * @param duracao duração em minutos
     * @param generos conjunto de gêneros
     * @param classificacao classificação etária
     * @param idioma idioma original do filme
     * @param popularidade índice de popularidade (0-100)
     */
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
        return new HashSet<>(generos);
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

    /**
     * Verifica se o filme contém um determinado gênero.
     * @param genero a buscar
     * @return true se o filme pertence a este gênero
     */
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
