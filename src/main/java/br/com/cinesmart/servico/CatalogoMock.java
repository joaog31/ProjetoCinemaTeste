package br.com.cinesmart.servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.cinesmart.modelo.ClassificacaoEtaria;
import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.Idioma;

/**
 * Implementação em memória de {@link CatalogoFilmesAPI} para uso local e testes.
 * Mantém um catálogo fixo com pelo menos 30 filmes para atender o cenário de exemplo.
 */
public final class CatalogoMock implements CatalogoFilmesAPI {
    private static final List<Filme> FILMES = List.of(
            new Filme("F001", "Duna: Parte Dois", 2024, 166,
                    Set.of(Genero.FICCAO_CIENTIFICA, Genero.AVENTURA),
                    ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 96),
            new Filme("F002", "Interestelar", 2014, 169,
                    Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                    ClassificacaoEtaria.DOZE, Idioma.INGLES, 97),
            new Filme("F003", "A Origem", 2010, 148,
                    Set.of(Genero.FICCAO_CIENTIFICA, Genero.ACAO),
                    ClassificacaoEtaria.DOZE, Idioma.INGLES, 95),
            new Filme("F004", "Matrix", 1999, 136,
                    Set.of(Genero.FICCAO_CIENTIFICA, Genero.ACAO),
                    ClassificacaoEtaria.DOZE, Idioma.INGLES, 98),
            new Filme("F005", "Her", 2013, 126,
                    Set.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA, Genero.ROMANCE),
                    ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 84),
            new Filme("F006", "O Poderoso Chefao", 1972, 175,
                    Set.of(Genero.DRAMA, Genero.POLICIAL),
                    ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 99),
            new Filme("F007", "Parasita", 2019, 132,
                    Set.of(Genero.DRAMA, Genero.SUSPENSE),
                    ClassificacaoEtaria.DEZESSEIS, Idioma.COREANO, 97),
            new Filme("F008", "Cidade de Deus", 2002, 130,
                    Set.of(Genero.DRAMA, Genero.POLICIAL),
                    ClassificacaoEtaria.DEZOITO, Idioma.PORTUGUES, 94),
            new Filme("F009", "Toy Story", 1995, 81,
                    Set.of(Genero.ANIMACAO, Genero.AVENTURA),
                    ClassificacaoEtaria.LIVRE, Idioma.INGLES, 93),
            new Filme("F010", "Divertida Mente", 2015, 95,
                    Set.of(Genero.ANIMACAO, Genero.COMEDIA),
                    ClassificacaoEtaria.LIVRE, Idioma.INGLES, 95),
            new Filme("F011", "Soul", 2020, 100,
                    Set.of(Genero.ANIMACAO, Genero.DRAMA),
                    ClassificacaoEtaria.LIVRE, Idioma.INGLES, 90),
            new Filme("F012", "O Iluminado", 1980, 146,
                    Set.of(Genero.TERROR),
                    ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 89),
            new Filme("F013", "O Exorcista", 1973, 122,
                    Set.of(Genero.TERROR, Genero.DRAMA),
                    ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 88),
            new Filme("F014", "A Bruxa", 2015, 92,
                    Set.of(Genero.TERROR, Genero.SUSPENSE),
                    ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 79),
            new Filme("F015", "Click", 2006, 107,
                    Set.of(Genero.COMEDIA, Genero.DRAMA),
                    ClassificacaoEtaria.DOZE, Idioma.INGLES, 72),
            new Filme("F016", "Se Beber, Nao Case!", 2009, 100,
                    Set.of(Genero.COMEDIA),
                    ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 80),
            new Filme("F017", "O Auto da Compadecida", 2000, 104,
                    Set.of(Genero.COMEDIA, Genero.DRAMA),
                    ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 91),
            new Filme("F018", "La La Land", 2016, 128,
                    Set.of(Genero.MUSICAL, Genero.ROMANCE),
                    ClassificacaoEtaria.DOZE, Idioma.INGLES, 89),
            new Filme("F019", "Whiplash", 2014, 106,
                    Set.of(Genero.DRAMA, Genero.MUSICAL),
                    ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 94),
            new Filme("F020", "Cisne Negro", 2010, 108,
                    Set.of(Genero.DRAMA, Genero.SUSPENSE),
                    ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 90),
            new Filme("F021", "Gladiador", 2000, 155,
                    Set.of(Genero.ACAO, Genero.DRAMA),
                    ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 96),
            new Filme("F022", "Mad Max: Estrada da Furia", 2015, 120,
                    Set.of(Genero.ACAO, Genero.AVENTURA),
                    ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 95),
            new Filme("F023", "Pantera Negra", 2018, 134,
                    Set.of(Genero.ACAO, Genero.AVENTURA),
                    ClassificacaoEtaria.DEZ, Idioma.INGLES, 93),
            new Filme("F024", "Up: Altas Aventuras", 2009, 96,
                    Set.of(Genero.ANIMACAO, Genero.AVENTURA),
                    ClassificacaoEtaria.LIVRE, Idioma.INGLES, 94),
            new Filme("F025", "Procurando Nemo", 2003, 100,
                    Set.of(Genero.ANIMACAO, Genero.AVENTURA),
                    ClassificacaoEtaria.LIVRE, Idioma.INGLES, 92),
            new Filme("F026", "Ratatouille", 2007, 111,
                    Set.of(Genero.ANIMACAO, Genero.COMEDIA),
                    ClassificacaoEtaria.LIVRE, Idioma.INGLES, 91),
            new Filme("F027", "Roma", 2018, 135,
                    Set.of(Genero.DRAMA),
                    ClassificacaoEtaria.DEZESSEIS, Idioma.ESPANHOL, 86),
            new Filme("F028", "A Vida e Bela", 1997, 116,
                    Set.of(Genero.COMEDIA, Genero.DRAMA),
                    ClassificacaoEtaria.LIVRE, Idioma.ITALIANO, 90),
            new Filme("F029", "Amelie Poulain", 2001, 122,
                    Set.of(Genero.ROMANCE, Genero.COMEDIA),
                    ClassificacaoEtaria.DEZ, Idioma.FRANCES, 88),
            new Filme("F030", "O Segredo dos Seus Olhos", 2009, 129,
                    Set.of(Genero.DRAMA, Genero.POLICIAL),
                    ClassificacaoEtaria.DEZESSEIS, Idioma.ESPANHOL, 92)
    );

    @Override
    public List<Filme> buscarTodos() {
        return new ArrayList<>(FILMES);
    }

    @Override
    public Filme buscarPorId(String id) {
        return FILMES.stream()
                .filter(filme -> filme.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}