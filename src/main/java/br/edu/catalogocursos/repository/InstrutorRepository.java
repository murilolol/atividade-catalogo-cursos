package br.edu.catalogocursos.repository;

import br.edu.catalogocursos.domain.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {

    boolean existsByEmail(String email);
}
