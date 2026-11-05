package br.edu.catalogocursos.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class EsquemaBancoTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void cursoSemInstrutorERejeitadoPeloBanco() {
        assertThatThrownBy(() -> jdbc.update(
                "insert into curso (titulo, carga_horaria, nivel) values ('Sem instrutor', 10, 'INICIANTE')"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void chaveEstrangeiraImpedeCursoComInstrutorInexistente() {
        assertThatThrownBy(() -> jdbc.update(
                "insert into curso (titulo, carga_horaria, nivel, instrutor_id) values ('Orfao', 10, 'INICIANTE', 999999)"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void emailDeInstrutorEUnicoNoBanco() {
        jdbc.update("insert into instrutor (nome, email) values ('Marina Costa', 'repetido@example.com')");

        assertThatThrownBy(() -> jdbc.update(
                "insert into instrutor (nome, email) values ('Outra Pessoa', 'repetido@example.com')"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
