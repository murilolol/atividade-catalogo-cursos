package br.edu.catalogocursos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstrutorTest {

    @Test
    void novoInstrutorComecaComColecaoDeCursosVazia() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina.costa@example.com");

        assertThat(instrutor.getCursos()).isNotNull().isEmpty();
    }

    @Test
    void adicionarCursoMantemOsDoisLadosConsistentes() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina.costa@example.com");
        Curso curso = new Curso("APIs REST com Spring Boot", 40, NivelCurso.INTERMEDIARIO);

        instrutor.adicionarCurso(curso);
        instrutor.adicionarCurso(curso);

        assertThat(curso.getInstrutor()).isSameAs(instrutor);
        assertThat(instrutor.getCursos()).containsExactly(curso);
    }

    @Test
    void colecaoDeCursosNaoPodeSerAlteradaPorFora() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina.costa@example.com");
        Curso curso = new Curso("APIs REST com Spring Boot", 40, NivelCurso.INTERMEDIARIO);

        assertThatThrownBy(() -> instrutor.getCursos().add(curso))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThat(curso.getInstrutor()).isNull();
    }

    @Test
    void adicionarCursoNuloLancaExcecao() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina.costa@example.com");

        assertThatThrownBy(() -> instrutor.adicionarCurso(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
