package br.com.cinesmart.modelo;

/**
 * Enumeração de idiomas de filmes suportados pelo CineSmart.
 */
public enum Idioma {
    PORTUGUES("Português"),
    INGLES("Inglês"),
    ESPANHOL("Espanhol"),
    FRANCES("Francês"),
    ITALIANO("Italiano"),
    RUSSO("Russo"),
    CHINES("Chinês"),
    JAPONES("Japonês"),
    COREANO("Coreano");

    private final String descricao;

    Idioma(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
