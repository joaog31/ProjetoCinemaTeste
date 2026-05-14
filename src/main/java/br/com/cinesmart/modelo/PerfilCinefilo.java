package br.com.cinesmart.modelo;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import br.com.cinesmart.excecao.DuracaoInvalidaExcecao;
import br.com.cinesmart.excecao.NotaInvalidaExcecao;
import br.com.cinesmart.excecao.PesoInvalidoExcecao;

public class PerfilCinefilo {
    private final Map<Genero, Double> pesosPorGenero;
    private final int duracaoMinima;
    private final int duracaoMaxima;
    private final ClassificacaoEtaria classificacaoMaxima;
    private final Set<Idioma> idiomasAceitos;
    private final Set<String> filmesAssistidos;
    private final Map<String, Integer> notasPorFilme;

    public PerfilCinefilo(int duracaoMinima, int duracaoMaxima,
                          ClassificacaoEtaria classificacaoMaxima,
                          Set<Idioma> idiomasAceitos) {
        if (duracaoMinima > duracaoMaxima) {
            throw new DuracaoInvalidaExcecao(
                "Duração mínima (" + duracaoMinima + ") não pode ser maior que máxima (" + duracaoMaxima + ")"
            );
        }
        if (duracaoMinima < 0 || duracaoMaxima < 0) {
            throw new DuracaoInvalidaExcecao("Duração não pode ser negativa");
        }

        this.duracaoMinima = duracaoMinima;
        this.duracaoMaxima = duracaoMaxima;
        this.classificacaoMaxima = Objects.requireNonNull(classificacaoMaxima);
        this.idiomasAceitos = Collections.unmodifiableSet(new HashSet<>(idiomasAceitos));
        this.pesosPorGenero = new HashMap<>();
        this.filmesAssistidos = new HashSet<>();
        this.notasPorFilme = new HashMap<>();

        for (Genero genero : Genero.values()) {
            pesosPorGenero.put(genero, 0.0);
        }
    }

    public void setPeso(Genero genero, double peso) {
        if (peso < 0.0 || peso > 1.0) {
            throw new PesoInvalidoExcecao(
                "Peso deve estar entre 0.0 e 1.0, recebido: " + peso
            );
        }
        pesosPorGenero.put(genero, peso);
    }

    public double getPeso(Genero genero) {
        return pesosPorGenero.getOrDefault(genero, 0.0);
    }

    public void marcarComoAssistido(String filmeId) {
        filmesAssistidos.add(Objects.requireNonNull(filmeId));
    }

    public boolean jaAssistiu(String filmeId) {
        return filmesAssistidos.contains(filmeId);
    }

    public void adicionarNota(String filmeId, int nota) {
        if (nota < 1 || nota > 5) {
            throw new NotaInvalidaExcecao(
                "Nota deve estar entre 1 e 5, recebida: " + nota
            );
        }
        notasPorFilme.put(Objects.requireNonNull(filmeId), nota);
    }

    public Integer getNotaPara(String filmeId) {
        return notasPorFilme.get(filmeId);
    }

    public Map<String, Integer> getNotas() {
        return Collections.unmodifiableMap(new HashMap<>(notasPorFilme));
    }

    public Set<String> getFilmesAssistidos() {
        return Collections.unmodifiableSet(new HashSet<>(filmesAssistidos));
    }

    public int getDuracaoMinima() {
        return duracaoMinima;
    }

    public int getDuracaoMaxima() {
        return duracaoMaxima;
    }

    public ClassificacaoEtaria getClassificacaoMaxima() {
        return classificacaoMaxima;
    }

    public Set<Idioma> getIdiomasAceitos() {
        return new HashSet<>(idiomasAceitos);
    }

    public Map<Genero, Double> getPesosPorGenero() {
        return Collections.unmodifiableMap(new HashMap<>(pesosPorGenero));
    }

    @Override
    public String toString() {
        return "PerfilCinefilo{" +
                "duracaoMinima=" + duracaoMinima +
                ", duracaoMaxima=" + duracaoMaxima +
                ", classificacaoMaxima=" + classificacaoMaxima +
                '}';
    }
}
