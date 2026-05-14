package br.com.cinesmart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cinesmart.excecao.PerfilIncompletoExcecao;

@DisplayName("Teste: PerfilIncompletoExcecao")
class PerfilIncompletoExcecaoTest {

    private static final String MENSAGEM = "Perfil incompleto";

    @Test
    @DisplayName("deve preservar mensagem no construtor simples")
    void devePreservarMensagemNoConstrutorSimples() {
        PerfilIncompletoExcecao excecao = new PerfilIncompletoExcecao(MENSAGEM);

        assertEquals(MENSAGEM, excecao.getMessage());
    }

    @Test
    @DisplayName("deve preservar mensagem e causa no construtor com causa")
    void devePreservarMensagemECausa() {
        IllegalArgumentException causa = new IllegalArgumentException("Causa original");
        PerfilIncompletoExcecao excecao = new PerfilIncompletoExcecao(MENSAGEM, causa);

        assertEquals(MENSAGEM, excecao.getMessage());
        assertSame(causa, excecao.getCause());
    }
}