package br.edu.catalogocursos.api.mapper;

import br.edu.catalogocursos.api.dto.InstrutorRequest;
import br.edu.catalogocursos.api.dto.InstrutorResponse;
import br.edu.catalogocursos.domain.Instrutor;

public final class InstrutorMapper {

    private InstrutorMapper() {
    }

    public static Instrutor toEntity(InstrutorRequest request) {
        return new Instrutor(request.nome(), request.email());
    }

    public static InstrutorResponse toResponse(Instrutor instrutor) {
        return new InstrutorResponse(instrutor.getId(), instrutor.getNome(), instrutor.getEmail());
    }
}
