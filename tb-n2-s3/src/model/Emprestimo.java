package model;

import java.time.LocalDate;

public class Emprestimo {
    private ItemBiblioteca item;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucao;

    public Emprestimo(ItemBiblioteca item, Usuario usuario, int prazoDias) {
        setItem(item);
        setUsuario(usuario);
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(prazoDias);
    }

    public Emprestimo(
            ItemBiblioteca item,
            Usuario usuario,
            LocalDate dataEmprestimo,
            LocalDate dataDevolucaoPrevista,
            LocalDate dataDevolucao
    ) {
        setItem(item);
        setUsuario(usuario);
        setDataEmprestimo(dataEmprestimo);
        setDataDevolucaoPrevista(dataDevolucaoPrevista);
        this.dataDevolucao = dataDevolucao;
    }

    public ItemBiblioteca getItem() {
        return item;
    }

    public final void setItem(ItemBiblioteca item) {
        if (item == null) {
            throw new IllegalArgumentException("Item do emprestimo nao pode ser nulo.");
        }
        this.item = item;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public final void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario do emprestimo nao pode ser nulo.");
        }
        this.usuario = usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public final void setDataEmprestimo(LocalDate dataEmprestimo) {
        if (dataEmprestimo == null) {
            throw new IllegalArgumentException("Data de emprestimo nao pode ser nula.");
        }
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public final void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        if (dataDevolucaoPrevista == null || dataDevolucaoPrevista.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("Data de devolucao prevista invalida.");
        }
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void registrarDevolucao() {
        this.dataDevolucao = LocalDate.now();
    }

    public boolean isAtivo() {
        return dataDevolucao == null;
    }

    @Override
    public String toString() {
        String status = isAtivo() ? "Ativo" : "Devolvido em: " + dataDevolucao;
        return item.getCodigo() + " | " + item.getTitulo() + " | "
                + usuario.getNome() + " | Emprestado em: " + dataEmprestimo
                + " | Devolver ate: " + dataDevolucaoPrevista + " | " + status;
    }
}
