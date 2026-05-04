package br.com.cinesmart.modelo;

/**
 * Enumeração de gêneros de filmes disponíveis no CineSmart.
 */
public enum Genero {
    ACAO("Ação"),
    COMEDIA("Comédia"),
    DRAMA("Drama"),
    FICCAO_CIENTIFICA("Ficção Científica"),
    ROMANCE("Romance"),
    TERROR("Terror"),
    AVENTURA("Aventura"),
    DOCUMENTARIO("Documentário"),
    ANIMACAO("Animação"),
    POLICIAL("Policial"),
    SUSPENSE("Suspense"),
    MUSICAL("Musical"),
    FANTASMA("Fantasma");

    private final String descricao;

    Genero(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
