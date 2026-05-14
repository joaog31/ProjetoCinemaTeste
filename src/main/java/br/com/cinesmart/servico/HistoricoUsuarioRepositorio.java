package br.com.cinesmart.servico;

import java.util.List;

import br.com.cinesmart.modelo.Recomendacao;
import br.com.cinesmart.modelo.Usuario;

public interface HistoricoUsuarioRepositorio {
    void registrarRecomendacao(Usuario usuario, List<Recomendacao> recomendacoes);

    void registrarNota(String usuarioId, String filmeId, int nota);
}
