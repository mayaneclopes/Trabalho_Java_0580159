package controller;

import dto.ProfessorDTO;
import model.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.ProfessorRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorController(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> getAllProfessores() {
        List<ProfessorDTO> professoresDTO = professorRepository.findAll()
                .stream()
                .map(professor -> new ProfessorDTO(professor.getId(), professor.getNome(), professor.getEmail(),
                        professor.getTelefone(), professor.getEspecialidade()))
                .toList();

        if (professoresDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  //err
        } else {
            return ResponseEntity.ok(professoresDTO);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getProfessorById(@PathVariable Integer id) {
        Optional<Professor> professor = professorRepository.findById(id);
        return professor.map(value -> ResponseEntity.ok(new ProfessorDTO(value.getId(), value.getNome(), value.getEmail(),
                        value.getTelefone(), value.getEspecialidade())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); //err
    }

    @PostMapping
    public ResponseEntity<ProfessorDTO> createProfessor(@RequestBody ProfessorDTO professorDTO) {
        Professor professor = new Professor(professorDTO.nome(), professorDTO.email(), professorDTO.telefone(), professorDTO.especialidade());
        professorRepository.save(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProfessorDTO(professor.getId(), professor.getNome(),
                professor.getEmail(), professor.getTelefone(), professor.getEspecialidade()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> updateProfessor(@PathVariable Integer id, @RequestBody ProfessorDTO professorDTO) {
        Optional<Professor> professorOptional = professorRepository.findById(id);
        if (professorOptional.isPresent()) {
            Professor professor = professorOptional.get();
            professor.setNome(professorDTO.nome());
            professor.setEmail(professorDTO.email());
            professor.setTelefone(professorDTO.telefone());
            professor.setEspecialidade(professorDTO.especialidade());
            professorRepository.save(professor);
            return ResponseEntity.ok(new ProfessorDTO(professor.getId(), professor.getNome(), professor.getEmail(),
                    professor.getTelefone(), professor.getEspecialidade()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //err
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Integer id) {
        Optional<Professor> professorOptional = professorRepository.findById(id);
        if (professorOptional.isPresent()) {
            professorRepository.delete(professorOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //err
        }
    }
}
