package br.com.cinesmart.servico;

import java.util.List;

import br.com.cinesmart.modelo.Filme;

public interface CatalogoFilmesAPI {
    List<Filme> buscarTodos() throws Exception;

    Filme buscarPorId(String id) throws Exception;
}
