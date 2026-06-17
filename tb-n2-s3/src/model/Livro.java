package model;

import exceptions.ItemIndisponivelException;
import interfaces.Emprestavel;

public class Livro extends ItemBiblioteca implements Emprestavel {
    private int quantidadePaginas;

    public Livro(String codigo, String titulo, String autor, int anoPublicacao, int quantidadePaginas) {
        super(codigo, titulo, autor, anoPublicacao);
        setQuantidadePaginas(quantidadePaginas);
    }

    public int getQuantidadePaginas() {
        return quantidadePaginas;
    }

    public final void setQuantidadePaginas(int quantidadePaginas) {
        if (quantidadePaginas <= 0) {
            throw new IllegalArgumentException("Quantidade de paginas deve ser positiva.");
        }
        this.quantidadePaginas = quantidadePaginas;
    }

    @Override
    public void emprestar() throws ItemIndisponivelException {
        if (!isDisponivel()) {
            throw new ItemIndisponivelException("Livro indisponivel para emprestimo.");
        }
        setDisponivel(false);
    }

    @Override
    public void devolver() {
        setDisponivel(true);
    }

    @Override
    public int getPrazoDias() {
        return 14;
    }

    @Override
    public String getTipo() {
        return "LIVRO";
    }

    @Override
    public String getDetalhes() {
        return quantidadePaginas + " paginas";
    }
}
