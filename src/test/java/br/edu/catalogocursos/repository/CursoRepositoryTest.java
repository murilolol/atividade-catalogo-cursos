package br.edu.catalogocursos.repository;

import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.domain.NivelCurso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CursoRepositoryTest {

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void listagemPorInstrutorRetornaSomenteSeusCursosOrdenadosPorTitulo() {
        Instrutor marina = instrutorRepository.save(new Instrutor("Marina Costa", "marina@example.com"));
        Instrutor paulo = instrutorRepository.save(new Instrutor("Paulo Lima", "paulo@example.com"));

        salvar(marina, new Curso("Java orientado a objetos", 60, NivelCurso.INICIANTE));
        salvar(paulo, new Curso("Banco de dados", 30, NivelCurso.INICIANTE));
        salvar(marina, new Curso("APIs REST com Spring Boot", 40, NivelCurso.INTERMEDIARIO));

        List<Curso> cursos = cursoRepository.findByInstrutorIdOrderByTituloAsc(marina.getId());

        assertThat(cursos).extracting(Curso::getTitulo)
                .containsExactly("APIs REST com Spring Boot", "Java orientado a objetos");
        assertThat(cursos).allSatisfy(c -> assertThat(c.getInstrutor().getId()).isEqualTo(marina.getId()));
    }

    @Test
    void existsByEmailDetectaEmailCadastrado() {
        instrutorRepository.save(new Instrutor("Marina Costa", "marina@example.com"));

        assertThat(instrutorRepository.existsByEmail("marina@example.com")).isTrue();
        assertThat(instrutorRepository.existsByEmail("outro@example.com")).isFalse();
    }

    private void salvar(Instrutor instrutor, Curso curso) {
        instrutor.adicionarCurso(curso);
        cursoRepository.save(curso);
    }
}
