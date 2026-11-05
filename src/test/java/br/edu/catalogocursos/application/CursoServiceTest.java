package br.edu.catalogocursos.application;

import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.domain.NivelCurso;
import br.edu.catalogocursos.repository.CursoRepository;
import br.edu.catalogocursos.repository.InstrutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private InstrutorRepository instrutorRepository;

    @InjectMocks
    private CursoService cursoService;

    @Test
    void cadastroComInstrutorInexistenteERejeitado() {
        when(instrutorRepository.findById(999L)).thenReturn(Optional.empty());
        Curso curso = new Curso("APIs REST com Spring Boot", 40, NivelCurso.INTERMEDIARIO);

        assertThatThrownBy(() -> cursoService.cadastrar(999L, curso))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Instrutor não encontrado");
        verify(cursoRepository, never()).save(any());
    }

    @Test
    void cadastroComInstrutorExistenteAssociaOsDoisLadosESalva() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina.costa@example.com");
        Curso curso = new Curso("APIs REST com Spring Boot", 40, NivelCurso.INTERMEDIARIO);
        when(instrutorRepository.findById(1L)).thenReturn(Optional.of(instrutor));
        when(cursoRepository.save(curso)).thenReturn(curso);

        Curso salvo = cursoService.cadastrar(1L, curso);

        assertThat(salvo.getInstrutor()).isSameAs(instrutor);
        assertThat(instrutor.getCursos()).containsExactly(curso);
        verify(cursoRepository).save(curso);
    }

    @Test
    void buscarCursoInexistenteLancaExcecao() {
        when(cursoRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cursoService.buscarPorId(42L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Curso não encontrado");
    }
}
