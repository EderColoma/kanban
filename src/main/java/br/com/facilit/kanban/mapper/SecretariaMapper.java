package br.com.facilit.kanban.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.facilit.kanban.dto.request.SecretariaUpdateDTO;
import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import br.com.facilit.kanban.model.Secretaria;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecretariaMapper {

	private final ModelMapper modelMapper;

	public SecretariaDTO toDTO(final Secretaria secretaria) {
		return modelMapper.map(secretaria, SecretariaDTO.class);
	}

	public Secretaria toEntity(final SecretariaDTO secretariaDTO) {
		return modelMapper.map(secretariaDTO, Secretaria.class);
	}

	public Secretaria toEntity(final SecretariaUpdateDTO secretariaUpdateDTO) {
		return modelMapper.map(secretariaUpdateDTO, Secretaria.class);
	}
}
