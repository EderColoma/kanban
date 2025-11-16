package br.com.facilit.kanban.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.facilit.kanban.dto.SecretariaDTO;
import br.com.facilit.kanban.dto.SecretariaUpdateDTO;
import br.com.facilit.kanban.mapper.SecretariaMapper;
import br.com.facilit.kanban.model.Secretaria;
import br.com.facilit.kanban.repository.SecretariaRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class SecretariaServiceTest {

	@Mock
	private SecretariaRepository secretariaRepository;

	@Mock
	private SecretariaMapper secretariaMapper;

	@Autowired
	@InjectMocks
	private SecretariaService secretariaService;

	@Test
	void findById_Should_ReturnASecretariaDTO_When_ASecretariaWithTheGivenIdWasFound() {
		final Secretaria secretaria = new Secretaria(1L, "Secretaria");
		final SecretariaDTO secretariaDTO = new SecretariaDTO(1L, "Secretaria");

		when(secretariaRepository.findById(anyLong())).thenReturn(Optional.of(secretaria));
		when(secretariaMapper.toDTO(any(Secretaria.class))).thenReturn(secretariaDTO);

		final Optional<SecretariaDTO> optionalSecretariaDTO = secretariaService.findById(1L);

		assertTrue(optionalSecretariaDTO.isPresent());
		assertEquals(Long.valueOf(1), optionalSecretariaDTO.get().getId());
		assertEquals("Secretaria", optionalSecretariaDTO.get().getNome());
	}

	@Test
	void findById_Should_ReturnEmpty_When_NoSecretariaWasFound() {
		when(secretariaRepository.findById(anyLong())).thenReturn(Optional.empty());

		final Optional<SecretariaDTO> optionalSecretariaDTO = secretariaService.findById(1L);

		assertTrue(optionalSecretariaDTO.isEmpty());
	}

	@Test
	void save_Should_SaveAndReturnASecretariaEntity_When_AValidSecretariaDTOIsProvided() {
		final SecretariaDTO secretariaDTO = new SecretariaDTO(null, "Secretaria");
		final Secretaria secretariaToSave = new Secretaria(null, "Secretaria");
		final Secretaria savedSecretaria = new Secretaria(1L, "Secretaria");

		when(secretariaMapper.toEntity(any(SecretariaDTO.class))).thenReturn(secretariaToSave);
		when(secretariaRepository.save(any(Secretaria.class))).thenReturn(savedSecretaria);

		final Secretaria result = secretariaService.save(secretariaDTO);

		assertEquals(Long.valueOf(1), result.getId());
		assertEquals("Secretaria", result.getNome());
	}

	@Test
	void update_Should_NotThrowAnException_When_AValidSecretariaDTOIsProvided() {
		final SecretariaUpdateDTO secretariaUpdateDTO = new SecretariaUpdateDTO(1L, "Secretaria");
		final Secretaria secretariaToUpdate = new Secretaria(1L, "Secretaria");
		final Secretaria savedSecretaria = new Secretaria(1L, "Secretaria");

		when(secretariaRepository.findById(anyLong())).thenReturn(Optional.of(savedSecretaria));
		when(secretariaMapper.toEntity(any(SecretariaUpdateDTO.class))).thenReturn(secretariaToUpdate);
		when(secretariaRepository.save(any(Secretaria.class))).thenReturn(savedSecretaria);

		try {
			secretariaService.update(secretariaUpdateDTO);
			assertTrue(true);
		} catch (final Exception _) {
			assertFalse(true);
		}
	}

	@Test
	void update_Should_ThrowAnEntityNotFoundException_When_NoSecretariaIsFound() {
		final SecretariaUpdateDTO secretariaUpdateDTO = new SecretariaUpdateDTO(1L, "Secretaria");

		when(secretariaRepository.findById(anyLong())).thenReturn(Optional.empty());

		try {
			secretariaService.update(secretariaUpdateDTO);
			assertFalse(true);
		} catch (final EntityNotFoundException e) {
			assertEquals("Secretaria não encontrada", e.getMessage());
		} catch (final Exception _) {
			assertFalse(true);
		}
	}

	@Test
	void delete_Should_NotThrowAnException() {
		try {
			secretariaService.delete(1L);
			assertTrue(true);
		} catch (final Exception _) {
			assertFalse(true);
		}
	}

}
