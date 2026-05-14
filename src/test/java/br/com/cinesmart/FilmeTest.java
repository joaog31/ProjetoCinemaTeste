package br.com.cinesmart;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cinesmart.modelo.ClassificacaoEtaria;
import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.Idioma;


@DisplayName("Teste: Filme")
class FilmeTest {

    @Test
    @DisplayName("deve criar um filme com todos os atributos preenchidos")
    void deveCriarFilmeComTodosAtributos() {
        
        Set<Genero> generos = Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA);
        
        
        Filme filme = new Filme("F001", "Duna", 2021, 166, generos,
                ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92);

        
        assertAll(
            () -> assertEquals("F001", filme.getId()),
            () -> assertEquals("Duna", filme.getTitulo()),
            () -> assertEquals(2021, filme.getAno()),
            () -> assertEquals(166, filme.getDuracao()),
            () -> assertEquals(2, filme.getGeneros().size()),
            () -> assertEquals(ClassificacaoEtaria.QUATORZE, filme.getClassificacao()),
            () -> assertEquals(Idioma.INGLES, filme.getIdioma()),
            () -> assertEquals(92, filme.getPopularidade())
        );
    }

    @Test
    @DisplayName("dois filmes com mesmo ID são considerados iguais")
    void deveConsiderarFilmesIguaisPorID() {
        
        Set<Genero> generos = Set.of(Genero.COMEDIA);
        Filme filme1 = new Filme("F002", "Click", 2006, 107, generos,
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 65);
        Filme filme2 = new Filme("F002", "Outro Título", 1999, 90, generos,
                ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 50);

        
        assertEquals(filme1, filme2);
        assertEquals(filme1.hashCode(), filme2.hashCode());
    }

    @Test
    @DisplayName("filme deve verificar se contém um gênero específico")
    void deveVerificarSeSomaGenero() {
        
        Set<Genero> generos = Set.of(Genero.ACAO, Genero.AVENTURA);
        Filme filme = new Filme("F003", "Avatar", 2009, 162, generos,
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 88);

        
        assertTrue(filme.temGenero(Genero.ACAO));
        assertTrue(filme.temGenero(Genero.AVENTURA));
        assertFalse(filme.temGenero(Genero.COMEDIA));
    }

    @Test
    @DisplayName("filme deve retornar cópia do conjunto de gêneros (imutabilidade)")
    void deveRetornarCopiaDeGeneros() {
        
        Set<Genero> generos = new HashSet<>(Set.of(Genero.DRAMA));
        Filme filme = new Filme("F004", "O Iluminado", 1980, 146, generos,
                ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 88);

        
        Set<Genero> generosRetornados = filme.getGeneros();

        
        assertThrows(UnsupportedOperationException.class, () -> generosRetornados.add(Genero.COMEDIA));
        assertFalse(filme.getGeneros().contains(Genero.COMEDIA));
        assertEquals(1, filme.getGeneros().size());
    }

    @Test
    @DisplayName("filme não deve aceitar ID nulo")
    void naoDeveAceitarIDNulo() {
        
        assertThrows(NullPointerException.class, () ->
            new Filme(null, "Título", 2020, 120, Set.of(Genero.DRAMA),
                    ClassificacaoEtaria.DOZE, Idioma.INGLES, 80)
        );
    }

    @Test
    @DisplayName("filme não deve aceitar título nulo")
    void naoDeveAceitarTituloNulo() {
        
        assertThrows(NullPointerException.class, ()->
            new Filme("F005", null, 2020, 120, Set.of(Genero.DRAMA),
                    ClassificacaoEtaria.DOZE, Idioma.INGLES, 80)
        );
    }
}
