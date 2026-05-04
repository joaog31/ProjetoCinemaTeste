package br.com.cinesmart.modelo;

import br.com.cinesmart.excecao.DuracaoInvalidaExcecao;
import br.com.cinesmart.excecao.NotaInvalidaExcecao;
import br.com.cinesmart.excecao.PesoInvalidoExcecao;
import java.util.*;

/**
 * Representa o perfil de preferências de um usuário no CineSmart.
 * Armazena pesos de gêneros, faixa de duração, classificação máxima aceitável,
 * idiomas aceitos, histórico de filmes assistidos e notas dadas.
 */
public class PerfilCinefilo {
    private final Map<Genero, Double> pesosPorGenero;
    private final int duracaoMinima;
    private final int duracaoMaxima;
    private final ClassificacaoEtaria classificacaoMaxima;
    private final Set<Idioma> idiomasAceitos;
    private final Set<String> filmesAssistidos;
    private final Map<String, Integer> notasPorFilme;

    /**
     * Construtor para criar um perfil de usuário.
     * @param duracaoMinima duração mínima preferida (minutos)
     * @param duracaoMaxima duração máxima preferida (minutos)
     * @param classificacaoMaxima classificação etária máxima aceitável
     * @param idiomasAceitos conjunto de idiomas aceitos
     */
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

        // Inicializar pesos com 0.0 para todos os gêneros
        for (Genero genero : Genero.values()) {
            pesosPorGenero.put(genero, 0.0);
        }
    }

    /**
     * Define o peso de preferência para um gênero.
     * @param genero o gênero
     * @param peso valor entre 0.0 (não gosta) e 1.0 (adora)
     * @throws PesoInvalidoExcecao se peso < 0 ou peso > 1
     */
    public void setPeso(Genero genero, double peso) {
        if (peso < 0.0 || peso > 1.0) {
            throw new PesoInvalidoExcecao(
                "Peso deve estar entre 0.0 e 1.0, recebido: " + peso
            );
        }
        pesosPorGenero.put(genero, peso);
    }

    /**
     * Obtém o peso de preferência de um gênero.
     * @param genero o gênero
     * @return peso entre 0.0 e 1.0
     */
    public double getPeso(Genero genero) {
        return pesosPorGenero.getOrDefault(genero, 0.0);
    }

    /**
     * Marca um filme como assistido.
     * @param filmeId ID do filme
     */
    public void marcarComoAssistido(String filmeId) {
        filmesAssistidos.add(Objects.requireNonNull(filmeId));
    }

    /**
     * Verifica se um filme já foi assistido.
     * @param filmeId ID do filme
     * @return true se o filme foi assistido
     */
    public boolean jaAssistiu(String filmeId) {
        return filmesAssistidos.contains(filmeId);
    }

    /**
     * Registra uma nota para um filme.
     * @param filmeId ID do filme
     * @param nota valor entre 1 e 5
     * @throws NotaInvalidaExcecao se nota < 1 ou nota > 5
     */
    public void adicionarNota(String filmeId, int nota) {
        if (nota < 1 || nota > 5) {
            throw new NotaInvalidaExcecao(
                "Nota deve estar entre 1 e 5, recebida: " + nota
            );
        }
        notasPorFilme.put(Objects.requireNonNull(filmeId), nota);
    }

    /**
     * Obtém a nota dada a um filme.
     * @param filmeId ID do filme
     * @return nota (1-5) ou null se não avaliado
     */
    public Integer getNotaPara(String filmeId) {
        return notasPorFilme.get(filmeId);
    }

    /**
     * Obtém todas as notas registradas.
     * @return mapa de filmeId -> nota
     */
    public Map<String, Integer> getNotas() {
        return Collections.unmodifiableMap(new HashMap<>(notasPorFilme));
    }

    /**
     * Obtém todos os filmes marcados como assistidos.
     * @return conjunto de IDs de filmes assistidos
     */
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

    /**
     * Obtém todos os pesos de gênero.
     * @return mapa de gênero -> peso
     */
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
