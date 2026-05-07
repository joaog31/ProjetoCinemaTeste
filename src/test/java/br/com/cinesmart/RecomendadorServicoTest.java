package br.com.cinesmart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.cinesmart.modelo.ClassificacaoEtaria;
import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.Idioma;
import br.com.cinesmart.modelo.PerfilCinefilo;
import br.com.cinesmart.modelo.Recomendacao;
import br.com.cinesmart.modelo.Usuario;
import br.com.cinesmart.servico.CalculadoraScore;
import br.com.cinesmart.servico.CatalogoFilmesAPI;
import br.com.cinesmart.servico.FiltroFilmes;
import br.com.cinesmart.servico.HistoricoUsuarioRepositorio;
import br.com.cinesmart.servico.NotificadorPush;
import br.com.cinesmart.servico.RecomendadorServico;
import br.com.cinesmart.utilitario.GeradorAleatorio;


@ExtendWith(MockitoExtension.class)
@DisplayName("Teste: RecomendadorServico")
class RecomendadorServicoTest {

    @Mock private CatalogoFilmesAPI catalogo;
    @Mock private HistoricoUsuarioRepositorio historico;
    @Mock private NotificadorPush notificador;
    @Mock private GeradorAleatorio gerador;
    @Captor private ArgumentCaptor<List<Recomendacao>> captorRecomendacoes;
    @Spy private final CalculadoraScore calculadora = new CalculadoraScore();
    @InjectMocks private RecomendadorServico servico;
    private Usuario usuario;
    private List<Filme> catalogoMock;

    private static class CalculadoraScoreFixa extends CalculadoraScore {
        @Override
        public double calcular(Filme filme, PerfilCinefilo perfil) {
            return 85.0;
        }
    }

    @BeforeEach
    void setUp() {
        
        PerfilCinefilo perfil = new PerfilCinefilo(90, 150, ClassificacaoEtaria.DEZESSEIS,
                Set.of(Idioma.PORTUGUES, Idioma.INGLES));
        perfil.setPeso(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPeso(Genero.DRAMA, 0.6);
        perfil.setPeso(Genero.COMEDIA, 0.5);
        perfil.setPeso(Genero.ROMANCE, 0.4);
        perfil.setPeso(Genero.TERROR, 0.0);

        usuario = new Usuario("U001", "Maria", 28, perfil, false);

        
        catalogoMock = new ArrayList<>();
        catalogoMock.add(new Filme("F01", "Duna: Parte Dois", 2024, 166,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92));
        catalogoMock.add(new Filme("F02", "Ela (Her)", 2013, 126,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA, Genero.ROMANCE),
                ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 78));
        catalogoMock.add(new Filme("F03", "O Iluminado", 1980, 146,
                Set.of(Genero.TERROR),
                ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 88));
        catalogoMock.add(new Filme("F04", "Interestelar", 2014, 169,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 95));
        catalogoMock.add(new Filme("F05", "Tropa de Elite", 2007, 115,
                Set.of(Genero.ACAO, Genero.DRAMA),
                ClassificacaoEtaria.DEZOITO, Idioma.PORTUGUES, 80));
        catalogoMock.add(new Filme("F06", "Click", 2006, 107,
                Set.of(Genero.COMEDIA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 65));
        catalogoMock.add(new Filme("F07", "A Chegada", 2016, 116,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 84));
    }

    @Test
    @DisplayName("deve retornar recomendações ordenadas por score decrescente")
    void deveRetornarRecomendacoesOrdenadas() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        
        List<Recomendacao> resultado = servico.recomendar(usuario, 5);

        
        assertEquals(5, resultado.size());
        assertTrue(resultado.get(0).getScore() >= resultado.get(1).getScore());
        assertTrue(resultado.get(1).getScore() >= resultado.get(2).getScore());
    }

