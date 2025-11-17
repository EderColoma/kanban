package br.com.facilit.kanban.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.dto.request.SecretariaUpdateDTO;
import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import br.com.facilit.kanban.model.Secretaria;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SecretariaMapper.class, ModelMapper.class})
class SecretariaMapperTest {

	@Autowired
	private SecretariaMapper secretariaMapper;

	@Test
	void toDTO_Should_ConvertTheEntityObjectToSecretariaDTO() {
		final Secretaria secretaria = new Secretaria(1L, "Secretaria");
		final SecretariaDTO secretariaDTO = secretariaMapper.toDTO(secretaria);

		assertNotNull(secretariaDTO);
		assertEquals(Long.valueOf(1), secretariaDTO.getId());
		assertEquals("Secretaria", secretariaDTO.getNome());
	}

	@Test
	void toEntity_Should_ConvertTheSecretariaDTOToAnEntityObject() {
		final SecretariaDTO secretariaDTO = new SecretariaDTO(1L, "Secretaria");
		final Secretaria secretaria = secretariaMapper.toEntity(secretariaDTO);

		assertNotNull(secretaria);
		assertEquals(Long.valueOf(1), secretaria.getId());
		assertEquals("Secretaria", secretaria.getNome());
	}

	@Test
	void toEntity_Should_ConvertTheSecretariaUpdateDTOToAnEntityObject() {
		final SecretariaUpdateDTO secretariaUpdateDTO = new SecretariaUpdateDTO(1L, "Secretaria");
		final Secretaria secretaria = secretariaMapper.toEntity(secretariaUpdateDTO);

		assertNotNull(secretaria);
		assertEquals(Long.valueOf(1), secretaria.getId());
		assertEquals("Secretaria", secretaria.getNome());
	}

}
