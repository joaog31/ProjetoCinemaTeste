package br.com.cinesmart.excecao;

public class DuracaoInvalidaExcecao extends IllegalArgumentException {
    public DuracaoInvalidaExcecao(String mensagem) {
        super(mensagem);
    }

    public DuracaoInvalidaExcecao(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
