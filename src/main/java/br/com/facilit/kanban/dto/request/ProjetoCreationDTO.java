package br.com.facilit.kanban.dto.request;

import java.util.Set;

import br.com.facilit.kanban.validation.ExistingResponsavel;
import br.com.facilit.kanban.validation.Period;
import br.com.facilit.kanban.validation.ValidLocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Period(startField = "inicioPrevisto", endField = "terminoPrevisto", message = "Término previsto não pode ser antes do início previsto", pattern = "dd-MM-uuuu")
@Period(startField = "inicioRealizado", endField = "terminoRealizado", message = "Término realizado não pode ser antes do início relizado", pattern = "dd-MM-uuuu")
public class ProjetoCreationDTO {

	private static final String DATE_FORMAT = "dd-MM-uuuu";
	private static final String INVALID_DATE_MESSAGE = "Data inválida ou formato inesperado. O formato esperado é dd-MM-yyyy";

	@NotBlank(message = "Nome do projeto é obrigatório")
    private String nome;

	@NotBlank(message = "Início previsto do projeto é obrigatório")
	@ValidLocalDate(pattern = DATE_FORMAT, message = INVALID_DATE_MESSAGE)
    private String inicioPrevisto;

	@NotBlank(message = "Término previsto do projeto é obrigatório")
	@ValidLocalDate(pattern = DATE_FORMAT, message = INVALID_DATE_MESSAGE)
    private String terminoPrevisto;

	@ValidLocalDate(pattern = DATE_FORMAT, message = INVALID_DATE_MESSAGE)
    private String inicioRealizado;

	@ValidLocalDate(pattern = DATE_FORMAT, message = INVALID_DATE_MESSAGE)
    private String terminoRealizado;

	@ExistingResponsavel
    @NotEmpty(message = "O Projeto deve ter ao menos um responsável")
    private Set<Long> responsaveisIds;

}
