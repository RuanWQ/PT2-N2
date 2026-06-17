package model;

import exceptions.ItemIndisponivelException;
import interfaces.Emprestavel;

public class Revista extends ItemBiblioteca implements Emprestavel {
    private int edicao;

    public Revista(String codigo, String titulo, String autor, int anoPublicacao, int edicao) {
        super(codigo, titulo, autor, anoPublicacao);
        setEdicao(edicao);
    }

    public int getEdicao() {
        return edicao;
    }

    public final void setEdicao(int edicao) {
        if (edicao <= 0) {
            throw new IllegalArgumentException("Edicao deve ser positiva.");
        }
        this.edicao = edicao;
    }

    @Override
    public void emprestar() throws ItemIndisponivelException {
        if (!isDisponivel()) {
            throw new ItemIndisponivelException("Revista indisponivel para emprestimo.");
        }
        setDisponivel(false);
    }

    @Override
    public void devolver() {
        setDisponivel(true);
    }

    @Override
    public int getPrazoDias() {
        return 7;
    }

    @Override
    public String getTipo() {
        return "REVISTA";
    }

    @Override
    public String getDetalhes() {
        return "Edicao " + edicao;
    }
}
