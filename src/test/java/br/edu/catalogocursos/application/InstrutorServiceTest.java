package br.edu.catalogocursos.application;

import br.edu.catalogocursos.domain.Instrutor;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstrutorServiceTest {

    @Mock
    private InstrutorRepository instrutorRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private InstrutorService instrutorService;

    @Test
    void cadastroComEmailNovoSalvaInstrutor() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina.costa@example.com");
        when(instrutorRepository.existsByEmail("marina.costa@example.com")).thenReturn(false);
        when(instrutorRepository.save(instrutor)).thenReturn(instrutor);

        assertThat(instrutorService.cadastrar(instrutor)).isSameAs(instrutor);
    }

    @Test
    void cadastroComEmailJaUtilizadoERejeitado() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina.costa@example.com");
        when(instrutorRepository.existsByEmail("marina.costa@example.com")).thenReturn(true);

        assertThatThrownBy(() -> instrutorService.cadastrar(instrutor))
                .isInstanceOf(EmailDuplicadoException.class)
                .hasMessageContaining("marina.costa@example.com");
        verify(instrutorRepository, never()).save(any());
    }

    @Test
    void buscarInstrutorInexistenteLancaExcecao() {
        when(instrutorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instrutorService.buscarPorId(999L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Instrutor não encontrado");
    }

    @Test
    void listarCursosDeInstrutorInexistenteLancaExcecao() {
        when(instrutorRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> instrutorService.listarCursos(999L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Instrutor não encontrado");
        verify(cursoRepository, never()).findByInstrutorIdOrderByTituloAsc(anyLong());
    }
}
