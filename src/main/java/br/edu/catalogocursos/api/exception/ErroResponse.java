package br.edu.catalogocursos.api.exception;

public record ErroResponse(int status, String message, String path) {
}
