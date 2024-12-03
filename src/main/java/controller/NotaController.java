package controller;

import model.Nota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.DisciplinaRepository;
import repository.MatriculaRepository;
import repository.NotaRepository;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final NotaRepository notaRepository;
    private final MatriculaRepository matriculaRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Autowired
    public NotaController(NotaRepository notaRepository,
                          MatriculaRepository matriculaRepository,
                          DisciplinaRepository disciplinaRepository) {
        this.notaRepository = notaRepository;
        this.matriculaRepository = matriculaRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarNota(@RequestParam Integer matriculaId,
                                                @RequestParam Integer disciplinaId,
                                                @RequestParam Double nota) {

        if (!matriculaRepository.existsById(matriculaId)) {
            return ResponseEntity.badRequest().body("Matrícula não encontrada.");
        }

        if (!disciplinaRepository.existsById(disciplinaId)) {
            return ResponseEntity.badRequest().body("Disciplina não encontrada.");
        }


        if (notaRepository.findByMatriculaIdAndDisciplinaId(matriculaId, disciplinaId).isPresent()) {
            return ResponseEntity.badRequest().body(
                    "Nota já foi lançada para esta disciplina e matrícula.");
        }
        Nota novaNota = new Nota();
        novaNota.setMatriculaId(matriculaId);
        novaNota.setDisciplinaId(disciplinaId);
        novaNota.setNota(nota);
        novaNota.setDataLancamento((java.sql.Date) new Date());
        notaRepository.save(novaNota);

        return ResponseEntity.ok("Nota cadastrada com sucesso!");
    }

    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarNota(@RequestParam Integer matriculaId,
                                                @RequestParam Integer disciplinaId,
                                                @RequestParam Double novaNota) {

        Optional<Object> notaOptional = notaRepository.findByMatriculaIdAndDisciplinaId(matriculaId, disciplinaId);
        if (notaOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    "Nota não encontrada para a matrícula e disciplina especificadas.");
        }

        Nota nota = (Nota) notaOptional.get();
        nota.setNota(novaNota);
        nota.setDataLancamento((java.sql.Date) new Date());
        notaRepository.save(nota);

        return ResponseEntity.ok("Nota atualizada com sucesso!");
    }
}
