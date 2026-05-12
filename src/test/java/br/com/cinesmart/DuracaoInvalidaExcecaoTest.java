package br.com.cinesmart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cinesmart.excecao.DuracaoInvalidaExcecao;

@DisplayName("Teste: DuracaoInvalidaExcecao")
class DuracaoInvalidaExcecaoTest {

    private static final String MENSAGEM = "Duração inválida";

    @Test
    @DisplayName("deve preservar mensagem no construtor simples")
    void devePreservarMensagemNoConstrutorSimples() {
        DuracaoInvalidaExcecao excecao = new DuracaoInvalidaExcecao(MENSAGEM);

        assertEquals(MENSAGEM, excecao.getMessage());
    }

    @Test
    @DisplayName("deve preservar mensagem e causa no construtor com causa")
    void devePreservarMensagemECausa() {
        Throwable causa = new IllegalStateException("Causa original");
        DuracaoInvalidaExcecao excecao = new DuracaoInvalidaExcecao(MENSAGEM, causa);

        assertEquals(MENSAGEM, excecao.getMessage());
        assertSame(causa, excecao.getCause());
    }
}