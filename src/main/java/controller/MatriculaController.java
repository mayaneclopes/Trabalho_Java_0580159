package controller;

import model.Matricula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.AlunoRepository;
import repository.MatriculaRepository;
import repository.TurmaRepository;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    @Autowired
    public MatriculaController(MatriculaRepository matriculaRepository,
                               AlunoRepository alunoRepository,
                               TurmaRepository turmaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    @PostMapping("/inscrever")
    public ResponseEntity<String> inscreverAluno(@RequestParam Integer alunoId,
                                                 @RequestParam Integer turmaId) {
        // Verificar se o aluno existe
        if (!alunoRepository.existsById(alunoId)) {
            return ResponseEntity.badRequest().body("Aluno não encontrado.");
        }

        if (!turmaRepository.existsById(turmaId)) {
            return ResponseEntity.badRequest().body("Turma não encontrada.");
        }


        if (matriculaRepository.findByAlunoIdAndTurmaId(alunoId, turmaId).isPresent()) {
            return ResponseEntity.badRequest().body("Aluno já está matriculado nesta turma.");
        }


        Matricula matricula = new Matricula();
        matricula.setAlunoId(alunoId);
        matricula.setTurmaId(turmaId);
        matriculaRepository.save(matricula);

        return ResponseEntity.ok("Aluno matriculado com sucesso!");
    }
}
