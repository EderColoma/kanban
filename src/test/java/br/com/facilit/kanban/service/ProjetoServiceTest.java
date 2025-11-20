package br.com.facilit.kanban.service;

import static br.com.facilit.kanban.model.Status.EM_ANDAMENTO;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.facilit.kanban.dto.request.ProjetoCreationDTO;
import br.com.facilit.kanban.dto.request.ProjetoUpdateDTO;
import br.com.facilit.kanban.dto.response.ProjetoDTO;
import br.com.facilit.kanban.mapper.ProjetoMapper;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.repository.ProjetoRepository;
import br.com.facilit.kanban.service.status.strategy.ProjetoStatusCalculator;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProjetoServiceTest {

	@Mock
	private ProjetoMapper projetoMapper;

	@Mock
    private ProjetoStatusCalculator projetoStatusCalculator;

	@Mock
	private ProjetoRepository projetoRepository;

	@Mock
	private ResponsavelService responsavelService;

    @Autowired
	@InjectMocks
    private ProjetoService projetoService;

	@Test
    void findById_Should_ReturnTheFoundProjeto() {
        final Projeto projeto = new Projeto();
        projeto.setId(1L);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        final Optional<Projeto> optionalProjeto = projetoService.findById(1L);

        assertTrue(optionalProjeto.isPresent());
		assertEquals(Long.valueOf(1), optionalProjeto.get().getId());
    }

    @Test
    void findById_Should_ReturnEmpty_When_NoProjetoWasFound() {
        when(projetoRepository.findById(1L)).thenReturn(Optional.empty());
        assertTrue(projetoService.findById(1L).isEmpty());
    }

    @Test
    void findDTOById__Should_ReturnTheFoundProjetoAsDTO() {
        final Projeto projeto = new Projeto();

        final ProjetoDTO projetoDTO = new ProjetoDTO();
        projetoDTO.setId(1L);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoMapper.toDTO(projeto)).thenReturn(projetoDTO);

        final Optional<ProjetoDTO> optionalProjetoDTO = projetoService.findDTOById(1L);

        assertTrue(optionalProjetoDTO.isPresent());
		assertEquals(Long.valueOf(1), optionalProjetoDTO.get().getId());
    }

    @Test
    void findDTOById_Should_ReturnEmpty_When_NoProjetoWasFound() {
        when(projetoRepository.findById(1L)).thenReturn(Optional.empty());
        assertTrue(projetoService.findDTOById(1L).isEmpty());
    }

    @Test
    void findAll_Should_ReturnAPageOfProjetos() {
        final Projeto projeto = new Projeto();
        projeto.setId(1L);

        final Page<Projeto> page = new PageImpl<>(List.of(projeto));

        final ProjetoDTO projetoDTO = new ProjetoDTO();
        projetoDTO.setId(1L);

        when(projetoRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(projetoMapper.toDTO(projeto)).thenReturn(projetoDTO);

        final Page<ProjetoDTO> projetosPage = projetoService.findAll(mock(Pageable.class));

        assertEquals(1, projetosPage.getContent().size());
        assertEquals(projeto.getId(), projetosPage.getContent().get(0).getId());
    }

    @Test
    void save_Should_MapEntityCalculateStatusAndPersist() {
        final ProjetoCreationDTO projetoCreationDTO = new ProjetoCreationDTO();
        projetoCreationDTO.setNome("Projeto");
        projetoCreationDTO.setInicioPrevisto("01-01-2026");
        projetoCreationDTO.setTerminoPrevisto("15-06-2026");
        projetoCreationDTO.setResponsaveisIds(Set.of(1L, 2L));

        final Projeto projetoMapped = new Projeto();
        projetoMapped.setNome("Projeto");
        projetoMapped.setInicioPrevisto(now());
        projetoMapped.setTerminoPrevisto(now().plusDays(10));

        final Projeto savedProjeto = new Projeto();
        savedProjeto.setNome("Projeto");
        savedProjeto.setInicioPrevisto(now());
        savedProjeto.setTerminoPrevisto(now().plusDays(10));

        when(projetoMapper.toEntity(projetoCreationDTO)).thenReturn(projetoMapped);
        when(projetoRepository.save(projetoMapped)).thenReturn(savedProjeto);
        when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));

        final Projeto newProjeto = projetoService.save(projetoCreationDTO);

        assertEquals(savedProjeto.getNome(), newProjeto.getNome());
        assertEquals(savedProjeto.getInicioPrevisto(), newProjeto.getInicioPrevisto());
        assertEquals(savedProjeto.getTerminoPrevisto(), newProjeto.getTerminoPrevisto());
    }

    @Test
    void save_Should_ThrowAnException_When_ResponsavelDoesNotExist() {
        final ProjetoCreationDTO projetoCreationDTO = new ProjetoCreationDTO();
        projetoCreationDTO.setNome("Projeto");
        projetoCreationDTO.setInicioPrevisto("01-01-2026");
        projetoCreationDTO.setTerminoPrevisto("10-06-2026");
        projetoCreationDTO.setResponsaveisIds(Set.of(1L, 2L));

        final Projeto projetoMapped = new Projeto();
        projetoMapped.setNome("Projeto");
        projetoMapped.setInicioPrevisto(now());
        projetoMapped.setTerminoPrevisto(now().plusDays(10));

        final Projeto savedProjeto = new Projeto();
        savedProjeto.setNome("Projeto");
        savedProjeto.setInicioPrevisto(now());
        savedProjeto.setTerminoPrevisto(now().plusDays(10));

        when(projetoMapper.toEntity(projetoCreationDTO)).thenReturn(projetoMapped);
        when(responsavelService.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projetoService.save(projetoCreationDTO));
    }

    @Test
    void update_Should_UpdateFieldsCalculateStatusAndSave() {
        final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO();
        projetoUpdateDTO.setId(5L);
        projetoUpdateDTO.setResponsaveisIds(Set.of(1L, 2L));

        final Projeto currentProjeto = new Projeto();
        final Projeto newProjeto = new Projeto();

        newProjeto.setNome("Projeto");
        newProjeto.setStatus(EM_ANDAMENTO);
        newProjeto.setInicioPrevisto(LocalDate.now());
        newProjeto.setTerminoPrevisto(LocalDate.now().plusDays(10));

        when(projetoRepository.findById(5L)).thenReturn(Optional.of(currentProjeto));
        when(projetoMapper.toEntity(projetoUpdateDTO)).thenReturn(newProjeto);
        when(projetoRepository.save(currentProjeto)).thenReturn(currentProjeto);
        when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));

        final Projeto updatedProjeto = projetoService.update(projetoUpdateDTO);

        assertEquals(currentProjeto.getNome(), updatedProjeto.getNome());
        assertEquals(currentProjeto.getStatus(), updatedProjeto.getStatus());
        assertEquals(currentProjeto.getInicioPrevisto(), updatedProjeto.getInicioPrevisto());
        assertEquals(currentProjeto.getTerminoPrevisto(), updatedProjeto.getTerminoPrevisto());
    }

    @Test
    void update_Should_ThrowAnException_When_ResponsavelDoesNotExist() {
        final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO();
        projetoUpdateDTO.setId(5L);
        projetoUpdateDTO.setResponsaveisIds(Set.of(1L, 2L));

        final Projeto currentProjeto = new Projeto();
        final Projeto newProjeto = new Projeto();

        newProjeto.setNome("Projeto");
        newProjeto.setStatus(EM_ANDAMENTO);
        newProjeto.setInicioPrevisto(LocalDate.now());
        newProjeto.setTerminoPrevisto(LocalDate.now().plusDays(10));

        when(projetoRepository.findById(5L)).thenReturn(Optional.of(currentProjeto));
        when(projetoMapper.toEntity(projetoUpdateDTO)).thenReturn(newProjeto);
        when(responsavelService.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projetoService.update(projetoUpdateDTO));
    }

    @Test
    void update_ShouldThrowException_When_ProjetoIsNotFound() {
        final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO();
        projetoUpdateDTO.setId(999L);

        when(projetoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projetoService.update(projetoUpdateDTO));
    }

    @Test
	void delete_Should_NotThrowAnException() {
		try {
			projetoService.delete(1L);
			assertTrue(true);
		} catch (final Exception _) {
			assertFalse(true);
		}
	}

 }
