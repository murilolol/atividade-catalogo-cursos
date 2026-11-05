package br.edu.catalogocursos.api.mapper;

import br.edu.catalogocursos.api.dto.CursoRequest;
import br.edu.catalogocursos.api.dto.CursoResponse;
import br.edu.catalogocursos.api.dto.CursoResumoResponse;
import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;

public final class CursoMapper {

    private CursoMapper() {
    }

    public static Curso toEntity(CursoRequest request) {
        return new Curso(request.titulo(), request.cargaHoraria(), request.nivel());
    }

    public static CursoResponse toResponse(Curso curso) {
        Instrutor instrutor = curso.getInstrutor();
        return new CursoResponse(curso.getId(), curso.getTitulo(), curso.getCargaHoraria(), curso.getNivel(),
                new CursoResponse.InstrutorResumo(instrutor.getId(), instrutor.getNome()));
    }

    public static CursoResumoResponse toResumo(Curso curso) {
        return new CursoResumoResponse(curso.getId(), curso.getTitulo(), curso.getCargaHoraria(), curso.getNivel());
    }
}
