package br.com.facilit.kanban.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.facilit.kanban.dto.request.ResponsavelCreationDTO;
import br.com.facilit.kanban.dto.request.ResponsavelUpdateDTO;
import br.com.facilit.kanban.dto.response.ResponsavelDTO;
import br.com.facilit.kanban.model.Responsavel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResponsavelMapper {

	private final ModelMapper modelMapper;

	public ResponsavelDTO toDTO(final Responsavel responsavel) {
		return modelMapper.map(responsavel, ResponsavelDTO.class);
	}

	public Responsavel toEntity(final ResponsavelDTO responsavelDTO) {
		return modelMapper.map(responsavelDTO, Responsavel.class);
	}

	public Responsavel toEntity(final ResponsavelCreationDTO responsavelCreationDTO) {
		return modelMapper.map(responsavelCreationDTO, Responsavel.class);
	}

	public Responsavel toEntity(final ResponsavelUpdateDTO responsavelUpdateDTO) {
		return modelMapper.map(responsavelUpdateDTO, Responsavel.class);
	}

}
