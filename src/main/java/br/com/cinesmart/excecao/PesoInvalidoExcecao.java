package br.com.cinesmart.excecao;

/**
 * Exceção lançada quando um peso de gênero está fora do intervalo [0.0, 1.0].
 */
public class PesoInvalidoExcecao extends IllegalArgumentException {
    public PesoInvalidoExcecao(String mensagem) {
        super(mensagem);
    }

    public PesoInvalidoExcecao(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
