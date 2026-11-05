package br.edu.catalogocursos.application;

public class EmailDuplicadoException extends RuntimeException {

    public EmailDuplicadoException(String email) {
        super("Email já cadastrado: " + email);
    }
}
