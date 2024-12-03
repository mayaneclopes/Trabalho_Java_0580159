package controller;

import dto.TurmaDTO;
import model.Curso;
import model.Turma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.TurmaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    private final TurmaRepository turmaRepository;

    @Autowired
    public TurmaController(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    @GetMapping
    public List<TurmaDTO> getAllTurmas() {
        return turmaRepository.findAll()
                .stream()
                .map(turma -> new TurmaDTO(turma.getId(), turma.getAno(), turma.getSemestre(), turma.getCurso().getId()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaDTO> getTurmaById(@PathVariable Integer id) {
        Optional<Turma> turma = turmaRepository.findById(id);
        return turma.map(value -> ResponseEntity.ok(new TurmaDTO(value.getId(), value.getAno(), value.getSemestre(), value.getCurso().getId())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<TurmaDTO> createTurma(@RequestBody TurmaDTO turmaDTO) {
        Turma turma = new Turma(turmaDTO.ano(), turmaDTO.semestre(), new Curso(turmaDTO.cursoId(), null, null, null)); // Curso já deve existir
        turmaRepository.save(turma);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TurmaDTO(turma.getId(), turma.getAno(), turma.getSemestre(), turma.getCurso().getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaDTO> updateTurma(@PathVariable Integer id, @RequestBody TurmaDTO turmaDTO) {
        Optional<Turma> turmaOptional = turmaRepository.findById(id);
        if (turmaOptional.isPresent()) {
            Turma turma = turmaOptional.get();
            turma.setAno(turmaDTO.ano());
            turma.setSemestre(turmaDTO.semestre());
            turma.setCurso(new Curso(turmaDTO.cursoId(), null, null, null));
            turmaRepository.save(turma);
            return ResponseEntity.ok(new TurmaDTO(turma.getId(), turma.getAno(), turma.getSemestre(), turma.getCurso().getId()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurma(@PathVariable Integer id) {
        Optional<Turma> turmaOptional = turmaRepository.findById(id);
        if (turmaOptional.isPresent()) {
            turmaRepository.delete(turmaOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

//não consegui fazer o "turma" como os demais, recebi muitos erros.