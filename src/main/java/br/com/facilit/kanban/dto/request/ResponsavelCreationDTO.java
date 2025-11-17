package br.com.facilit.kanban.dto.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponsavelCreationDTO extends ResponsavelRequestDTO {

	@Override
	public Long getId() {
		return null;
	}

	public ResponsavelCreationDTO(final String nome, final String email, final String cargo, final Long secretariaId) {
		super(nome, email, cargo, secretariaId);
	}

}
