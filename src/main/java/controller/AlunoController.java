package controller;

import dto.AlunoDTO;
import model.Aluno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.AlunoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    @Autowired
    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> getAllAlunos() {
        List<AlunoDTO> alunosDTO = alunoRepository.findAll()
                .stream()
                .map(aluno -> new AlunoDTO(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getMatricula(), aluno.getDataNascimento().toString()))
                .toList();

        if (alunosDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Caso não haja alunos
        } else {
            return ResponseEntity.ok(alunosDTO);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> getAlunoById(@PathVariable Integer id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        return aluno.map(value -> ResponseEntity.ok(new AlunoDTO(value.getId(), value.getNome(), value.getEmail(), value.getMatricula(), value.getDataNascimento().toString())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<AlunoDTO> createAluno(@RequestBody AlunoDTO alunoDTO) {
        Aluno aluno = new Aluno(alunoDTO.nome(), alunoDTO.email(), alunoDTO.matricula(), java.sql.Date.valueOf(alunoDTO.dataNascimento()));
        alunoRepository.save(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AlunoDTO(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getMatricula(), aluno.getDataNascimento().toString()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoDTO> updateAluno(@PathVariable Integer id, @RequestBody AlunoDTO alunoDTO) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(id);
        if (alunoOptional.isPresent()) {
            Aluno aluno = alunoOptional.get();
            aluno.setNome(alunoDTO.nome());
            aluno.setEmail(alunoDTO.email());
            aluno.setMatricula(alunoDTO.matricula());
            aluno.setDataNascimento(java.sql.Date.valueOf(alunoDTO.dataNascimento()));
            alunoRepository.save(aluno);
            return ResponseEntity.ok(new AlunoDTO(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getMatricula(), aluno.getDataNascimento().toString()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable Integer id) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(id);
        if (alunoOptional.isPresent()) {
            alunoRepository.delete(alunoOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
