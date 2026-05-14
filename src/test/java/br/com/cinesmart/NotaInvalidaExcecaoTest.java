package br.com.cinesmart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cinesmart.excecao.NotaInvalidaExcecao;

@DisplayName("Teste: NotaInvalidaExcecao")
class NotaInvalidaExcecaoTest {

    private static final String MENSAGEM = "Nota inválida";

    @Test
    @DisplayName("deve preservar mensagem no construtor simples")
    void devePreservarMensagemNoConstrutorSimples() {
        NotaInvalidaExcecao excecao = new NotaInvalidaExcecao(MENSAGEM);

        assertEquals(MENSAGEM, excecao.getMessage());
    }

    @Test
    @DisplayName("deve preservar mensagem e causa no construtor com causa")
    void devePreservarMensagemECausa() {
        Throwable causa = new IllegalArgumentException("Causa original");
        NotaInvalidaExcecao excecao = new NotaInvalidaExcecao(MENSAGEM, causa);

        assertEquals(MENSAGEM, excecao.getMessage());
        assertSame(causa, excecao.getCause());
    }
}