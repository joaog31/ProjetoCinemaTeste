package br.com.cinesmart.servico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.cinesmart.modelo.Filme;
import br.com.cinesmart.modelo.Genero;
import br.com.cinesmart.modelo.PerfilCinefilo;

public class FiltroFilmes {

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

    private boolean deveSerEliminado(Filme filme, PerfilCinefilo perfil) {
        if (perfil.jaAssistiu(filme.getId())) {
            return true;
        }

        if (filme.getClassificacao().ehMaiorOuIgual(perfil.getClassificacaoMaxima()) &&
            !filme.getClassificacao().equals(perfil.getClassificacaoMaxima())) {
            return true;
        }

        if (!perfil.getIdiomasAceitos().contains(filme.getIdioma())) {
            return true;
        }

        if (todosPesosSaoZero(filme, perfil)) {
            return true;
        }

        return false;
    }

    private boolean todosPesosSaoZero(Filme filme, PerfilCinefilo perfil) {
        for (Genero genero : filme.getGeneros()) {
            if (perfil.getPeso(genero) > 0.0) {
                return false;
            }
        }
        return true;
    }
}
