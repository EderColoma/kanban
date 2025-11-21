package br.com.facilit.kanban.controller;

import static br.com.facilit.kanban.model.Status.CONCLUIDO;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.facilit.kanban.dto.request.ProjetoCreationDTO;
import br.com.facilit.kanban.dto.request.ProjetoUpdateDTO;
import br.com.facilit.kanban.dto.response.ProjetoDTO;
import br.com.facilit.kanban.dto.response.ResponsavelDTO;
import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.model.Secretaria;
import br.com.facilit.kanban.service.ProjetoService;
import br.com.facilit.kanban.service.ResponsavelService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ProjetoController.class)
class ProjetoControllerTest {

	@MockitoBean
	private ProjetoService projetoService;

	@MockitoBean
	private ResponsavelService responsavelService;

	@Autowired
	@InjectMocks
	private ProjetoController projetoController;

	@Autowired
	private MockMvc mockMvc;

	private static final String URL = "/projeto";

	@Test
	void findById_Should_ReturnProjeto_When_ProjetoIsFound() {

		final String endpoint = URL + "/1";

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

		when(projetoService.findDTOById(1L)).thenReturn(Optional.of(projetoDTO));

		try {
			mockMvc.perform(get(endpoint))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Projeto"));
		} catch (final Exception e) {
			fail();
		}
	}

	@Test
	void findById_Should_ReturnNoContent_When_NoProjetolIsFound() {

		final String endpoint = URL + "/1";

		when(projetoService.findDTOById(1L)).thenReturn(Optional.empty());

		try {
			mockMvc.perform(get(endpoint)).andExpect(status().isNoContent());
		} catch (final Exception e) {
			fail();
		}
	}

	@Test
    void findAll_ShouldReturnAPageOfProjetos() {
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

        when(projetoService.findAll(PageRequest.of(0, 10))).thenReturn(page);

        try {
	        mockMvc.perform(get(URL)
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

	@Test
	void create_Should_ReturnOk_When_CreatingAProjeto() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.save(any(ProjetoCreationDTO.class))).thenReturn(projeto);

		try {
			final ProjetoCreationDTO projetoCreationDTO = new ProjetoCreationDTO("Projeto", "01-10-2025", "30-11-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoCreationDTO);

			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isOk());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_AtLeastOneResponsavelDoesNotExist() {
		when(responsavelService.findById(anyLong())).thenReturn(Optional.empty());

		try {
			final ProjetoCreationDTO projetoCreationDTO = new ProjetoCreationDTO("Projeto", "01-10-2025", "30-11-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoCreationDTO);

			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_TheNameIsBlank() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.save(any(ProjetoCreationDTO.class))).thenReturn(projeto);

		try {
			final ProjetoCreationDTO projetoCreationDTO = new ProjetoCreationDTO("", "01-10-2025", "25-06-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoCreationDTO);

			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_TheInicioPrevistoIsNull() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.save(any(ProjetoCreationDTO.class))).thenReturn(projeto);

		try {
			final ProjetoCreationDTO projetoCreationDTO = new ProjetoCreationDTO("Projeto", null, "01-10-2025", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoCreationDTO);

			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_TheTerminoPrevistoIsNull() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.save(any(ProjetoCreationDTO.class))).thenReturn(projeto);

		try {
			final ProjetoCreationDTO projetoCreationDTO = new ProjetoCreationDTO("Projeto", "01-10-2025", null, null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoCreationDTO);

			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_TheTerminoPrevistoIsBeforeInicioPrevisto() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.save(any(ProjetoCreationDTO.class))).thenReturn(projeto);

		try {
			final ProjetoCreationDTO projetoCreationDTO = new ProjetoCreationDTO("Projeto", "01-10-2026", "25-01-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoCreationDTO);

			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_NoResponsavelIsGiven() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.save(any(ProjetoCreationDTO.class))).thenReturn(projeto);

		try {
			final ProjetoCreationDTO projetoCreationDTO = new ProjetoCreationDTO("Projeto", "01-10-2025", "25-06-2026", null, null, new HashSet<>());
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoCreationDTO);

			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void update_Should_ReturnOk_When_CreatingAProjeto() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.update(any(ProjetoUpdateDTO.class))).thenReturn(projeto);

		try {
			final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO(1L, "Projeto", "01-10-2025", "25-06-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoUpdateDTO);

			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isOk());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_AtLeastOneResponsavelDoesNotExist() {
		when(responsavelService.findById(anyLong())).thenReturn(Optional.empty());

		try {
			final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO(1L, "Projeto", "01-10-2025", "25-06-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoUpdateDTO);

			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheNameIsBlank() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.update(any(ProjetoUpdateDTO.class))).thenReturn(projeto);

		try {
			final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO(1L, "", "01-10-2025", "25-06-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoUpdateDTO);

			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheInicioPrevistoIsNull() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(projetoService.update(any(ProjetoUpdateDTO.class))).thenReturn(projeto);
		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));

		try {
			final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO(1L, "Projeto", null, "01-05-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoUpdateDTO);

			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheTerminoPrevistoIsNull() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.update(any(ProjetoUpdateDTO.class))).thenReturn(projeto);

		try {
			final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO(1L, "Projeto", "01-05-2026", null, null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoUpdateDTO);

			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheTerminoPrevistoIsBeforeInicioPrevisto() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.update(any(ProjetoUpdateDTO.class))).thenReturn(projeto);

		try {
			final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO(1L, "Projeto", "01-05-2026", "01-02-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoUpdateDTO);

			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_NoResponsavelIsGiven() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				 .id(1L)
				 .nome("Projeto")
				 .responsaveis(Set.of(responsavel))
				 .build();

		when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));
		when(projetoService.update(any(ProjetoUpdateDTO.class))).thenReturn(projeto);

		try {
			final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO(1L, "Projeto", "01-10-2025", "25-06-2026", null, null, new HashSet<>());
			final ObjectMapper objectMapper = new  ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoUpdateDTO);

			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheProjetoDoesNotExist() {
		doThrow(new EntityNotFoundException("Projeto não encontrado")).when(projetoService).update(any(ProjetoUpdateDTO.class));

		try {
			when(responsavelService.findById(anyLong())).thenReturn(Optional.of(new Responsavel()));

			final ProjetoUpdateDTO projetoUpdateDTO = new ProjetoUpdateDTO(1L, "Projeto", "01-10-2025", "25-06-2026", null, null, Set.of(1L));
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(projetoUpdateDTO);

			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception e) {
			fail();
		}
	}

	@Test
	void delete_Should_ReturnOk_When_DeletingAProjeto() {
		final String endpoint = URL + "/1";

		doNothing().when(projetoService).delete(1L);

		try {
			mockMvc.perform(delete(endpoint)).andExpect(status().isOk());
		} catch (final Exception e) {
			fail();
		}
	}
}
