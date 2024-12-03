package controller;

import dto.MatriculaDTO;
import model.Aluno;
import model.Matricula;
import model.Turma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.MatriculaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaRepository matriculaRepository;

    @Autowired
    public MatriculaController(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    @GetMapping
    public ResponseEntity<List<MatriculaDTO>> getAllMatriculas() {
        List<MatriculaDTO> matriculasDTO = matriculaRepository.findAll()
                .stream()
                .map(matricula -> new MatriculaDTO(matricula.getId(), matricula.getAluno().getId(), matricula.getTurma().getId()))
                .toList();

        if (matriculasDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Caso não haja matrículas
        } else {
            return ResponseEntity.ok(matriculasDTO);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> getMatriculaById(@PathVariable Integer id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        return matricula.map(value -> ResponseEntity.ok(new MatriculaDTO(value.getId(), value.getAluno().getId(), value.getTurma().getId())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<MatriculaDTO> createMatricula(@RequestBody MatriculaDTO matriculaDTO) {
        Matricula matricula = new Matricula(new Aluno(matriculaDTO.alunoId()), new Turma(matriculaDTO.turmaId()));
        matriculaRepository.save(matricula);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MatriculaDTO(matricula.getId(), matricula.getAluno().getId(), matricula.getTurma().getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDTO> updateMatricula(@PathVariable Integer id, @RequestBody MatriculaDTO matriculaDTO) {
        Optional<Matricula> matriculaOptional = matriculaRepository.findById(id);
        if (matriculaOptional.isPresent()) {
            Matricula matricula = matriculaOptional.get();
            matricula.setAluno(new Aluno(matriculaDTO.alunoId()));
            matricula.setTurma(new Turma(matriculaDTO.turmaId()));
            matriculaRepository.save(matricula);
            return ResponseEntity.ok(new MatriculaDTO(matricula.getId(), matricula.getAluno().getId(), matricula.getTurma().getId()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatricula(@PathVariable Integer id) {
        Optional<Matricula> matriculaOptional = matriculaRepository.findById(id);
        if (matriculaOptional.isPresent()) {
            matriculaRepository.delete(matriculaOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
