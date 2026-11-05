package br.edu.catalogocursos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private NivelCurso nivel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;

    protected Curso() {
    }

    public Curso(String titulo, Integer cargaHoraria, NivelCurso nivel) {
        this.titulo = titulo;
        this.cargaHoraria = cargaHoraria;
        this.nivel = nivel;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public NivelCurso getNivel() {
        return nivel;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }

    void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }
}
