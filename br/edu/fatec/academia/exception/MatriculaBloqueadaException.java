package br.edu.fatec.academia.exception;

public class MatriculaBloqueadaException extends Exception {
    public MatriculaBloqueadaException() {
        super("Acesso negado: A matrícula do aluno encontra-se suspensa ou bloqueada!");
    }

    public MatriculaBloqueadaException(String mensagem) {
        super(mensagem);
    }

    public MatriculaBloqueadaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}