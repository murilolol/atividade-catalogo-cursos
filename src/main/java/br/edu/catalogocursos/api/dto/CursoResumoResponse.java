package br.edu.catalogocursos.api.dto;

import br.edu.catalogocursos.domain.NivelCurso;

public record CursoResumoResponse(Long id, String titulo, Integer cargaHoraria, NivelCurso nivel) {
}
