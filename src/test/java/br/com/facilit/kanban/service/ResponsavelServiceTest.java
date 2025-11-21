package br.com.facilit.kanban.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.facilit.kanban.dto.request.ResponsavelCreationDTO;
import br.com.facilit.kanban.dto.request.ResponsavelUpdateDTO;
import br.com.facilit.kanban.dto.response.ResponsavelDTO;
import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import br.com.facilit.kanban.mapper.ResponsavelMapper;
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.model.Secretaria;
import br.com.facilit.kanban.repository.ResponsavelRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ResponsavelServiceTest {

	@Mock
	private ResponsavelRepository responsavelRepository;

	@Mock
	private ResponsavelMapper responsavelMapper;

	@Mock
	private SecretariaService secretariaService;

	@Autowired
	@InjectMocks
	private ResponsavelService responsavelService;

	@Test
	void findById_Should_ReturnAResponsavel_When_AResponsavelWithTheGivenIdWasFound() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

		when(responsavelRepository.findById(anyLong())).thenReturn(Optional.of(responsavel));

		final Optional<Responsavel> optionalResponsavel = responsavelService.findById(1L);

		assertTrue(optionalResponsavel.isPresent());
		assertEquals(Long.valueOf(1), optionalResponsavel.get().getId());
		assertEquals("Responsavel", optionalResponsavel.get().getNome());
		assertEquals("responsavel@facilit.com", optionalResponsavel.get().getEmail());
		assertEquals("Product Owner", optionalResponsavel.get().getCargo());
		assertNotNull(optionalResponsavel.get().getSecretaria());
		assertEquals(Long.valueOf(2), optionalResponsavel.get().getSecretaria().getId());
		assertEquals("Secretaria", optionalResponsavel.get().getSecretaria().getNome());

	}

	@Test
	void findById_Should_ReturnEmpty_When_NoResponsavelWasFound() {
		when(responsavelRepository.findById(anyLong())).thenReturn(Optional.empty());

		final Optional<Responsavel> optionalResponsavel = responsavelService.findById(1L);

		assertTrue(optionalResponsavel.isEmpty());
	}

	@Test
	void findDTOById_Should_ReturnAResponsavel_When_AResponsavelWithTheGivenIdWasFound() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final SecretariaDTO secretariaDTO = new SecretariaDTO(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

		when(responsavelRepository.findById(anyLong())).thenReturn(Optional.of(responsavel));
		when(responsavelMapper.toDTO(any(Responsavel.class))).thenReturn(new ResponsavelDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretariaDTO));

		final Optional<ResponsavelDTO> optionalResponsavelDTO = responsavelService.findDTOById(1L);

		assertTrue(optionalResponsavelDTO.isPresent());
		assertEquals(Long.valueOf(1), optionalResponsavelDTO.get().getId());
		assertEquals("Responsavel", optionalResponsavelDTO.get().getNome());
		assertEquals("responsavel@facilit.com", optionalResponsavelDTO.get().getEmail());
		assertEquals("Product Owner", optionalResponsavelDTO.get().getCargo());
		assertNotNull(optionalResponsavelDTO.get().getSecretaria());
		assertEquals(Long.valueOf(2), optionalResponsavelDTO.get().getSecretaria().getId());
		assertEquals("Secretaria", optionalResponsavelDTO.get().getSecretaria().getNome());
	}

	@Test
	void findDTOById_Should_ReturnEmpty_When_NoResponsavelWasFound() {
		when(responsavelRepository.findById(anyLong())).thenReturn(Optional.empty());

		final Optional<ResponsavelDTO> optionalResponsavelDTO = responsavelService.findDTOById(1L);

		assertTrue(optionalResponsavelDTO.isEmpty());
	}

	@Test
	void findAll_Should_ReturnAPageOfResponsavelDTOs() {
		final Pageable pageable = PageRequest.of(0, 10);

		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

		final SecretariaDTO secretariaDTO = new SecretariaDTO(2L, "Secretaria");
        final ResponsavelDTO responsavelDTO = new ResponsavelDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretariaDTO);

        final Page<Responsavel> responsavelPage = new PageImpl<>(List.of(responsavel));

        when(responsavelRepository.findAll(pageable)).thenReturn(responsavelPage);
        when(responsavelMapper.toDTO(responsavel)).thenReturn(responsavelDTO);

        final Page<ResponsavelDTO> responsavelDTOsPage = responsavelService.findAll(pageable);

        assertNotNull(responsavelDTOsPage);
        assertEquals(1, responsavelDTOsPage.getTotalElements());
        assertEquals(responsavelDTO, responsavelDTOsPage.getContent().get(0));
	}

	@Test
    void findAll_ShouldReturnAnEmptyPage_WhenNoResponsaveisWereFound() {
        final Pageable pageable = PageRequest.of(0, 10);
        final Page<Responsavel> emptyPage = Page.empty(pageable);

        when(responsavelRepository.findAll(pageable)).thenReturn(emptyPage);

        final Page<ResponsavelDTO> responsavelDTOsPage = responsavelService.findAll(pageable);

        assertNotNull(responsavelDTOsPage);
        assertTrue(responsavelDTOsPage.isEmpty());
    }

	@Test
	void save_Should_SaveAndReturnAResponsavelEntity_When_AValidResponsavelCreationDTOIsProvided() {
		final ResponsavelCreationDTO responsavelCreationDTO = new ResponsavelCreationDTO("Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
		final Secretaria secretaria = new Secretaria(5L, "Secretaria");
		final Responsavel responsavelToSave = new Responsavel(null, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Responsavel savedResponsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

		when(responsavelMapper.toEntity(any(ResponsavelCreationDTO.class))).thenReturn(responsavelToSave);
		when(responsavelRepository.save(any(Responsavel.class))).thenReturn(savedResponsavel);
		when(secretariaService.findById(anyLong())).thenReturn(Optional.of(secretaria));

		final Responsavel result = responsavelService.save(responsavelCreationDTO);

		assertEquals(Long.valueOf(1), result.getId());
		assertEquals("Responsavel", result.getNome());
		assertEquals("responsavel@facilit.com", result.getEmail());
		assertEquals("Product Owner", result.getCargo());
		assertNotNull(result.getSecretaria());
		assertEquals(Long.valueOf(5), result.getSecretaria().getId());
		assertEquals("Secretaria", result.getSecretaria().getNome());
	}

	@Test
    void save_ShouldThrowException_WhenSecretariaWasNotFound() {
		final ResponsavelCreationDTO responsavelCreationDTO = new ResponsavelCreationDTO("Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
		final Secretaria secretaria = new Secretaria(5L, "Secretaria");
		final Responsavel responsavelToSave = new Responsavel(null, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

		when(responsavelMapper.toEntity(any(ResponsavelCreationDTO.class))).thenReturn(responsavelToSave);

        assertThrows(EntityNotFoundException.class, () -> responsavelService.save(responsavelCreationDTO));
    }

	 @Test
    void update_Should_NotThrowAnException_When_AValidResponsavelUpdateDTOIsProvided() {
        final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);

        final Secretaria secretaria = new Secretaria(5L, "Secretaria");
        final Responsavel existingResponsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
        final Responsavel mappedResponsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

        when(responsavelRepository.findById(anyLong())).thenReturn(Optional.of(existingResponsavel));
        when(secretariaService.findById(anyLong())).thenReturn(Optional.of(secretaria));
        when(responsavelMapper.toEntity(responsavelUpdateDTO)).thenReturn(mappedResponsavel);

        try {
        	responsavelService.update(responsavelUpdateDTO);
			assertTrue(true);
		} catch (final Exception e) {
			assertFalse(true);
		}
    }

	@Test
    void update_ShouldThrowAnException_WhenResponsavelWasNotFound() {
		final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);

        when(responsavelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> responsavelService.update(responsavelUpdateDTO));
    }

    @Test
    void update_ShouldThrowAnException_WhenSecretariaWasNotFound() {
    	final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);

        when(responsavelRepository.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
        when(secretariaService.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> responsavelService.update(responsavelUpdateDTO));
    }

	@Test
	void delete_Should_NotThrowAnException() {
		try {
			responsavelService.delete(1L);
			assertTrue(true);
		} catch (final Exception e) {
			assertFalse(true);
		}
	}

	@Test
	void findByEmail_Should_ReturnAResponsavel_When_AResponsavelWithTheGivenEmailWasFound() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

		when(responsavelRepository.findByEmail(any(String.class))).thenReturn(Optional.of(responsavel));

		final Optional<Responsavel> optionalResponsavel = responsavelService.findByEmail("responsavel@facilit.com");

		assertTrue(optionalResponsavel.isPresent());
		assertEquals(Long.valueOf(1), optionalResponsavel.get().getId());
		assertEquals("Responsavel", optionalResponsavel.get().getNome());
		assertEquals("responsavel@facilit.com", optionalResponsavel.get().getEmail());
		assertEquals("Product Owner", optionalResponsavel.get().getCargo());
		assertNotNull(optionalResponsavel.get().getSecretaria());
		assertEquals(Long.valueOf(2), optionalResponsavel.get().getSecretaria().getId());
		assertEquals("Secretaria", optionalResponsavel.get().getSecretaria().getNome());
	}

	@Test
	void findByEmail_Should_ReturnEmpty_When_NoResponsavelWasFound() {
		when(responsavelRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

		final Optional<Responsavel> optionalResponsavel = responsavelService.findByEmail("responsavel@facilit.com");

		assertTrue(optionalResponsavel.isEmpty());
	}
}
