package br.com.cinesmart;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cinesmart.modelo.ClassificacaoEtaria;
import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.Idioma;
import br.com.cinesmart.modelo.Recomendacao;

@DisplayName("Teste: Recomendacao")
class RecomendacaoTest {

    private static final String TEXTO_TESTE = "Teste";

    private Filme criarFilme(String id) {
        return new Filme(id, "Duna", 2024, 166,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92);
    }

    @Test
    @DisplayName("deve criar recomendação com score válido")
    void deveCriarRecomendacaoComScoreValido() {
        Recomendacao recomendacao = new Recomendacao(criarFilme("F001"), 87.5, "Boa opção");

        assertAll(
                () -> assertEquals("F001", recomendacao.getFilme().getId()),
                () -> assertEquals(87.5, recomendacao.getScore()),
                () -> assertEquals("Boa opção", recomendacao.getJustificativa())
        );
    }

    @Test
    @DisplayName("deve limitar score abaixo de zero para zero")
    void deveLimitarScoreMinimo() {
        Recomendacao recomendacao = new Recomendacao(criarFilme("F002"), -20.0, TEXTO_TESTE);

        assertEquals(0.0, recomendacao.getScore());
    }

    @Test
    @DisplayName("deve limitar score acima de 100 para 100")
    void deveLimitarScoreMaximo() {
        Recomendacao recomendacao = new Recomendacao(criarFilme("F003"), 250.0, TEXTO_TESTE);

        assertEquals(100.0, recomendacao.getScore());
    }

    @Test
    @DisplayName("deve considerar recomendações iguais quando o filme é o mesmo")
    void deveConsiderarIguaisPorFilme() {
        Recomendacao recomendacao1 = new Recomendacao(criarFilme("F004"), 80.0, "A");
        Recomendacao recomendacao2 = new Recomendacao(criarFilme("F004"), 20.0, "B");

        assertAll(
                () -> assertEquals(recomendacao1, recomendacao2),
                () -> assertEquals(recomendacao1.hashCode(), recomendacao2.hashCode()),
                () -> assertNotEquals(recomendacao1, new Recomendacao(criarFilme("F005"), 80.0, "A"))
        );
    }

    @Test
    @DisplayName("toString deve conter título e score formatado")
    void toStringDeveConterTituloEScore() {
        Recomendacao recomendacao = new Recomendacao(criarFilme("F006"), 91.234, "Texto");

        String texto = recomendacao.toString();

        assertAll(
                () -> assertTrue(texto.contains("Duna")),
                () -> assertTrue(texto.matches(".*91[.,]23.*"))
        );
    }

    @Test
    @DisplayName("não deve aceitar filme nulo")
    void naoDeveAceitarFilmeNulo() {
        assertThrows(NullPointerException.class, () -> new Recomendacao(null, 50.0, TEXTO_TESTE));
    }

    @Test
    @DisplayName("não deve aceitar justificativa nula")
    void naoDeveAceitarJustificativaNula() {
        assertThrows(NullPointerException.class, () -> new Recomendacao(criarFilme("F007"), 50.0, null));
    }
}