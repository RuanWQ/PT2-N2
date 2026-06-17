# Sistema Simples de Biblioteca - POO

Aplicacao Java de console para gerenciar uma biblioteca simples. O sistema
permite cadastrar itens, cadastrar usuarios, realizar emprestimos, devolucoes e
salvar os dados em arquivos CSV.

## Como executar

Requisitos: JDK 17 ou superior.

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out Main
```

Os dados sao carregados da pasta `dados/` ao iniciar e salvos em arquivos CSV
quando a opcao de salvar ou sair e escolhida.

## Requisitos atendidos

1. Encapsulamento estrito: atributos privados e validacoes nos setters.
2. Heranca e classe abstrata: `ItemBiblioteca` e abstrata; `Livro`, `Revista`
   e `TCC` herdam dela.
3. Interfaces: `Emprestavel` e `Persistivel`.
4. Polimorfismo: `getTipo`, `getDetalhes` e `getPrazoDias` mudam conforme o
   objeto (`Livro`, `Revista` ou `TCC`).
5. Colecoes: `BibliotecaRepository` usa `Map` para busca por codigo/matricula,
   `List` para emprestimos e `.map()` em stream.
6. Excecoes customizadas: `ItemNaoEncontradoException`,
   `ItemIndisponivelException` e `UsuarioInvalidoException`.
   Tambem usa excecoes nativas como `IOException`, `IllegalArgumentException`
   e `NumberFormatException`.
7. Padrao de projeto: `ItemFactory` aplica Factory Method com criadores
   especificos para `Livro`, `Revista` e `TCC`.
8. Persistencia: `BibliotecaRepository` le e salva itens, usuarios e
   emprestimos em arquivos CSV.
Os 8 requisitos obrigatorios acima somam 10 pontos. O projeto foi mantido em
console para ficar simples e direto, sem GUI Swing/JavaFX e sem uso de threads.

## Estrutura

```text
src/
  Main.java
  model/
  interfaces/
  exceptions/
  factory/
  repository/
```
