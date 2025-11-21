package br.com.facilit.kanban.controller;

import static br.com.facilit.kanban.model.Status.CONCLUIDO;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.facilit.kanban.dto.response.ProjetoDTO;
import br.com.facilit.kanban.dto.response.ResponsavelDTO;
import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.ProjetoService;

@WebMvcTest(KanbanController.class)
class KanbanControllerTest {

	@MockitoBean
	private ProjetoService projetoService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	@InjectMocks
	private KanbanController kanbanController;

	private static final String URL = "/kanban";

	@Test
    void transitStatus_Should_ReturnOk_When_StatusChanged() {

		final String endpoint = URL + "/1/status";

		try {
	        when(projetoService.transitStatus(anyLong(), any(Status.class))).thenReturn(new Projeto());

	        mockMvc.perform(patch(endpoint)
	                        .param("status", "CONCLUIDO"))
	                .andExpect(status().isOk());
		} catch (final Exception e) {
			fail();
		}
    }

	@Test
    void transitStatus_Should_ReturnBadRequest_When_StatusCanNotChange() {

		final String endpoint = URL + "/1/status";

		try {
	        doThrow(new StatusTransitionException("")).when(projetoService).transitStatus(anyLong(), any(Status.class));

	        mockMvc.perform(patch(endpoint)
	                        .param("status", "CONCLUIDO"))
	                .andExpect(status().isBadRequest());
		} catch (final Exception e) {
			fail();
		}
    }

	@Test
    void findAllByStatus_ShouldReturnAPageOfProjetoDTOs() {
		final String endpoint = URL + "/CONCLUIDO";

		final SecretariaDTO secretariaDTO = new SecretariaDTO(2L, "Secretaria");
        final ResponsavelDTO responsavelDTO = new ResponsavelDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretariaDTO);
        final ProjetoDTO projetoDTO = ProjetoDTO.builder()
        									    .id(1L)
        									    .nome("Projeto")
        									    .diasAtraso(5)
        									    .inicioPrevisto(null)
        									    .status(CONCLUIDO)
        									    .responsaveis(Set.of(responsavelDTO))
        									    .build();

        final Page<ProjetoDTO> page = new PageImpl<>(List.of(projetoDTO));

        when(projetoService.findAllByStatus(CONCLUIDO, PageRequest.of(0, 10))).thenReturn(page);

        try {
	        mockMvc.perform(get(endpoint)
	                        .param("page", "0")
	                        .param("size", "10"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.content[0].id").value(1L))
	                .andExpect(jsonPath("$.content[0].nome").value("Projeto"))
	                .andExpect(jsonPath("$.totalElements").value(1));
        } catch (final Exception e) {
			fail();
		}
    }

}
