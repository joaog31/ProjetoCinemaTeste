package br.com.cinesmart.modelo;

/**
 * Enumeração de classificações etárias para filmes no Brasil.
 */
public enum ClassificacaoEtaria {
    LIVRE(0, "Livre"),
    DEZ(10, "10 anos"),
    DOZE(12, "12 anos"),
    QUATORZE(14, "14 anos"),
    DEZESSEIS(16, "16 anos"),
    DEZOITO(18, "18 anos");

    private final int idade;
    private final String descricao;

    ClassificacaoEtaria(int idade, String descricao) {
        this.idade = idade;
        this.descricao = descricao;
    }

    public int getIdade() {
        return idade;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Verifica se uma classificação é maior ou igual à outra.
     * @param outra classificação a comparar
     * @return true se esta classificação é >= outra
     */
    public boolean ehMaiorOuIgual(ClassificacaoEtaria outra) {
        return this.idade >= outra.idade;
    }
}
