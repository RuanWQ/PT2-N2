package model;

public abstract class ItemBiblioteca implements Comparable<ItemBiblioteca> {
    private String codigo;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private boolean disponivel;

    public ItemBiblioteca(String codigo, String titulo, String autor, int anoPublicacao) {
        setCodigo(codigo);
        setTitulo(titulo);
        setAutor(autor);
        setAnoPublicacao(anoPublicacao);
        this.disponivel = true;
    }

    public String getCodigo() {
        return codigo;
    }

    public final void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo nao pode ser vazio.");
        }
        this.codigo = codigo.trim().toUpperCase();
    }

    public String getTitulo() {
        return titulo;
    }

    public final void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().length() < 2) {
            throw new IllegalArgumentException("Titulo deve ter pelo menos 2 caracteres.");
        }
        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public final void setAutor(String autor) {
        if (autor == null || autor.trim().length() < 2) {
            throw new IllegalArgumentException("Autor deve ter pelo menos 2 caracteres.");
        }
        this.autor = autor.trim();
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public final void setAnoPublicacao(int anoPublicacao) {
        if (anoPublicacao < 1900 || anoPublicacao > 2026) {
            throw new IllegalArgumentException("Ano de publicacao invalido.");
        }
        this.anoPublicacao = anoPublicacao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public abstract String getTipo();

    public abstract String getDetalhes();

    @Override
    public int compareTo(ItemBiblioteca outro) {
        return this.titulo.compareToIgnoreCase(outro.titulo);
    }

    @Override
    public String toString() {
        String status = disponivel ? "Disponivel" : "Emprestado";
        return codigo + " | " + getTipo() + " | " + titulo + " | " + autor
                + " | " + anoPublicacao + " | " + status + " | " + getDetalhes();
    }
}
