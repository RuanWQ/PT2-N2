package repository;

import exceptions.ItemIndisponivelException;
import exceptions.ItemNaoEncontradoException;
import exceptions.UsuarioInvalidoException;
import factory.ItemFactory;
import interfaces.Emprestavel;
import interfaces.Persistivel;
import model.Emprestimo;
import model.ItemBiblioteca;
import model.Usuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BibliotecaRepository implements Persistivel {
    private final Map<String, ItemBiblioteca> itens = new LinkedHashMap<>();
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final List<Emprestimo> emprestimos = new ArrayList<>();
    private final Path arquivoItens = Path.of("dados", "itens.csv");
    private final Path arquivoUsuarios = Path.of("dados", "usuarios.csv");
    private final Path arquivoEmprestimos = Path.of("dados", "emprestimos.csv");

    public void adicionarItem(ItemBiblioteca item) {
        if (itens.containsKey(item.getCodigo())) {
            throw new IllegalArgumentException("Ja existe item com este codigo.");
        }
        itens.put(item.getCodigo(), item);
    }

    public void adicionarUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getMatricula())) {
            throw new IllegalArgumentException("Ja existe usuario com esta matricula.");
        }
        usuarios.put(usuario.getMatricula(), usuario);
    }

    public ItemBiblioteca buscarItem(String codigo) throws ItemNaoEncontradoException {
        ItemBiblioteca item = itens.get(normalizar(codigo));
        if (item == null) {
            throw new ItemNaoEncontradoException("Item nao encontrado.");
        }
        return item;
    }

    public Usuario buscarUsuario(String matricula) throws UsuarioInvalidoException {
        Usuario usuario = usuarios.get(normalizar(matricula));
        if (usuario == null) {
            throw new UsuarioInvalidoException("Usuario nao encontrado.");
        }
        return usuario;
    }

    public List<ItemBiblioteca> listarItens() {
        List<ItemBiblioteca> lista = itens.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted()
                .toList();
        return lista;
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    public List<Emprestimo> listarEmprestimos() {
        return Collections.unmodifiableList(emprestimos);
    }

    public void emprestarItem(String codigoItem, String matriculaUsuario)
            throws ItemNaoEncontradoException, UsuarioInvalidoException, ItemIndisponivelException {
        ItemBiblioteca item = buscarItem(codigoItem);
        Usuario usuario = buscarUsuario(matriculaUsuario);

        if (item instanceof Emprestavel emprestavel) {
            emprestavel.emprestar();
            emprestimos.add(new Emprestimo(item, usuario, emprestavel.getPrazoDias()));
        }
    }

    public void devolverItem(String codigoItem) throws ItemNaoEncontradoException {
        ItemBiblioteca item = buscarItem(codigoItem);
        if (item instanceof Emprestavel emprestavel) {
            emprestavel.devolver();
            emprestimos.stream()
                    .filter(emprestimo -> emprestimo.getItem().getCodigo().equals(item.getCodigo()))
                    .filter(Emprestimo::isAtivo)
                    .findFirst()
                    .ifPresent(Emprestimo::registrarDevolucao);
        }
    }

    @Override
    public void salvar() throws IOException {
        Files.createDirectories(Path.of("dados"));
        salvarItens();
        salvarUsuarios();
        salvarEmprestimos();
    }

    @Override
    public void carregar() throws IOException {
        itens.clear();
        usuarios.clear();
        emprestimos.clear();
        carregarItens();
        carregarUsuarios();
        carregarEmprestimos();
    }

    private void salvarItens() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(arquivoItens, StandardCharsets.UTF_8)) {
            for (ItemBiblioteca item : itens.values()) {
                writer.write(item.getTipo() + ";" + item.getCodigo() + ";" + item.getTitulo() + ";"
                        + item.getAutor() + ";" + item.getAnoPublicacao() + ";" + item.isDisponivel() + ";"
                        + item.getDetalhes().replace(";", ","));
                writer.newLine();
            }
        }
    }

    private void salvarUsuarios() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(arquivoUsuarios, StandardCharsets.UTF_8)) {
            for (Usuario usuario : usuarios.values()) {
                writer.write(usuario.getMatricula() + ";" + usuario.getNome() + ";" + usuario.getEmail());
                writer.newLine();
            }
        }
    }

    private void salvarEmprestimos() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(arquivoEmprestimos, StandardCharsets.UTF_8)) {
            for (Emprestimo emprestimo : emprestimos) {
                String dataDevolucao = emprestimo.getDataDevolucao() == null
                        ? ""
                        : emprestimo.getDataDevolucao().toString();
                writer.write(emprestimo.getItem().getCodigo() + ";"
                        + emprestimo.getUsuario().getMatricula() + ";"
                        + emprestimo.getDataEmprestimo() + ";"
                        + emprestimo.getDataDevolucaoPrevista() + ";"
                        + dataDevolucao);
                writer.newLine();
            }
        }
    }

    private void carregarItens() throws IOException {
        if (!Files.exists(arquivoItens)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(arquivoItens, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";");
                if (campos.length >= 7) {
                    ItemBiblioteca item = criarItemDoArquivo(campos);
                    item.setDisponivel(Boolean.parseBoolean(campos[5]));
                    itens.put(item.getCodigo(), item);
                }
            }
        }
    }

    private ItemBiblioteca criarItemDoArquivo(String[] campos) {
        String tipo = campos[0];
        String codigo = campos[1];
        String titulo = campos[2];
        String autor = campos[3];
        int ano = Integer.parseInt(campos[4]);

        if ("LIVRO".equals(tipo)) {
            String paginas = campos[6].replace(" paginas", "");
            return ItemFactory.criarItem(tipo, codigo, titulo, autor, ano, paginas, "");
        }
        if ("REVISTA".equals(tipo)) {
            String edicao = campos[6].replace("Edicao ", "");
            return ItemFactory.criarItem(tipo, codigo, titulo, autor, ano, edicao, "");
        }

        String[] detalhesTcc = campos[6].replace("Curso: ", "").replace(" Orientador: ", "").split(",");
        String curso = detalhesTcc.length > 0 ? detalhesTcc[0].trim() : "Nao informado";
        String orientador = detalhesTcc.length > 1 ? detalhesTcc[1].trim() : "Nao informado";
        return ItemFactory.criarItem(tipo, codigo, titulo, autor, ano, curso, orientador);
    }

    private void carregarUsuarios() throws IOException {
        if (!Files.exists(arquivoUsuarios)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(arquivoUsuarios, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";");
                if (campos.length >= 3) {
                    try {
                        Usuario usuario = new Usuario(campos[0], campos[1], campos[2]);
                        usuarios.put(usuario.getMatricula(), usuario);
                    } catch (UsuarioInvalidoException exception) {
                        System.out.println("Usuario ignorado no arquivo: " + exception.getMessage());
                    }
                }
            }
        }
    }

    private void carregarEmprestimos() throws IOException {
        if (!Files.exists(arquivoEmprestimos)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(arquivoEmprestimos, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";", -1);
                if (campos.length >= 5) {
                    ItemBiblioteca item = itens.get(normalizar(campos[0]));
                    Usuario usuario = usuarios.get(normalizar(campos[1]));
                    if (item != null && usuario != null) {
                        LocalDate dataDevolucao = campos[4].isBlank() ? null : LocalDate.parse(campos[4]);
                        Emprestimo emprestimo = new Emprestimo(
                                item,
                                usuario,
                                LocalDate.parse(campos[2]),
                                LocalDate.parse(campos[3]),
                                dataDevolucao
                        );
                        emprestimos.add(emprestimo);
                    }
                }
            }
        }
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toUpperCase();
    }
}
