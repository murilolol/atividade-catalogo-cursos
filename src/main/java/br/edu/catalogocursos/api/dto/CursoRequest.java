package br.edu.catalogocursos.api.dto;

import br.edu.catalogocursos.domain.NivelCurso;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CursoRequest(
        @NotBlank @Size(min = 3, max = 150) String titulo,
        @NotNull @Min(1) @Max(400) Integer cargaHoraria,
        @NotNull NivelCurso nivel,
        @NotNull Long instrutorId) {
}
