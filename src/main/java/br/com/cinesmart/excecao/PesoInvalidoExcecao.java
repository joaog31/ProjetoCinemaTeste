package br.com.cinesmart.excecao;

public class PesoInvalidoExcecao extends IllegalArgumentException {
    public PesoInvalidoExcecao(String mensagem) {
        super(mensagem);
    }

    public PesoInvalidoExcecao(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
