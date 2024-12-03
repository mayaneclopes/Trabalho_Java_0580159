package controller;

import dto.NotaDTO;
import model.Nota;
import model.Matricula;
import model.Disciplina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.NotaRepository;
import repository.MatriculaRepository;
import repository.DisciplinaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final NotaRepository notaRepository;
    private final MatriculaRepository matriculaRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Autowired
    public NotaController(NotaRepository notaRepository, MatriculaRepository matriculaRepository, DisciplinaRepository disciplinaRepository) {
        this.notaRepository = notaRepository;
        this.matriculaRepository = matriculaRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping
    public ResponseEntity<List<NotaDTO>> getAllNotas() {
        List<NotaDTO> notasDTO = notaRepository.findAll()
                .stream()
                .map(nota -> new NotaDTO(nota.getId(), nota.getMatricula().getId(), nota.getDisciplina().getId(),
                        nota.getNota(), nota.getDataLancamento().toString()))
                .toList();

        if (notasDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(notasDTO);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaDTO> getNotaById(@PathVariable Integer id) {
        Optional<Nota> nota = notaRepository.findById(id);
        return nota.map(value -> ResponseEntity.ok(new NotaDTO(value.getId(), value.getMatricula().getId(),
                        value.getDisciplina().getId(), value.getNota(), value.getDataLancamento().toString())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); //erro valid
    }

    @PostMapping
    public ResponseEntity<NotaDTO> createNota(@RequestBody NotaDTO notaDTO) {
        Optional<Matricula> matriculaOptional = matriculaRepository.findById(notaDTO.matriculaId());
        Optional<Disciplina> disciplinaOptional = disciplinaRepository.findById(notaDTO.disciplinaId());

        if (matriculaOptional.isPresent() && disciplinaOptional.isPresent()) {
            Nota nota = new Nota(matriculaOptional.get(), disciplinaOptional.get(), notaDTO.nota(),
                    java.sql.Date.valueOf(notaDTO.dataLancamento()));
            notaRepository.save(nota);
            return ResponseEntity.status(HttpStatus.CREATED).body(new NotaDTO(nota.getId(), nota.getMatricula().getId(),
                    nota.getDisciplina().getId(), nota.getNota(), nota.getDataLancamento().toString()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //erros valid
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaDTO> updateNota(@PathVariable Integer id, @RequestBody NotaDTO notaDTO) {
        Optional<Nota> notaOptional = notaRepository.findById(id);
        if (notaOptional.isPresent()) {
            Nota nota = notaOptional.get();
            Optional<Matricula> matriculaOptional = matriculaRepository.findById(notaDTO.matriculaId());
            Optional<Disciplina> disciplinaOptional = disciplinaRepository.findById(notaDTO.disciplinaId());

            if (matriculaOptional.isPresent() && disciplinaOptional.isPresent()) {
                nota.setMatricula(matriculaOptional.get());
                nota.setDisciplina(disciplinaOptional.get());
                nota.setNota(notaDTO.nota());
                nota.setDataLancamento(java.sql.Date.valueOf(notaDTO.dataLancamento()));
                notaRepository.save(nota);
                return ResponseEntity.ok(new NotaDTO(nota.getId(), nota.getMatricula().getId(),
                        nota.getDisciplina().getId(), nota.getNota(), nota.getDataLancamento().toString()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // erro
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNota(@PathVariable Integer id) {
        Optional<Nota> notaOptional = notaRepository.findById(id);
        if (notaOptional.isPresent()) {
            notaRepository.delete(notaOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
