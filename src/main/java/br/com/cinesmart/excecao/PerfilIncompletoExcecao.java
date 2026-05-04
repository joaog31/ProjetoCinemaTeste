package br.com.cinesmart.excecao;

/**
 * Exceção lançada quando o perfil de usuário está incompleto ou inválido.
 */
public class PerfilIncompletoExcecao extends IllegalStateException {
    public PerfilIncompletoExcecao(String mensagem) {
        super(mensagem);
    }

    public PerfilIncompletoExcecao(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
