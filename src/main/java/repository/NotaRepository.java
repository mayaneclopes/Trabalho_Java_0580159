package repository;

import model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {

    List<Nota> findByDisciplinaId(Integer disciplinaId);

    Optional<Object> findByMatriculaIdAndDisciplinaId(Integer matriculaId, Integer disciplinaId);

    List<Nota> findByMatriculaId(Integer matriculaId);
}
