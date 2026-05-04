package br.com.cinesmart.servico;

import br.com.cinesmart.modelo.Usuario;
import br.com.cinesmart.modelo.Recomendacao;
import java.util.List;

/**
 * Interface para envio de notificações push ao usuário.
 * Em produção, será integrada com serviços como Firebase ou OneSignal.
 * Nos testes, será mockada.
 */
public interface NotificadorPush {
    /**
     * Envia uma notificação de recomendação ao usuário.
     * @param usuario o usuário que receberá a notificação
     * @param recomendacoes lista de recomendações para notificar
     */
    void enviar(Usuario usuario, List<Recomendacao> recomendacoes);
}
