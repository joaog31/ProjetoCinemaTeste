package br.com.cinesmart.modelo;

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

    public boolean ehMaiorOuIgual(ClassificacaoEtaria outra) {
        return this.idade >= outra.idade;
    }
}
