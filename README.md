## ANTLR4 Minijava Compiler

Projeto que usa parser/lexer e visitors do ANTLR4 para geração de código baixo nível a partir de um código Minijava

## Gramatica Minijava
https://drive.google.com/file/d/0B1PNkF42ed2eR0JlNWtPQkJuMHM/view?usp=sharing

## Como executar
Com o terminal na pasta codegen/source, execute os seguintes comandos:

    $ make all
    $ make run INPUT=<Arquivo de entrada> OUTPUT=<Arquivo de saida>
        
    
O arquivo gerado estará com código LLVM, para executar-lo:

    $ lli <Arquivo>
    



