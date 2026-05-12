package br.com.cinesmart;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import br.com.cinesmart.modelo.ClassificacaoEtaria;
import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.Idioma;
import br.com.cinesmart.modelo.PerfilCinefilo;
import br.com.cinesmart.servico.CalculadoraScore;


@DisplayName("Teste: CalculadoraScore")
class CalculadoraScoreTest {

    private CalculadoraScore calculadora;
    private PerfilCinefilo perfil;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraScore();
        perfil = new PerfilCinefilo(90, 150, ClassificacaoEtaria.DEZESSEIS,
                Set.of(Idioma.PORTUGUES, Idioma.INGLES));
    }

    @Test
    @DisplayName("deve calcular score máximo para filme com todos os gêneros amados")
    void deveCalcularScoreMaximoParaFilmeComTodosGenerosAmados() {
        
        perfil.setPeso(Genero.FICCAO_CIENTIFICA, 1.0);
        Filme filme = new Filme("F001", "Duna", 2021, 120, Set.of(Genero.FICCAO_CIENTIFICA),
                ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92);

        
        double score = calculadora.calcular(filme, perfil);

        
        assertTrue(score > 80.0); 
        assertTrue(score <= 100.0); 
    }

    @Test
    @DisplayName("deve retornar score baixo para filme com gênero não preferido")
    void deveRetornarScoreBaixoParaGeneroNaoPreferido() {
        
        perfil.setPeso(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPeso(Genero.TERROR, 0.0);
        
        Filme filme = new Filme("F002", "O Iluminado", 1980, 30, Set.of(Genero.TERROR),
            ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 5);

        
        double score = calculadora.calcular(filme, perfil);

        
        assertTrue(score < 20.0); 
    }

    @Test
    @DisplayName("deve retornar score máximo para duração dentro da faixa preferida")
    void deveRetornarScoreMaximoParaDuracaoDentroFaixa() {
        
        perfil.setPeso(Genero.DRAMA, 0.8);
        Filme filme = new Filme("F003", "A Origem", 2010, 120, Set.of(Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 85);

        
        double score = calculadora.calcular(filme, perfil);

        
        assertTrue(score >= 60.0); 
        assertTrue(score <= 100.0);
    }

    @Test
    @DisplayName("deve reduzir score para duração acima da máxima preferida")
    void deveReduzirScoreParaDuracaoAcima() {
        
        perfil.setPeso(Genero.DRAMA, 0.8);
        Filme filmeNormal = new Filme("F004a", "Filme Normal", 2020, 130,
                Set.of(Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.INGLES, 80);
        Filme filmeGrande = new Filme("F004b", "Filme Grande", 2020, 200,
                Set.of(Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.INGLES, 80);

        
        double scoreNormal = calculadora.calcular(filmeNormal, perfil);
        double scoreGrande = calculadora.calcular(filmeGrande, perfil);

        
        assertTrue(scoreNormal > scoreGrande); 
    }

    @ParameterizedTest
    @DisplayName("score deve sempre estar entre 0 e 100")
    @CsvSource({
        "0, 0, 0",
        "1.0, 1.0, 1.0",
        "0.5, 0.5, 0.5"
    })
    void scoreDeveEstadoEntre0E100(double p1, double p2, double p3) {
        
        perfil.setPeso(Genero.ACAO, p1);
        perfil.setPeso(Genero.COMEDIA, p2);
        perfil.setPeso(Genero.DRAMA, p3);
        Filme filme = new Filme("F005", "Filme Teste", 2020, 100,
                Set.of(Genero.ACAO, Genero.COMEDIA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 50);

        
        double score = calculadora.calcular(filme, perfil);

        
        assertTrue(score >= 0.0 && score <= 100.0);
    }

    @Test
    @DisplayName("deve considerar popularidade na fórmula de score")
    void deveConsiderarPopularidadeNaFormula() {
        
        perfil.setPeso(Genero.DRAMA, 0.5);
        Filme filmeMenosPopular = new Filme("F006a", "Filme Menos Popular", 2020, 120,
                Set.of(Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.INGLES, 30);
        Filme filmeMaisPopular = new Filme("F006b", "Filme Mais Popular", 2020, 120,
                Set.of(Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.INGLES, 90);

        
        double scoreMenos = calculadora.calcular(filmeMenosPopular, perfil);
        double scoreMais = calculadora.calcular(filmeMaisPopular, perfil);

        
        assertTrue(scoreMais > scoreMenos); 
    }

    @Test
    @DisplayName("deve limitar score quando não há aderência de gênero mesmo com duração e popularidade altas")
    void deveLimitarScoreSemAderenciaDeGenero() {
        
        perfil.setPeso(Genero.ACAO, 0.0);
        Filme filme = new Filme("F006c", "Blockbuster sem Aderencia", 2024, 120,
                Set.of(Genero.ACAO), ClassificacaoEtaria.DOZE, Idioma.INGLES, 95);

        
        double score = calculadora.calcular(filme, perfil);

        
        assertTrue(score <= 30.0);
    }

    @Test
    @DisplayName("deve considerar bônus de afinidade baseado em notas históricas")
    void deveConsiderarBonusDeAfinidade() {
        
        perfil.setPeso(Genero.DRAMA, 0.6);
        perfil.adicionarNota("F_OLD", 5); 
        
        Filme filmeNovo = new Filme("F007", "Filme Novo Drama", 2020, 120,
                Set.of(Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.INGLES, 70);

        
        double score = calculadora.calcular(filmeNovo, perfil);

        
        assertTrue(score >= 40.0); 
    }

    @Test
    @DisplayName("filme sem gêneros deve ter score baixo")
    void filmeSemGeneroDeveTerScoreBaixo() {
        
        
        Filme filmeSemGenero = new Filme("F008", "Filme Vazio", 2020, 20,
            Set.of(), ClassificacaoEtaria.DOZE, Idioma.INGLES, 0);

        
        double score = calculadora.calcular(filmeSemGenero, perfil);

        
        assertTrue(score < 20.0); 
    }

    @Test
    @DisplayName("deve ser determinístico: mesmo filme e perfil retornam mesmo score")
    void deveSerDeterministico() {
        
        perfil.setPeso(Genero.ACAO, 0.7);
        Filme filme = new Filme("F009", "Filme Deterministico", 2020, 120,
            Set.of(Genero.ACAO), ClassificacaoEtaria.DOZE, Idioma.INGLES, 80);
        double score1 = calculadora.calcular(filme, perfil);
        double score2 = calculadora.calcular(filme, perfil);
        double score3 = calculadora.calcular(filme, perfil);

        
        assertEquals(score1, score2);
        assertEquals(score2, score3);
    }
}
