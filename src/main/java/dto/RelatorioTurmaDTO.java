package dto;

import java.util.List;

public record RelatorioTurmaDTO(
        String nomeCurso,
        Integer ano,
        String semestre,
        List<AlunoDTO> alunos
) {
    public record AlunoDTO(
            String nomeAluno,
            List<NotaDTO> notas
    ) {
    }

    public record NotaDTO(
            String nomeDisciplina,
            Double nota
    ) {
    }
}
