package br.com.cinesmart.excecao;

public class PerfilIncompletoExcecao extends IllegalStateException {
    public PerfilIncompletoExcecao(String mensagem) {
        super(mensagem);
    }

    public PerfilIncompletoExcecao(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
