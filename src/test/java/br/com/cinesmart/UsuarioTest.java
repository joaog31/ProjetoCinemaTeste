package br.com.cinesmart;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cinesmart.modelo.ClassificacaoEtaria;
import br.com.cinesmart.modelo.Idioma;
import br.com.cinesmart.modelo.PerfilCinefilo;
import br.com.cinesmart.modelo.Usuario;

@DisplayName("Teste: Usuario")
class UsuarioTest {

    private PerfilCinefilo criarPerfil() {
        return new PerfilCinefilo(90, 150, ClassificacaoEtaria.DEZESSEIS,
                Set.of(Idioma.PORTUGUES, Idioma.INGLES));
    }

    @Test
    @DisplayName("deve criar usuário com notificação desabilitada por padrão")
    void deveCriarUsuarioComNotificacaoPadraoFalse() {
        Usuario usuario = new Usuario("U001", "Maria", 28, criarPerfil());

        assertAll(
                () -> assertEquals("U001", usuario.getId()),
                () -> assertEquals("Maria", usuario.getNome()),
                () -> assertEquals(28, usuario.getIdade()),
                () -> assertFalse(usuario.isNotificacaoHabilitada()),
                () -> assertNotNull(usuario.getPerfil())
        );
    }

    @Test
    @DisplayName("deve criar usuário com notificação habilitada")
    void deveCriarUsuarioComNotificacaoHabilitada() {
        Usuario usuario = new Usuario("U002", "João", 30, criarPerfil(), true);

        assertAll(
                () -> assertEquals("U002", usuario.getId()),
                () -> assertEquals("João", usuario.getNome()),
                () -> assertEquals(30, usuario.getIdade()),
                () -> assertTrue(usuario.isNotificacaoHabilitada())
        );
    }

    @Test
    @DisplayName("deve considerar usuários iguais quando o ID é o mesmo")
    void deveConsiderarIguaisPorId() {
        Usuario usuario1 = new Usuario("U003", "Ana", 25, criarPerfil());
        Usuario usuario2 = new Usuario("U003", "Outro Nome", 40, criarPerfil(), true);

        assertAll(
                () -> assertEquals(usuario1, usuario2),
                () -> assertEquals(usuario1.hashCode(), usuario2.hashCode()),
                () -> assertNotEquals(usuario1, new Usuario("U004", "Ana", 25, criarPerfil()))
        );
    }

    @Test
    @DisplayName("toString deve conter dados principais")
    void toStringDeveConterDadosPrincipais() {
        Usuario usuario = new Usuario("U005", "Bruno", 32, criarPerfil());

        String texto = usuario.toString();

        assertAll(
                () -> assertTrue(texto.contains("U005")),
                () -> assertTrue(texto.contains("Bruno")),
                () -> assertTrue(texto.contains("32"))
        );
    }

    @Test
    @DisplayName("não deve aceitar id nulo")
    void naoDeveAceitarIdNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Usuario(null, "Maria", 28, criarPerfil());
        });
    }

    @Test
    @DisplayName("não deve aceitar perfil nulo")
    void naoDeveAceitarPerfilNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Usuario("U006", "Maria", 28, null);
        });
    }
}