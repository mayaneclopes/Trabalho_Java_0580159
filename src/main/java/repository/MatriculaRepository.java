package repository;

import model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    List<Matricula> findByTurmaId(Integer turmaId);

    Optional<Object> findByAlunoIdAndTurmaId(Integer alunoId, Integer turmaId);
}
