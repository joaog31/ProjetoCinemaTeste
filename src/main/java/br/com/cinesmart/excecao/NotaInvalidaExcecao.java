package br.com.cinesmart.excecao;

public class NotaInvalidaExcecao extends IllegalArgumentException {
    public NotaInvalidaExcecao(String mensagem) {
        super(mensagem);
    }

    public NotaInvalidaExcecao(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
