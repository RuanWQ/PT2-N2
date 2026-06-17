package interfaces;

import exceptions.ItemIndisponivelException;

public interface Emprestavel {
    void emprestar() throws ItemIndisponivelException;

    void devolver();

    boolean isDisponivel();

    int getPrazoDias();
}
