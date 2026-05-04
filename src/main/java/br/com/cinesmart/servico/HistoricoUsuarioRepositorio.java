package br.com.cinesmart.servico;

import br.com.cinesmart.modelo.Usuario;
import br.com.cinesmart.modelo.Recomendacao;
import java.util.List;

/**
 * Interface para persistência do histórico de recomendações e notas do usuário.
 * Em produção, esta será um acesso a banco de dados.
 * Nos testes, será mockada.
 */
public interface HistoricoUsuarioRepositorio {
    /**
     * Registra uma lista de recomendações feita para o usuário.
     * @param usuario o usuário que recebeu as recomendações
     * @param recomendacoes lista de recomendações
     */
    void registrarRecomendacao(Usuario usuario, List<Recomendacao> recomendacoes);

    /**
     * Registra uma nota dada pelo usuário a um filme.
     * @param usuarioId ID do usuário
     * @param filmeId ID do filme
     * @param nota valor entre 1 e 5
     */
    void registrarNota(String usuarioId, String filmeId, int nota);
}
