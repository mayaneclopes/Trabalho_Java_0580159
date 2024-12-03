package dto;

public record NotaDTO(Integer id, Integer matriculaId, Integer disciplinaId,
                      Double nota, String dataLancamento) {
}
