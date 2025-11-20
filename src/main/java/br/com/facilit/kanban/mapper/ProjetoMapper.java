package br.com.facilit.kanban.mapper;

import static java.time.format.ResolverStyle.STRICT;
import static java.util.Objects.isNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.facilit.kanban.dto.request.ProjetoCreationDTO;
import br.com.facilit.kanban.dto.request.ProjetoUpdateDTO;
import br.com.facilit.kanban.dto.response.ProjetoDTO;
import br.com.facilit.kanban.model.Projeto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjetoMapper {

	private final ModelMapper modelMapper;

	public ProjetoDTO toDTO(final Projeto projeto) {
		return modelMapper.map(projeto, ProjetoDTO.class);
	}

	public Projeto toEntity(final ProjetoDTO projetoDTO) {
		return modelMapper.map(projetoDTO, Projeto.class);
	}

	public Projeto toEntity(final ProjetoUpdateDTO projetoUpdateDTO) {
		final Projeto projeto = modelMapper.map(projetoUpdateDTO, Projeto.class);
		mapDates(projeto, projetoUpdateDTO);

		return projeto;
	}

	public Projeto toEntity(final ProjetoCreationDTO projetoCreationDTO) {
		final Projeto projeto = modelMapper.map(projetoCreationDTO, Projeto.class);
		mapDates(projeto, projetoCreationDTO);

		return projeto;
	}

	private void mapDates(final Projeto projeto, final ProjetoCreationDTO projetoCreationDTO)  {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);

		projeto.setInicioPrevisto(LocalDate.parse(projetoCreationDTO.getInicioPrevisto(), formatter));
		projeto.setTerminoPrevisto(LocalDate.parse(projetoCreationDTO.getTerminoPrevisto(), formatter));

		if(!isNull(projetoCreationDTO.getInicioRealizado())) {
			projeto.setInicioRealizado(LocalDate.parse(projetoCreationDTO.getInicioRealizado(), formatter));
		}

		if(!isNull(projetoCreationDTO.getTerminoRealizado())) {
			projeto.setTerminoRealizado(LocalDate.parse(projetoCreationDTO.getTerminoRealizado(), formatter));
		}
	}

	private void mapDates(final Projeto projeto, final ProjetoUpdateDTO projetoUpdateDTO)  {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);

		projeto.setInicioPrevisto(LocalDate.parse(projetoUpdateDTO.getInicioPrevisto(), formatter));
		projeto.setTerminoPrevisto(LocalDate.parse(projetoUpdateDTO.getTerminoPrevisto(), formatter));

		if(!isNull(projetoUpdateDTO.getInicioRealizado())) {
			projeto.setInicioRealizado(LocalDate.parse(projetoUpdateDTO.getInicioRealizado(), formatter));
		}

		if(!isNull(projetoUpdateDTO.getTerminoRealizado())) {
			projeto.setTerminoRealizado(LocalDate.parse(projetoUpdateDTO.getTerminoRealizado(), formatter));
		}
	}
}
