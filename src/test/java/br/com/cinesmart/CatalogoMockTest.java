package br.com.cinesmart;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.servico.CatalogoMock;

@DisplayName("Teste: CatalogoMock")
class CatalogoMockTest {

    @Test
    @DisplayName("deve retornar todos os filmes do catálogo")
    void deveRetornarTodosOsFilmes() {
        CatalogoMock catalogo = new CatalogoMock();

        List<Filme> filmes = catalogo.buscarTodos();

        assertNotNull(filmes);
        assertEquals(30, filmes.size());
    }

    @Test
    @DisplayName("deve retornar cópia independente da lista de filmes")
    void deveRetornarCopiaIndependente() {
        CatalogoMock catalogo = new CatalogoMock();

        List<Filme> primeiraLeitura = catalogo.buscarTodos();
        primeiraLeitura.remove(0);

        List<Filme> segundaLeitura = catalogo.buscarTodos();

        assertEquals(30, segundaLeitura.size());
    }

    @Test
    @DisplayName("deve buscar filme por id existente")
    void deveBuscarFilmePorIdExistente() {
        CatalogoMock catalogo = new CatalogoMock();

        Filme filme = catalogo.buscarPorId("F010");

        assertNotNull(filme);
        assertEquals("F010", filme.getId());
        assertEquals("Divertida Mente", filme.getTitulo());
    }

    @Test
    @DisplayName("deve retornar null quando id não existir")
    void deveRetornarNullQuandoIdNaoExistir() {
        CatalogoMock catalogo = new CatalogoMock();

        assertNull(catalogo.buscarPorId("NAO_EXISTE"));
    }
}