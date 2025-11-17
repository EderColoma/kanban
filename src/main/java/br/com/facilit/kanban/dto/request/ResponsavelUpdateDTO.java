package br.com.facilit.kanban.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
public class ResponsavelUpdateDTO extends ResponsavelRequestDTO {

	@Getter
	@Setter
	@NotNull(message = "ID é obrigatório")
	protected Long id;

	public ResponsavelUpdateDTO(final Long id, final String nome, final String email, final String cargo, final Long secretariaId) {
		super(nome, email, cargo, secretariaId);
		this.id = id;
	}

}