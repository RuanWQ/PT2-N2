import exceptions.ItemIndisponivelException;
import exceptions.ItemNaoEncontradoException;
import exceptions.UsuarioInvalidoException;
import factory.ItemFactory;
import model.ItemBiblioteca;
import model.Usuario;
import repository.BibliotecaRepository;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BibliotecaRepository repository = new BibliotecaRepository();

    public static void main(String[] args) {
        try {
            repository.carregar();
        } catch (IOException exception) {
            System.out.println("Nao foi possivel carregar os dados: " + exception.getMessage());
        }

        int opcao;
        do {
            mostrarMenu();
            opcao = lerInteiro("Opcao: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarItem();
                    case 2 -> cadastrarUsuario();
                    case 3 -> listarItens();
                    case 4 -> listarUsuarios();
                    case 5 -> emprestarItem();
                    case 6 -> devolverItem();
                    case 7 -> listarEmprestimos();
                    case 8 -> salvarDados();
                    case 0 -> sair();
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (IllegalArgumentException | ItemNaoEncontradoException
                     | UsuarioInvalidoException | ItemIndisponivelException | IOException exception) {
                System.out.println("Erro: " + exception.getMessage());
            }
        } while (opcao != 0);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== SISTEMA DE BIBLIOTECA =====");
        System.out.println("1 - Cadastrar item");
        System.out.println("2 - Cadastrar usuario");
        System.out.println("3 - Listar itens");
        System.out.println("4 - Listar usuarios");
        System.out.println("5 - Emprestar item");
        System.out.println("6 - Devolver item");
        System.out.println("7 - Listar emprestimos");
        System.out.println("8 - Salvar dados");
        System.out.println("0 - Sair");
    }

    private static void cadastrarItem() {
        System.out.println("Tipos disponiveis: LIVRO, REVISTA, TCC");
        String tipo = lerTexto("Tipo: ");
        String codigo = lerTexto("Codigo: ");
        String titulo = lerTexto("Titulo: ");
        String autor = lerTexto("Autor: ");
        int ano = lerInteiro("Ano de publicacao: ");

        String extra1;
        String extra2 = "";

        if (tipo.equalsIgnoreCase("LIVRO")) {
            extra1 = lerTexto("Quantidade de paginas: ");
        } else if (tipo.equalsIgnoreCase("REVISTA")) {
            extra1 = lerTexto("Numero da edicao: ");
        } else {
            extra1 = lerTexto("Curso: ");
            extra2 = lerTexto("Orientador: ");
        }

        ItemBiblioteca item = ItemFactory.criarItem(tipo, codigo, titulo, autor, ano, extra1, extra2);
        repository.adicionarItem(item);
        System.out.println("Item cadastrado com sucesso.");
    }

    private static void cadastrarUsuario() throws UsuarioInvalidoException {
        String matricula = lerTexto("Matricula: ");
        String nome = lerTexto("Nome: ");
        String email = lerTexto("Email: ");
        repository.adicionarUsuario(new Usuario(matricula, nome, email));
        System.out.println("Usuario cadastrado com sucesso.");
    }

    private static void listarItens() {
        if (repository.listarItens().isEmpty()) {
            System.out.println("Nenhum item cadastrado.");
            return;
        }

        repository.listarItens().forEach(System.out::println);
    }

    private static void listarUsuarios() {
        if (repository.listarUsuarios().isEmpty()) {
            System.out.println("Nenhum usuario cadastrado.");
            return;
        }

        repository.listarUsuarios().forEach(System.out::println);
    }

    private static void emprestarItem()
            throws ItemNaoEncontradoException, UsuarioInvalidoException, ItemIndisponivelException {
        String codigo = lerTexto("Codigo do item: ");
        String matricula = lerTexto("Matricula do usuario: ");
        repository.emprestarItem(codigo, matricula);
        System.out.println("Emprestimo realizado com sucesso.");
    }

    private static void devolverItem() throws ItemNaoEncontradoException {
        String codigo = lerTexto("Codigo do item: ");
        repository.devolverItem(codigo);
        System.out.println("Item devolvido com sucesso.");
    }

    private static void listarEmprestimos() {
        if (repository.listarEmprestimos().isEmpty()) {
            System.out.println("Nenhum emprestimo registrado.");
            return;
        }

        repository.listarEmprestimos().forEach(System.out::println);
    }

    private static void salvarDados() throws IOException {
        repository.salvar();
        System.out.println("Dados salvos com sucesso.");
    }

    private static void sair() throws IOException {
        salvarDados();
        System.out.println("Sistema encerrado.");
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(mensagem));
            } catch (NumberFormatException exception) {
                System.out.println("Digite um numero valido.");
            }
        }
    }
}
