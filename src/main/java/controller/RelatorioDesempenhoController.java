package controller;

import dto.BoletimDTO;
import model.Matricula;
import model.Nota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.MatriculaRepository;
import repository.NotaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioDesempenhoController {

    private final MatriculaRepository matriculaRepository;
    private final NotaRepository notaRepository;

    @Autowired
    public RelatorioDesempenhoController(MatriculaRepository matriculaRepository, NotaRepository notaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.notaRepository = notaRepository;
    }

    @GetMapping("/boletim/{matriculaId}")
    public ResponseEntity<BoletimDTO> getBoletimPorAluno(@PathVariable Integer matriculaId) {
        Optional<Matricula> matriculaOptional = matriculaRepository.findById(matriculaId);

        if (matriculaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Matricula matricula = matriculaOptional.get();
        List<Nota> notas = notaRepository.findByMatriculaId(matriculaId);

        BoletimDTO boletim = new BoletimDTO(
                matricula.getAluno().getNome(),
                matricula.getTurma().getCurso().getNome(),
                notas.stream().map(nota -> new BoletimDTO.NotaDTO(
                        nota.getDisciplina().getNome(),
                        nota.getNota(),
                        nota.getDataLancamento().toString()
                )).collect(Collectors.toList())
        );

        return ResponseEntity.ok(boletim);
    }
}
