package br.com.cinesmart.servico;

import java.util.List;

import br.com.cinesmart.modelo.Recomendacao;
import br.com.cinesmart.modelo.Usuario;

public interface NotificadorPush {
    void enviar(Usuario usuario, List<Recomendacao> recomendacoes);
}
