package model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "notas")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    private Double nota;
    private Date dataLancamento;

    public Nota() {}

    public Nota(Matricula matricula, Disciplina disciplina,
                Double nota, Date dataLancamento) {
        this.matricula = matricula;
        this.disciplina = disciplina;
        this.nota = nota;
        this.dataLancamento = dataLancamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public void setMatriculaId(Integer matriculaId) {
    }

    public void setDisciplinaId(Integer disciplinaId) {
    }
}
