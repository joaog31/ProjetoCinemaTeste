package br.com.cinesmart.excecao;

/**
 * Exceção lançada quando uma nota de filme está fora do intervalo [1, 5].
 */
public class NotaInvalidaExcecao extends IllegalArgumentException {
    public NotaInvalidaExcecao(String mensagem) {
        super(mensagem);
    }

    public NotaInvalidaExcecao(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
