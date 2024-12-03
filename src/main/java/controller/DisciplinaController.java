package controller;

import dto.DisciplinaDTO;
import model.Curso;
import model.Disciplina;
import model.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.DisciplinaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    @Autowired
    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaDTO>> getAllDisciplinas() {
        List<DisciplinaDTO> disciplinasDTO = disciplinaRepository.findAll()
                .stream()
                .map(disciplina -> new DisciplinaDTO(disciplina.getId(), disciplina.getNome(), disciplina.getCodigo(), disciplina.getCurso().getId(), disciplina.getProfessor().getId()))
                .toList();

        if (disciplinasDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Caso não haja disciplinas
        } else {
            return ResponseEntity.ok(disciplinasDTO);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> getDisciplinaById(@PathVariable Integer id) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(id);
        return disciplina.map(value -> ResponseEntity.ok(new DisciplinaDTO(value.getId(), value.getNome(), value.getCodigo(), value.getCurso().getId(), value.getProfessor().getId())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<DisciplinaDTO> createDisciplina(@RequestBody DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = new Disciplina(Integer.valueOf(disciplinaDTO.nome()), disciplinaDTO.codigo(), disciplinaDTO.cursoId(), disciplinaDTO.professorId());
        disciplinaRepository.save(disciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DisciplinaDTO(disciplina.getId(), disciplina.getNome(), disciplina.getCodigo(), disciplina.getCurso().getId(), disciplina.getProfessor().getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> updateDisciplina(@PathVariable Integer id, @RequestBody DisciplinaDTO disciplinaDTO) {
        Optional<Disciplina> disciplinaOptional = disciplinaRepository.findById(id);
        if (disciplinaOptional.isPresent()) {
            Disciplina disciplina = disciplinaOptional.get();
            disciplina.setNome(disciplinaDTO.nome());
            disciplina.setCodigo(disciplinaDTO.codigo());
            disciplina.setCurso(new Curso(disciplinaDTO.cursoId()));  // Atualizando a associação com Curso
            disciplina.setProfessor(new Professor(disciplinaDTO.professorId()));  // Atualizando a associação com Professor
            disciplinaRepository.save(disciplina);
            return ResponseEntity.ok(new DisciplinaDTO(disciplina.getId(), disciplina.getNome(), disciplina.getCodigo(), disciplina.getCurso().getId(), disciplina.getProfessor().getId()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisciplina(@PathVariable Integer id) {
        Optional<Disciplina> disciplinaOptional = disciplinaRepository.findById(id);
        if (disciplinaOptional.isPresent()) {
            disciplinaRepository.delete(disciplinaOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
