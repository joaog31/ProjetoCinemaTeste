package br.com.cinesmart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cinesmart.excecao.PesoInvalidoExcecao;

@DisplayName("Teste: PesoInvalidoExcecao")
class PesoInvalidoExcecaoTest {

    private static final String MENSAGEM = "Peso inválido";

    @Test
    @DisplayName("deve preservar mensagem no construtor simples")
    void devePreservarMensagemNoConstrutorSimples() {
        PesoInvalidoExcecao excecao = new PesoInvalidoExcecao(MENSAGEM);

        assertEquals(MENSAGEM, excecao.getMessage());
    }

    @Test
    @DisplayName("deve preservar mensagem e causa no construtor com causa")
    void devePreservarMensagemECausa() {
        Throwable causa = new IllegalArgumentException("Causa original");
        PesoInvalidoExcecao excecao = new PesoInvalidoExcecao(MENSAGEM, causa);

        assertEquals(MENSAGEM, excecao.getMessage());
        assertSame(causa, excecao.getCause());
    }
}