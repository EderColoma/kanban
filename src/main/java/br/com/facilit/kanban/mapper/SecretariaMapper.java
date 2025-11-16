package br.com.facilit.kanban.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.facilit.kanban.dto.SecretariaDTO;
import br.com.facilit.kanban.dto.SecretariaUpdateDTO;
import br.com.facilit.kanban.model.Secretaria;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecretariaMapper {

	private final ModelMapper modelMapper;

	public SecretariaDTO toDTO(Secretaria secretaria) {
		return modelMapper.map(secretaria, SecretariaDTO.class);
	}

	public Secretaria toEntity(SecretariaDTO secretariaDTO) {
		return modelMapper.map(secretariaDTO, Secretaria.class);
	}

	public Secretaria toEntity(SecretariaUpdateDTO secretariaUpdateDTO) {
		return modelMapper.map(secretariaUpdateDTO, Secretaria.class);
	}
}
