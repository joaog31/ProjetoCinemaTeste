package br.com.cinesmart;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cinesmart.modelo.ClassificacaoEtaria;
import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.Idioma;
import br.com.cinesmart.modelo.PerfilCinefilo;
import br.com.cinesmart.servico.FiltroFilmes;

/**
 * Testes unitários para a classe FiltroFilmes.
 */
@DisplayName("Teste: FiltroFilmes")
class FiltroFilmesTest {

    private FiltroFilmes filtro;
    private PerfilCinefilo perfil;
    private List<Filme> catalogo;

    @BeforeEach
    void setUp() {
        filtro = new FiltroFilmes();
        perfil = new PerfilCinefilo(90, 150, ClassificacaoEtaria.DEZESSEIS,
                Set.of(Idioma.PORTUGUES, Idioma.INGLES));

        // Criar catálogo de teste com 5 filmes
        catalogo = new ArrayList<>();
        
        // F01: Duna (FC, Drama, 14 anos, Inglês) - VÁLIDO
        catalogo.add(new Filme("F01", "Duna", 2021, 166,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92));

        // F02: Her (FC, Drama, Romance, 16 anos, Inglês) - VÁLIDO
        catalogo.add(new Filme("F02", "Her", 2013, 126,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA, Genero.ROMANCE),
                ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 78));

        // F03: O Iluminado (Terror, 18 anos, Inglês) - INVÁLIDO: acima classificação
        catalogo.add(new Filme("F03", "O Iluminado", 1980, 146,
                Set.of(Genero.TERROR),
                ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 88));

        // F04: A Origem (FC, Drama, 12 anos, Inglês) - VÁLIDO
        catalogo.add(new Filme("F04", "A Origem", 2010, 148,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 85));

        // F05: Matrix (FC, Ação, 12 anos, Inglês) - JÁ ASSISTIDO
        catalogo.add(new Filme("F05", "Matrix", 1999, 136,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.ACAO),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 95));

        // F06: Interestelar (FC, Drama, 12 anos, Português) - VÁLIDO
        catalogo.add(new Filme("F06", "Interestelar", 2014, 169,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.PORTUGUES, 90));

        // F07: Tropa de Elite (Ação, Drama, 18 anos, Português) - INVÁLIDO: acima classificação
        catalogo.add(new Filme("F07", "Tropa de Elite", 2007, 115,
                Set.of(Genero.ACAO, Genero.DRAMA),
                ClassificacaoEtaria.DEZOITO, Idioma.PORTUGUES, 80));

        // F08: Click (Comédia, Drama, 12 anos, Inglês, com todos gêneros peso 0) - INVÁLIDO
        catalogo.add(new Filme("F08", "Click", 2006, 107,
                Set.of(Genero.COMEDIA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 65));

        // F09: Stranger Things Ep 1 (Terror, 16 anos, Português) - INVÁLIDO: gênero peso 0
        catalogo.add(new Filme("F09", "Stranger Things", 2016, 50,
                Set.of(Genero.TERROR),
                ClassificacaoEtaria.DEZESSEIS, Idioma.PORTUGUES, 92));
    }

    @Test
    @DisplayName("deve remover filmes já assistidos")
    void deveRemoverFilmesJaAssistidos() {
        // Arrange
        perfil.marcarComoAssistido("F05");

        // Act
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        // Assert
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F05")));
    }

    @Test
    @DisplayName("deve remover filmes acima da classificação máxima")
    void deveRemoverFilmesAcimaClassificacao() {
        // Act
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        // Assert
        // F03 (18 anos) e F07 (18 anos) devem ser removidos (máxima é 16)
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F03")));
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F07")));
    }

    @Test
    @DisplayName("deve remover filmes em idioma não aceito")
    void deveRemoverFilmesIdiomaInvalido() {
        // Arrange
        PerfilCinefilo perfilSoIngles = new PerfilCinefilo(90, 150,
                ClassificacaoEtaria.DEZESSEIS, Set.of(Idioma.INGLES)); // apenas inglês

        // Act
        List<Filme> resultado = filtro.filtrar(catalogo, perfilSoIngles);

        // Assert
        // F06 é em português, deve ser removido
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F06")));
        // F09 é em português, deve ser removido
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F09")));
    }

    @Test
    @DisplayName("deve remover filmes com todos os gêneros de peso 0.0")
    void deveRemoverFilmesComTodosGenerosComPeso0() {
        // Arrange
        perfil.setPeso(Genero.COMEDIA, 0.0); // Click tem só comédia e drama
        perfil.setPeso(Genero.DRAMA, 0.0);   // ambos com peso 0, será removido

        // Act
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        // Assert
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F08")));
    }

    @Test
    @DisplayName("deve retornar lista vazia quando catálogo é nulo")
    void deveRetornarListaVaziaQuandoCatalogoNulo() {
        // Act
        List<Filme> resultado = filtro.filtrar(null, perfil);

        // Assert
        assertTrue(resultado.isEmpty());
        assertNotNull(resultado); // não deve retornar null
    }

    @Test
    @DisplayName("deve retornar lista vazia quando catálogo é vazio")
    void deveRetornarListaVaziaQuandoCatalogoVazio() {
        // Act
        List<Filme> resultado = filtro.filtrar(new ArrayList<>(), perfil);

        // Assert
        assertTrue(resultado.isEmpty());
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("deve permitir filme com pelo menos um gênero com peso > 0")
    void devePermitirFilmeComPeloMenosUmGeneroComPeso() {
        // Arrange
        perfil.setPeso(Genero.DRAMA, 0.8); // Drama tem peso
        perfil.setPeso(Genero.COMEDIA, 0.0); // Comédia tem peso 0
        // Click tem Comédia e Drama, deve passar pois Drama tem peso

        // Act
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        // Assert
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F08")));
    }

    @Test
    @DisplayName("deve manter filmes válidos após filtro")
    void deveMantarFilmesValidos() {
        // Arrange
        perfil.setPeso(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPeso(Genero.DRAMA, 0.6);

        // Act
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        // Assert
        // F01, F02, F04, F06 devem estar presentes
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F01")));
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F02")));
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F04")));
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F06")));
    }

    @Test
    @DisplayName("deve aplicar todas as regras de filtro simultaneamente")
    void deveAplicarTodasAsRegrasSimultaneamente() {
        // Arrange
        perfil.marcarComoAssistido("F05");
        perfil.setPeso(Genero.TERROR, 0.0);
        perfil.setPeso(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPeso(Genero.DRAMA, 0.6);

        // Act
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        // Assert
        int esperado = 5; // F01, F02, F04, F06, F08
        assertEquals(esperado, resultado.size());
    }

    @Test
    @DisplayName("não deve modificar a lista de entrada")
    void naoDeveModificarListaDeEntrada() {
        // Arrange
        int tamanhoOriginal = catalogo.size();

        // Act
        filtro.filtrar(catalogo, perfil);

        // Assert
        assertEquals(tamanhoOriginal, catalogo.size());
    }
}
