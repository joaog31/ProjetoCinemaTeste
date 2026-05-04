package br.com.cinesmart.modelo;

import java.util.Objects;

/**
 * Representa um usuário do sistema CineSmart.
 */
public final class Usuario {
    private final String id;
    private final String nome;
    private final int idade;
    private final PerfilCinefilo perfil;
    private final boolean notificacaoHabilitada;

    /**
     * Construtor para criar um usuário.
     * @param id identificador único do usuário
     * @param nome nome do usuário
     * @param idade idade em anos
     * @param perfil perfil de preferências
     * @param notificacaoHabilitada se push notifications estão habilitadas
     */
    public Usuario(String id, String nome, int idade, PerfilCinefilo perfil, boolean notificacaoHabilitada) {
        this.id = Objects.requireNonNull(id);
        this.nome = Objects.requireNonNull(nome);
        this.idade = idade;
        this.perfil = Objects.requireNonNull(perfil);
        this.notificacaoHabilitada = notificacaoHabilitada;
    }

    /**
     * Construtor alternativo com notificação desabilitada por padrão.
     * @param id identificador único do usuário
     * @param nome nome do usuário
     * @param idade idade em anos
     * @param perfil perfil de preferências
     */
    public Usuario(String id, String nome, int idade, PerfilCinefilo perfil) {
        this(id, nome, idade, perfil, false);
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public PerfilCinefilo getPerfil() {
        return perfil;
    }

    public boolean isNotificacaoHabilitada() {
        return notificacaoHabilitada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", idade=" + idade +
                '}';
    }
}
