package dto;

import java.util.List;

public record BoletimDTO(
        String nomeAluno,
        String nomeCurso,
        List<NotaDTO> notas
) {
    public record NotaDTO(
            String nomeDisciplina,
            Double nota,
            String dataLancamento
    ) {
    }
}
