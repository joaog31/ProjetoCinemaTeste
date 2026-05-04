package br.com.cinesmart.servico;

import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.PerfilCinefilo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsável por filtrar filmes de um catálogo de acordo com as preferências do usuário.
 * Aplica regras de eliminação baseadas em: histórico, classificação etária, idioma e gênero.
 * Lógica pura: não tem dependências externas.
 */
public class FiltroFilmes {

    /**
     * Filtra uma lista de filmes de acordo com o perfil do usuário.
     * Elimina filmes que:
     * - Já foram assistidos
     * - Excedem a classificação etária máxima
     * - Não estão em um idioma aceito
     * - Têm apenas gêneros com peso 0.0 no perfil
     *
     * @param filmes lista de filmes a filtrar
     * @param perfil perfil do usuário
     * @return lista filtrada de filmes adequados
     */
    public List<Filme> filtrar(List<Filme> filmes, PerfilCinefilo perfil) {
        if (filmes == null || filmes.isEmpty()) {
            return Collections.emptyList();
        }

        List<Filme> resultado = new ArrayList<>();

        for (Filme filme : filmes) {
            if (deveSerEliminado(filme, perfil)) {
                continue;
            }
            resultado.add(filme);
        }

        return resultado;
    }

    /**
     * Verifica se um filme deve ser eliminado de acordo com o perfil.
     * @param filme o filme a verificar
     * @param perfil o perfil do usuário
     * @return true se o filme deve ser eliminado, false caso contrário
     */
    private boolean deveSerEliminado(Filme filme, PerfilCinefilo perfil) {
        // Regra 1: Filme já assistido
        if (perfil.jaAssistiu(filme.getId())) {
            return true;
        }

        // Regra 2: Classificação etária acima do permitido
        if (filme.getClassificacao().ehMaiorOuIgual(perfil.getClassificacaoMaxima()) &&
            !filme.getClassificacao().equals(perfil.getClassificacaoMaxima())) {
            return true;
        }

        // Regra 3: Idioma não aceito
        if (!perfil.getIdiomasAceitos().contains(filme.getIdioma())) {
            return true;
        }

        // Regra 4: Todos os gêneros têm peso 0.0 (usuário explicitamente não quer)
        if (todosPesosSaoZero(filme, perfil)) {
            return true;
        }

        return false;
    }

    /**
     * Verifica se todos os gêneros do filme têm peso 0.0 no perfil.
     * @param filme o filme
     * @param perfil o perfil do usuário
     * @return true se todos os gêneros têm peso 0.0
     */
    private boolean todosPesosSaoZero(Filme filme, PerfilCinefilo perfil) {
        for (Genero genero : filme.getGeneros()) {
            if (perfil.getPeso(genero) > 0.0) {
                return false; // encontrou pelo menos um gênero com peso > 0
            }
        }
        return true; // todos os gêneros têm peso 0.0
    }
}
