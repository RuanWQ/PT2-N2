package model;

import exceptions.ItemIndisponivelException;
import interfaces.Emprestavel;

public class TCC extends ItemBiblioteca implements Emprestavel {
    private String curso;
    private String orientador;

    public TCC(String codigo, String titulo, String autor, int anoPublicacao, String curso, String orientador) {
        super(codigo, titulo, autor, anoPublicacao);
        setCurso(curso);
        setOrientador(orientador);
    }

    public String getCurso() {
        return curso;
    }

    public final void setCurso(String curso) {
        if (curso == null || curso.trim().length() < 2) {
            throw new IllegalArgumentException("Curso deve ter pelo menos 2 caracteres.");
        }
        this.curso = curso.trim();
    }

    public String getOrientador() {
        return orientador;
    }

    public final void setOrientador(String orientador) {
        if (orientador == null || orientador.trim().length() < 2) {
            throw new IllegalArgumentException("Orientador deve ter pelo menos 2 caracteres.");
        }
        this.orientador = orientador.trim();
    }

    @Override
    public void emprestar() throws ItemIndisponivelException {
        if (!isDisponivel()) {
            throw new ItemIndisponivelException("TCC indisponivel para consulta.");
        }
        setDisponivel(false);
    }

    @Override
    public void devolver() {
        setDisponivel(true);
    }

    @Override
    public int getPrazoDias() {
        return 3;
    }

    @Override
    public String getTipo() {
        return "TCC";
    }

    @Override
    public String getDetalhes() {
        return "Curso: " + curso + ", Orientador: " + orientador;
    }
}
