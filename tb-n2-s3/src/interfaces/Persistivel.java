package interfaces;

import java.io.IOException;

public interface Persistivel {
    void salvar() throws IOException;

    void carregar() throws IOException;
}
