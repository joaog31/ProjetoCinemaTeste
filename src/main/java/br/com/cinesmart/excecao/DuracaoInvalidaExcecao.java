package br.com.cinesmart.excecao;

/**
 * Exceção lançada quando a duração mínima é maior que a máxima, ou valores inválidos.
 */
public class DuracaoInvalidaExcecao extends IllegalArgumentException {
    public DuracaoInvalidaExcecao(String mensagem) {
        super(mensagem);
    }

    public DuracaoInvalidaExcecao(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
