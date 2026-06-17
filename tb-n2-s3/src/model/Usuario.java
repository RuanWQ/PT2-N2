package model;

import exceptions.UsuarioInvalidoException;

public class Usuario {
    private String matricula;
    private String nome;
    private String email;

    public Usuario(String matricula, String nome, String email) throws UsuarioInvalidoException {
        setMatricula(matricula);
        setNome(nome);
        setEmail(email);
    }

    public String getMatricula() {
        return matricula;
    }

    public final void setMatricula(String matricula) throws UsuarioInvalidoException {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new UsuarioInvalidoException("Matricula nao pode ser vazia.");
        }
        this.matricula = matricula.trim().toUpperCase();
    }

    public String getNome() {
        return nome;
    }

    public final void setNome(String nome) throws UsuarioInvalidoException {
        if (nome == null || nome.trim().length() < 3) {
            throw new UsuarioInvalidoException("Nome deve ter pelo menos 3 caracteres.");
        }
        this.nome = nome.trim();
    }

    public String getEmail() {
        return email;
    }

    public final void setEmail(String email) throws UsuarioInvalidoException {
        if (email == null || !email.contains("@")) {
            throw new UsuarioInvalidoException("Email invalido.");
        }
        this.email = email.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return matricula + " | " + nome + " | " + email;
    }
}
