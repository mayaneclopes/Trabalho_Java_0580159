package model;

import jakarta.persistence.*;
import org.hibernate.id.IntegralDataTypeHolder;

import java.util.Date;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String email;
    private String matricula;
    private Date dataNascimento;

    public Aluno() {}

    public Aluno(String nome, String email, String matricula, Date dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
    }

    public Aluno(Integer integer, Object email, Object matricula, Object dataNascimento) {
    }

    public Aluno(Integer integer) {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
