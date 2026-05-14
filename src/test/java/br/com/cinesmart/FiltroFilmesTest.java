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

        
        catalogo = new ArrayList<>();
        
        
        catalogo.add(new Filme("F01", "Duna", 2021, 166,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92));

        
        catalogo.add(new Filme("F02", "Her", 2013, 126,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA, Genero.ROMANCE),
                ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 78));

        
        catalogo.add(new Filme("F03", "O Iluminado", 1980, 146,
                Set.of(Genero.TERROR),
                ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 88));

        
        catalogo.add(new Filme("F04", "A Origem", 2010, 148,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 85));

        
        catalogo.add(new Filme("F05", "Matrix", 1999, 136,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.ACAO),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 95));

        
        catalogo.add(new Filme("F06", "Interestelar", 2014, 169,
                Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.PORTUGUES, 90));

        
        catalogo.add(new Filme("F07", "Tropa de Elite", 2007, 115,
                Set.of(Genero.ACAO, Genero.DRAMA),
                ClassificacaoEtaria.DEZOITO, Idioma.PORTUGUES, 80));

        
        catalogo.add(new Filme("F08", "Click", 2006, 107,
                Set.of(Genero.COMEDIA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 65));

        
        catalogo.add(new Filme("F09", "Stranger Things", 2016, 50,
                Set.of(Genero.TERROR),
                ClassificacaoEtaria.DEZESSEIS, Idioma.PORTUGUES, 92));
    }

    @Test
    @DisplayName("deve remover filmes já assistidos")
    void deveRemoverFilmesJaAssistidos() {
        
        perfil.marcarComoAssistido("F05");

        
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F05")));
    }

    @Test
    @DisplayName("deve remover filmes acima da classificação máxima")
    void deveRemoverFilmesAcimaClassificacao() {
        
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        
        
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F03")));
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F07")));
    }

    @Test
    @DisplayName("deve remover filmes em idioma não aceito")
    void deveRemoverFilmesIdiomaInvalido() {
        
        PerfilCinefilo perfilSoIngles = new PerfilCinefilo(90, 150,
                ClassificacaoEtaria.DEZESSEIS, Set.of(Idioma.INGLES)); 

        
        List<Filme> resultado = filtro.filtrar(catalogo, perfilSoIngles);

        
        
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F06")));
        
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F09")));
    }

    @Test
    @DisplayName("deve remover filmes com todos os gêneros de peso 0.0")
    void deveRemoverFilmesComTodosGenerosComPeso0() {
        
        perfil.setPeso(Genero.COMEDIA, 0.0); 
        perfil.setPeso(Genero.DRAMA, 0.0);   

        
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        
        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F08")));
    }

    @Test
    @DisplayName("deve retornar lista vazia quando catálogo é nulo")
    void deveRetornarListaVaziaQuandoCatalogoNulo() {
        
        List<Filme> resultado = filtro.filtrar(null, perfil);

        
        assertTrue(resultado.isEmpty());
        assertNotNull(resultado); 
    }

    @Test
    @DisplayName("deve retornar lista vazia quando catálogo é vazio")
    void deveRetornarListaVaziaQuandoCatalogoVazio() {
        
        List<Filme> resultado = filtro.filtrar(new ArrayList<>(), perfil);

        
        assertTrue(resultado.isEmpty());
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("deve permitir filme com pelo menos um gênero com peso > 0")
    void devePermitirFilmeComPeloMenosUmGeneroComPeso() {
        
        perfil.setPeso(Genero.DRAMA, 0.8); 
        perfil.setPeso(Genero.COMEDIA, 0.0); 
        

        
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F08")));
    }

    @Test
    @DisplayName("deve manter filmes válidos após filtro")
    void deveMantarFilmesValidos() {
        
        perfil.setPeso(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPeso(Genero.DRAMA, 0.6);

        
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        
        
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F01")));
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F02")));
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F04")));
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("F06")));
    }

    @Test
    @DisplayName("deve aplicar todas as regras de filtro simultaneamente")
    void deveAplicarTodasAsRegrasSimultaneamente() {
        
        perfil.marcarComoAssistido("F05");
        perfil.setPeso(Genero.TERROR, 0.0);
        perfil.setPeso(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPeso(Genero.DRAMA, 0.6);

        
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        
        int esperado = 5; 
        assertEquals(esperado, resultado.size());
    }

    @Test
    @DisplayName("não deve modificar a lista de entrada")
    void naoDeveModificarListaDeEntrada() {
        
        int tamanhoOriginal = catalogo.size();

        
        filtro.filtrar(catalogo, perfil);

        
        assertEquals(tamanhoOriginal, catalogo.size());
    }
}