    @Test
    @DisplayName("deve respeitar limite de topN recomendações")
    void deveRespeitarLimiteTopN() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        
        List<Recomendacao> resultado = servico.recomendar(usuario, 2);

        
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("deve retornar lista vazia quando catálogo está vazio")
    void deveRetornarListaVaziaQuandoCatalogoVazio() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(new ArrayList<>());

        
        List<Recomendacao> resultado = servico.recomendar(usuario, 5);

        
        assertTrue(resultado.isEmpty());
        assertNotNull(resultado); 
    }

    @Test
    @DisplayName("deve registrar recomendações no histórico após gerar")
    void deveRegistrarRecomendacoesNoHistorico() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        
        servico.recomendar(usuario, 3);

        
        verify(historico, times(1)).registrarRecomendacao(eq(usuario), anyList());
    }

    @Test
    @DisplayName("deve chamar notificador se notificação está habilitada")
    void deveChamarNotificadorQuandoHabilitado() throws Exception {
        
        Usuario usuarioComNotificacao = new Usuario("U002", "João", 30,
                usuario.getPerfil(), true); 
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        
        servico.recomendar(usuarioComNotificacao, 3);

        
        verify(notificador, times(1)).enviar(eq(usuarioComNotificacao), anyList());
    }

    @Test
    @DisplayName("não deve chamar notificador se notificação está desabilitada")
    void naoDeveChamarNotificadorQuandoDesabilitado() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        
        servico.recomendar(usuario, 3); 

        
        verify(notificador, never()).enviar(any(), anyList());
    }

    @Test
    @DisplayName("deve retornar lista vazia se API lança exceção")
    void deveRetornarListaVaziaQuandoAPILancaExcecao() throws Exception {
        
        when(catalogo.buscarTodos()).thenThrow(new IOException("API offline"));

        
        List<Recomendacao> resultado = servico.recomendar(usuario, 5);

        
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve ser resiliente a falha de notificador")
    void deveSerResilienteAFalhaDeNotificador() throws Exception {
        
        Usuario usuarioComNotificacao = new Usuario("U003", "Ana", 25,
                usuario.getPerfil(), true);
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);
        doThrow(new RuntimeException("Firebase offline")).when(notificador).enviar(any(), anyList());

        
        assertDoesNotThrow(() -> servico.recomendar(usuarioComNotificacao, 3));
    }

    @Test
    @DisplayName("deve usar ArgumentCaptor para inspecionar recomendações registradas")
    void deveInspecionarRecomendacoesRegistradas() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        
        servico.recomendar(usuario, 3);

        
        verify(historico).registrarRecomendacao(eq(usuario), captorRecomendacoes.capture());
        List<Recomendacao> registradas = captorRecomendacoes.getValue();
        
        assertAll(
            () -> assertFalse(registradas.isEmpty()),
            () -> assertTrue(registradas.get(0).getScore() >= registradas.get(registradas.size() - 1).getScore())
        );
    }

    @Test
    @DisplayName("recomendação deve conter justificativa textual")
    void recomendacaoDeveTerJustificativa() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        
        List<Recomendacao> resultado = servico.recomendar(usuario, 1);

        
        assertFalse(resultado.isEmpty());
        assertNotNull(resultado.get(0).getJustificativa());
        assertFalse(resultado.get(0).getJustificativa().isEmpty());
    }

    @Test
    @DisplayName("modo aleatório deve retornar um filme do conjunto filtrado")
    void modoAleatorioDevolveFilmeValidoDe() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);
        when(gerador.sortearInteiro(anyInt(), anyInt())).thenReturn(1, 0);

        
        List<Recomendacao> primeiro = servico.recomendarAleatorio(usuario);
        List<Recomendacao> segundo = servico.recomendarAleatorio(usuario);

        
        assertEquals(1, primeiro.size());
        assertEquals(1, segundo.size());
        assertEquals("F02", primeiro.get(0).getFilme().getId());
        assertEquals("F01", segundo.get(0).getFilme().getId());
        verify(gerador, times(2)).sortearInteiro(anyInt(), anyInt());
    }

    @Test
    @DisplayName("não deve retornar null para coleções")
    void naoDeveRetornarNullParaColecoes() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(new ArrayList<>());

        
        List<Recomendacao> resultado = servico.recomendar(usuario, 5);

        
        assertNotNull(resultado); 
    }

    @Test
    @DisplayName("deve calcular scores para filmes válidos")
    void deveCalcularScoresParaFilmesValidos() throws Exception {
        
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        
        List<Recomendacao> resultado = servico.recomendar(usuario, 5);

        
        verify(catalogo, atLeastOnce()).buscarTodos();
        verify(calculadora, atLeastOnce()).calcular(any(Filme.class), any(PerfilCinefilo.class));
        for (Recomendacao rec : resultado) {
            assertTrue(rec.getScore() >= 0.0 && rec.getScore() <= 100.0);
        }
    }

    @Test
    @DisplayName("deve embaralhar apenas filmes empatados por score e popularidade")
    void deveEmbaralharApenasEmpates() throws Exception {
        
        PerfilCinefilo perfilEmpate = new PerfilCinefilo(90, 150, ClassificacaoEtaria.DEZESSEIS,
                Set.of(Idioma.INGLES));
        perfilEmpate.setPeso(Genero.DRAMA, 1.0);
        Usuario usuarioEmpate = new Usuario("U004", "Teste", 30, perfilEmpate, false);

        List<Filme> catalogoEmpatado = List.of(
                new Filme("E01", "Filme A", 2024, 120,
                        Set.of(Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.INGLES, 80),
                new Filme("E02", "Filme B", 2023, 120,
                        Set.of(Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.INGLES, 80)
        );

        when(catalogo.buscarTodos()).thenReturn(catalogoEmpatado);
        when(gerador.sortearInteiro(0, 2)).thenReturn(0);

        
        List<Recomendacao> resultado = servico.recomendar(usuarioEmpate, 2);

        
        assertEquals(2, resultado.size());
        assertEquals("E02", resultado.get(0).getFilme().getId());
        assertEquals("E01", resultado.get(1).getFilme().getId());
        verify(gerador, times(1)).sortearInteiro(0, 2);
    }

    @Test
    @DisplayName("empates de score devem ser desempatados por popularidade")
    void deveDesempatarPorPopularidadeQuandoScoreEmpata() throws Exception {
        
        CalculadoraScore calculadoraEmpate = new CalculadoraScoreFixa();
        RecomendadorServico servicoLocal = new RecomendadorServico(catalogo, historico, notificador, gerador, calculadoraEmpate, new FiltroFilmes());

        List<Filme> catalogoEmpatePopularidade = List.of(
                new Filme("P01", "Filme A", 2024, 120,
                        Set.of(Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.INGLES, 70),
                new Filme("P02", "Filme B", 2024, 120,
                        Set.of(Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.INGLES, 90)
        );

        when(catalogo.buscarTodos()).thenReturn(catalogoEmpatePopularidade);

        
        List<Recomendacao> resultado = servicoLocal.recomendar(usuario, 2);

        
        assertEquals(2, resultado.size());
        assertEquals("P02", resultado.get(0).getFilme().getId());
        assertEquals("P01", resultado.get(1).getFilme().getId());
    }
}
