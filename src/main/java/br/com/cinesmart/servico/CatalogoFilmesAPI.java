package br.com.cinesmart.servico;

import br.com.cinesmart.modelo.Filme;
import java.util.List;

/**
 * Interface para acesso ao catálogo de filmes.
 * Em produção, esta será uma chamada HTTP a uma API como TMDB ou OMDB.
 * Nos testes, será mockada para controlar os dados retornados.
 */
public interface CatalogoFilmesAPI {
    /**
     * Busca todos os filmes disponíveis no catálogo.
     * @return lista de filmes disponíveis
     * @throws Exception se houver erro na comunicação com a API
     */
    List<Filme> buscarTodos() throws Exception;

    /**
     * Busca um filme pelo ID.
     * @param id do filme
     * @return filme encontrado ou null
     * @throws Exception se houver erro na comunicação com a API
     */
    Filme buscarPorId(String id) throws Exception;
}
