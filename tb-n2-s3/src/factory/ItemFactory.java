package factory;

import model.ItemBiblioteca;
import model.Livro;
import model.Revista;
import model.TCC;

public class ItemFactory {
    private interface CriadorItem {
        ItemBiblioteca criar(String codigo, String titulo, String autor, int ano, String extra1, String extra2);
    }

    public static ItemBiblioteca criarItem(
            String tipo,
            String codigo,
            String titulo,
            String autor,
            int ano,
            String extra1,
            String extra2
    ) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de item nao informado.");
        }

        CriadorItem criador = switch (tipo.trim().toUpperCase()) {
            case "LIVRO" -> new CriadorLivro();
            case "REVISTA" -> new CriadorRevista();
            case "TCC" -> new CriadorTCC();
            default -> throw new IllegalArgumentException("Tipo de item invalido.");
        };
        return criador.criar(codigo, titulo, autor, ano, extra1, extra2);
    }

    private static class CriadorLivro implements CriadorItem {
        @Override
        public ItemBiblioteca criar(String codigo, String titulo, String autor, int ano, String extra1, String extra2) {
            return new Livro(codigo, titulo, autor, ano, Integer.parseInt(extra1));
        }
    }

    private static class CriadorRevista implements CriadorItem {
        @Override
        public ItemBiblioteca criar(String codigo, String titulo, String autor, int ano, String extra1, String extra2) {
            return new Revista(codigo, titulo, autor, ano, Integer.parseInt(extra1));
        }
    }

    private static class CriadorTCC implements CriadorItem {
        @Override
        public ItemBiblioteca criar(String codigo, String titulo, String autor, int ano, String extra1, String extra2) {
            return new TCC(codigo, titulo, autor, ano, extra1, extra2);
        }
    }
}
