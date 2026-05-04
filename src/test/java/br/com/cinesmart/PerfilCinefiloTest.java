package br.com.cinesmart;

import br.com.cinesmart.excecao.DuracaoInvalidaExcecao;
import br.com.cinesmart.excecao.NotaInvalidaExcecao;
import br.com.cinesmart.excecao.PesoInvalidoExcecao;
import br.com.cinesmart.modelo.ClassificacaoEtaria;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.Idioma;
import br.com.cinesmart.modelo.PerfilCinefilo;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Testes unitários para a classe PerfilCinefilo.
 */
@DisplayName("Teste: PerfilCinefilo")
class PerfilCinefiloTest {

    private PerfilCinefilo perfil;

    @BeforeEach
    void setUp() {
        perfil = new PerfilCinefilo(90, 150, ClassificacaoEtaria.DEZESSEIS,
                Set.of(Idioma.PORTUGUES, Idioma.INGLES));
    }

    @Test
    @DisplayName("deve criar um perfil com parâmetros válidos")
    void deveCriarPerfilComParametrosValidos() {
        // Act & Assert
        assertNotNull(perfil);
        assertEquals(90, perfil.getDuracaoMinima());
        assertEquals(150, perfil.getDuracaoMaxima());
        assertEquals(ClassificacaoEtaria.DEZESSEIS, perfil.getClassificacaoMaxima());
        assertEquals(2, perfil.getIdiomasAceitos().size());
    }

    @Test
    @DisplayName("deve lançar DuracaoInvalidaExcecao quando duração mínima > máxima")
    void deveLancarExcecaoQuandoDuracaoMinimaMaiorQueMaxima() {
        // Arrange & Act & Assert
        assertThrows(DuracaoInvalidaExcecao.class, () ->
            new PerfilCinefilo(200, 100, ClassificacaoEtaria.DEZESSEIS,
                    Set.of(Idioma.PORTUGUES))
        );
    }

    @Test
    @DisplayName("deve lançar DuracaoInvalidaExcecao quando duração é negativa")
    void deveLancarExcecaoQuandoDuracaoNegativa() {
        // Arrange & Act & Assert
        assertThrows(DuracaoInvalidaExcecao.class, () ->
            new PerfilCinefilo(-10, 100, ClassificacaoEtaria.DEZESSEIS,
                    Set.of(Idioma.PORTUGUES))
        );
    }

    @Test
    @DisplayName("deve aceitar peso válido entre 0.0 e 1.0")
    void deveAceitarPesoValido() {
        // Arrange & Act
        perfil.setPeso(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPeso(Genero.COMEDIA, 0.0);
        perfil.setPeso(Genero.DRAMA, 0.5);

        // Assert
        assertEquals(0.9, perfil.getPeso(Genero.FICCAO_CIENTIFICA));
        assertEquals(0.0, perfil.getPeso(Genero.COMEDIA));
        assertEquals(0.5, perfil.getPeso(Genero.DRAMA));
    }

    @ParameterizedTest
    @DisplayName("deve lançar PesoInvalidoExcecao quando peso < 0 ou > 1")
    @CsvSource({
        "-0.1",
        "1.1",
        "-1.0",
        "2.0"
    })
    void deveLancarExcecaoQuandoPesoForaDoIntervalo(double pesoInvalido) {
        // Arrange & Act & Assert
        assertThrows(PesoInvalidoExcecao.class, () ->
            perfil.setPeso(Genero.ACAO, pesoInvalido)
        );
    }

    @Test
    @DisplayName("deve marcar filme como assistido e consultar histórico")
    void deveMarcarFilmeComoAssistido() {
        // Arrange & Act
        perfil.marcarComoAssistido("F001");
        perfil.marcarComoAssistido("F002");

        // Assert
        assertTrue(perfil.jaAssistiu("F001"));
        assertTrue(perfil.jaAssistiu("F002"));
        assertFalse(perfil.jaAssistiu("F003"));
        assertEquals(2, perfil.getFilmesAssistidos().size());
    }

    @Test
    @DisplayName("deve aceitar nota entre 1 e 5")
    void deveAceitarNotaValida() {
        // Arrange & Act
        perfil.adicionarNota("F001", 5);
        perfil.adicionarNota("F002", 3);
        perfil.adicionarNota("F003", 1);

        // Assert
        assertEquals(5, perfil.getNotaPara("F001"));
        assertEquals(3, perfil.getNotaPara("F002"));
        assertEquals(1, perfil.getNotaPara("F003"));
    }

    @ParameterizedTest
    @DisplayName("deve lançar NotaInvalidaExcecao quando nota < 1 ou > 5")
    @CsvSource({
        "0",
        "6",
        "-1",
        "10"
    })
    void deveLancarExcecaoQuandoNotaForaDoIntervalo(int notaInvalida) {
        // Arrange & Act & Assert
        assertThrows(NotaInvalidaExcecao.class, () ->
            perfil.adicionarNota("F001", notaInvalida)
        );
    }

    @Test
    @DisplayName("deve retornar null para nota de filme não avaliado")
    void deveRetornarNullParaFilmeNaoAvaliado() {
        // Act & Assert
        assertNull(perfil.getNotaPara("F999"));
    }

    @Test
    @DisplayName("deve retornar cópia imutável do mapa de notas")
    void deveRetornarCopiaImutavelDeNotas() {
        // Arrange
        perfil.adicionarNota("F001", 5);

        // Act
        var notas = perfil.getNotas();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> notas.put("F002", 3));
        assertEquals(1, perfil.getNotas().size());
    }

    @Test
    @DisplayName("deve retornar cópia imutável do conjunto de filmes assistidos")
    void deveRetornarCopiaImutavelDeFilmesAssistidos() {
        // Arrange
        perfil.marcarComoAssistido("F001");

        // Act
        var assistidos = perfil.getFilmesAssistidos();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> assistidos.add("F002"));
        assertEquals(1, perfil.getFilmesAssistidos().size());
    }

    @Test
    @DisplayName("deve retornar cópia imutável dos idiomas aceitos")
    void deveRetornarCopiaImutavelDeIdiomas() {
        // Act
        var idiomas = perfil.getIdiomasAceitos();
        idiomas.add(Idioma.FRANCES); // tenta modificar a cópia

        // Assert
        assertFalse(perfil.getIdiomasAceitos().contains(Idioma.FRANCES)); // original não foi modificado
        assertEquals(2, perfil.getIdiomasAceitos().size());
    }

    @Test
    @DisplayName("deve inicializar todos os gêneros com peso 0.0")
    void deveInicializarTodosGenerosComPesoZero() {
        // Act
        var pesos = perfil.getPesosPorGenero();

        // Assert
        assertEquals(Genero.values().length, pesos.size());
        for (Genero genero : Genero.values()) {
            assertEquals(0.0, pesos.get(genero));
        }
    }
}
