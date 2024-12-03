package controller;

import dto.RelatorioTurmaDTO;
import model.Matricula;
import model.Nota;
import model.Turma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.MatriculaRepository;
import repository.NotaRepository;
import repository.TurmaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioTurmaController {

    private final TurmaRepository turmaRepository;
    private final MatriculaRepository matriculaRepository;
    private final NotaRepository notaRepository;

    @Autowired
    public RelatorioTurmaController(TurmaRepository turmaRepository, MatriculaRepository matriculaRepository, NotaRepository notaRepository) {
        this.turmaRepository = turmaRepository;
        this.matriculaRepository = matriculaRepository;
        this.notaRepository = notaRepository;
    }

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<RelatorioTurmaDTO> getRelatorioPorTurma(@PathVariable Integer turmaId) {
        Optional<Turma> turmaOptional = turmaRepository.findById(turmaId);

        if (turmaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Turma turma = turmaOptional.get();
        List<Matricula> matriculas = matriculaRepository.findByTurmaId(turmaId);

        RelatorioTurmaDTO relatorio = new RelatorioTurmaDTO(
                turma.getCurso().getNome(),
                turma.getAno(),
                turma.getSemestre(),
                matriculas.stream().map(matricula -> new RelatorioTurmaDTO.AlunoDTO(
                        matricula.getAluno().getNome(),
                        notaRepository.findByMatriculaId(matricula.getId()).stream().map(nota -> new RelatorioTurmaDTO.NotaDTO(
                                nota.getDisciplina().getNome(),
                                nota.getNota()
                        )).collect(Collectors.toList())
                )).collect(Collectors.toList())
        );

        return ResponseEntity.ok(relatorio);
    }
}
