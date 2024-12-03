package controller;

import dto.CursoDTO;
import model.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.CursoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoRepository cursoRepository;

    @Autowired
    public CursoController(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public ResponseEntity<List<CursoDTO>> getAllCursos() {
        List<CursoDTO> cursosDTO = cursoRepository.findAll()
                .stream()
                .map(curso -> new CursoDTO(curso.getId(), curso.getNome(), curso.getCodigo(), curso.getCargaHoraria()))
                .toList();

        if (cursosDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Caso não haja cursos
        } else {
            return ResponseEntity.ok(cursosDTO);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> getCursoById(@PathVariable Integer id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        return curso.map(value -> ResponseEntity.ok(new CursoDTO(value.getId(), value.getNome(), value.getCodigo(), value.getCargaHoraria())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<CursoDTO> createCurso(@RequestBody CursoDTO cursoDTO) {
        Curso curso = new Curso(cursoDTO.nome(), cursoDTO.codigo(), cursoDTO.cargaHoraria());
        cursoRepository.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CursoDTO(curso.getId(), curso.getNome(), curso.getCodigo(), curso.getCargaHoraria()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> updateCurso(@PathVariable Integer id, @RequestBody CursoDTO cursoDTO) {
        Optional<Curso> cursoOptional = cursoRepository.findById(id);
        if (cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            curso.setNome(cursoDTO.nome());
            curso.setCodigo(cursoDTO.codigo());
            curso.setCargaHoraria(cursoDTO.cargaHoraria());
            cursoRepository.save(curso);
            return ResponseEntity.ok(new CursoDTO(curso.getId(), curso.getNome(), curso.getCodigo(), curso.getCargaHoraria()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Integer id) {
        Optional<Curso> cursoOptional = cursoRepository.findById(id);
        if (cursoOptional.isPresent()) {
            cursoRepository.delete(cursoOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
