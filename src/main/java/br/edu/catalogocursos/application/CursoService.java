package br.edu.catalogocursos.application;

import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.repository.CursoRepository;
import br.edu.catalogocursos.repository.InstrutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final InstrutorRepository instrutorRepository;

    public CursoService(CursoRepository cursoRepository, InstrutorRepository instrutorRepository) {
        this.cursoRepository = cursoRepository;
        this.instrutorRepository = instrutorRepository;
    }

    @Transactional
    public Curso cadastrar(Long instrutorId, Curso curso) {
        Instrutor instrutor = instrutorRepository.findById(instrutorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado"));
        instrutor.adicionarCurso(curso);
        return cursoRepository.save(curso);
    }

    @Transactional(readOnly = true)
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado"));
    }
}
