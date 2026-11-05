package br.edu.catalogocursos.api.dto;

import br.edu.catalogocursos.domain.NivelCurso;

public record CursoResponse(Long id, String titulo, Integer cargaHoraria, NivelCurso nivel,
                            InstrutorResumo instrutor) {

    public record InstrutorResumo(Long id, String nome) {
    }
}
