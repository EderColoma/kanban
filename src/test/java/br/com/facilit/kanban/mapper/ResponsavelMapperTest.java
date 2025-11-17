package br.com.facilit.kanban.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.dto.request.ResponsavelCreationDTO;
import br.com.facilit.kanban.dto.request.ResponsavelUpdateDTO;
import br.com.facilit.kanban.dto.response.ResponsavelDTO;
import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.model.Secretaria;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ResponsavelMapper.class, ModelMapper.class})
class ResponsavelMapperTest {

	@Autowired
	private ResponsavelMapper responsavelMapper;

	@Test
	void toDTO_Should_ConvertTheEntityObjectToResponsavelDTO() {
		final Secretaria secretaria = new Secretaria(1L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final ResponsavelDTO responsavelDTO = responsavelMapper.toDTO(responsavel);

		assertNotNull(responsavelDTO);
		assertEquals(Long.valueOf(1), responsavelDTO.getId());
		assertEquals("Responsavel", responsavelDTO.getNome());
		assertEquals("responsavel@facilit.com", responsavelDTO.getEmail());
		assertEquals("Product Owner", responsavelDTO.getCargo());
		assertNotNull(responsavelDTO.getSecretaria());
		assertEquals(Long.valueOf(1), responsavelDTO.getSecretaria().getId());
		assertEquals("Secretaria", responsavelDTO.getSecretaria().getNome());
	}

	@Test
	void toEntity_Should_ConvertTheResponsavelDTOToAnEntityObject() {
		final SecretariaDTO secretariaDTO = new SecretariaDTO(1L, "Secretaria");
		final ResponsavelDTO responsavelDTO = new ResponsavelDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretariaDTO);
		final Responsavel responsavel = responsavelMapper.toEntity(responsavelDTO);

		assertNotNull(responsavel);
		assertEquals(Long.valueOf(1), responsavel.getId());
		assertEquals("Responsavel", responsavel.getNome());
		assertEquals("responsavel@facilit.com", responsavel.getEmail());
		assertEquals("Product Owner", responsavel.getCargo());
		assertNotNull(responsavel.getSecretaria());
		assertEquals(Long.valueOf(1), responsavel.getSecretaria().getId());
		assertEquals("Secretaria", responsavel.getSecretaria().getNome());
	}

	@Test
	void toEntity_Should_ConvertTheResponsavelCreationDTOToAnEntityObject() {
		final ResponsavelCreationDTO responsavelCreationDTO = new ResponsavelCreationDTO("Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
		final Responsavel responsavel = responsavelMapper.toEntity(responsavelCreationDTO);

		assertNotNull(responsavel);
		assertEquals("Responsavel", responsavel.getNome());
		assertEquals("responsavel@facilit.com", responsavel.getEmail());
		assertEquals("Product Owner", responsavel.getCargo());
		assertEquals(Long.valueOf(5), responsavel.getSecretaria().getId());
	}

	@Test
	void toEntity_Should_ConvertTheResponsavelUpdateDTOToAnEntityObject() {
		final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
		final Responsavel responsavel = responsavelMapper.toEntity(responsavelUpdateDTO);

		assertNotNull(responsavel);
		assertEquals(Long.valueOf(1), responsavel.getId());
		assertEquals("Responsavel", responsavel.getNome());
		assertEquals("responsavel@facilit.com", responsavel.getEmail());
		assertEquals("Product Owner", responsavel.getCargo());
		assertEquals(Long.valueOf(5), responsavel.getSecretaria().getId());
	}

}
