package br.com.cinesmart.modelo;

import java.util.Objects;

public final class Usuario {
    private final String id;
    private final String nome;
    private final int idade;
    private final PerfilCinefilo perfil;
    private final boolean notificacaoHabilitada;

    public Usuario(String id, String nome, int idade, PerfilCinefilo perfil, boolean notificacaoHabilitada) {
        this.id = Objects.requireNonNull(id);
        this.nome = Objects.requireNonNull(nome);
        this.idade = idade;
        this.perfil = Objects.requireNonNull(perfil);
        this.notificacaoHabilitada = notificacaoHabilitada;
    }

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
