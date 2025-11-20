package br.com.facilit.kanban.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

import java.util.List;
import java.util.Optional;

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

import br.com.facilit.kanban.dto.request.ResponsavelCreationDTO;
import br.com.facilit.kanban.dto.request.ResponsavelUpdateDTO;
import br.com.facilit.kanban.dto.response.ResponsavelDTO;
import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.model.Secretaria;
import br.com.facilit.kanban.service.ResponsavelService;
import br.com.facilit.kanban.service.SecretariaService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ResponsavelController.class)
class ResponsavelControllerTest {

	@MockitoBean
	private ResponsavelService responsavelService;

	@MockitoBean
	private SecretariaService secretariaService;

	@Autowired
	@InjectMocks
	private ResponsavelController responsavelController;

	@Autowired
	private MockMvc mockMvc;

	private static final String URL = "/responsavel";

	@Test
	void findById_Should_ReturnResponsavel_When_ResponsavelIsFound() {

		final String endpoint = URL + "/1";

		final SecretariaDTO secretariaDTO = new SecretariaDTO(2L, "Secretaria");
        final ResponsavelDTO responsavelDTO = new ResponsavelDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretariaDTO);

		when(responsavelService.findDTOById(1L)).thenReturn(Optional.of(responsavelDTO));

		try {
			mockMvc.perform(get(endpoint))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Responsavel"))
				.andExpect(jsonPath("$.email").value("responsavel@facilit.com"))
				.andExpect(jsonPath("$.cargo").value("Product Owner"))
				.andExpect(jsonPath("$.secretaria.id").value(2))
				.andExpect(jsonPath("$.secretaria.nome").value("Secretaria"));
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void findById_Should_ReturnNoContent_When_NoResponsavelIsFound() {

		final String endpoint = URL + "/1";

		when(responsavelService.findDTOById(1L)).thenReturn(Optional.empty());

		try {
			mockMvc.perform(get(endpoint)).andExpect(status().isNoContent());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
    void findAll_ShouldReturnAPageOfResponsaveis() {
		final SecretariaDTO secretariaDTO = new SecretariaDTO(2L, "Secretaria");
        final ResponsavelDTO responsavelDTO = new ResponsavelDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretariaDTO);

        final Page<ResponsavelDTO> page = new PageImpl<>(List.of(responsavelDTO));

        when(responsavelService.findAll(PageRequest.of(0, 10))).thenReturn(page);

        try {
	        mockMvc.perform(get(URL)
	                        .param("page", "0")
	                        .param("size", "10"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.content[0].id").value(1L))
	                .andExpect(jsonPath("$.content[0].nome").value("Responsavel"))
					.andExpect(jsonPath("$.content[0].email").value("responsavel@facilit.com"))
					.andExpect(jsonPath("$.content[0].cargo").value("Product Owner"))
					.andExpect(jsonPath("$.content[0].secretaria.id").value(2))
					.andExpect(jsonPath("$.content[0].secretaria.nome").value("Secretaria"))
	                .andExpect(jsonPath("$.totalElements").value(1));
        } catch (final Exception _) {
			fail();
		}
    }

	@Test
	void create_Should_ReturnOk_When_CreatingAResponsavel() {
		final Secretaria secretaria = new Secretaria(2L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

		when(responsavelService.save(any(ResponsavelCreationDTO.class))).thenReturn(responsavel);
		when(secretariaService.findById(any(Long.class))).thenReturn(Optional.of(secretaria));

		try {
			final ResponsavelCreationDTO responsavelCreationDTO = new ResponsavelCreationDTO("Responsavel", "responsavel@facilit.com", "Product Owner", 2L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelCreationDTO);
			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isOk());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_TheNameIsBlank() {
		try {
			final ResponsavelCreationDTO responsavelCreationDTO = new ResponsavelCreationDTO("", "responsavel@facilit.com", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelCreationDTO);
			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_TheEmailIsBlank() {
		try {
			final ResponsavelCreationDTO responsavelCreationDTO = new ResponsavelCreationDTO("Responsavel", "", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelCreationDTO);
			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_TheEmailIsInvalid() {
		try {
			final ResponsavelCreationDTO responsavelCreationDTO = new ResponsavelCreationDTO("Responsavel", "responsavel", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelCreationDTO);
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
	void create_Should_ReturnBadRequest_When_TheEmailIsNotUnique() {
		try {
			final Secretaria secretaria = new Secretaria(2L, "Secretaria");
			final Responsavel responsavel = new Responsavel(7L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

			when(responsavelService.findByEmail(anyString())).thenReturn(Optional.of(responsavel));

			final ResponsavelCreationDTO responsavelCreationDTO = new ResponsavelCreationDTO("Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelCreationDTO);
			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void create_Should_ReturnBadRequest_When_TheSecretariaDoesNotExist() {
		doNothing().when(responsavelService).update(any(ResponsavelUpdateDTO.class));
		when(secretariaService.findById(anyLong())).thenReturn(Optional.empty());

		try {
			final ResponsavelCreationDTO responsavelCreationDTO = new ResponsavelCreationDTO("Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelCreationDTO);
			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnOk_When_UpdatingAResponsavel() {
		doNothing().when(responsavelService).update(any(ResponsavelUpdateDTO.class));
		when(secretariaService.findById(any(Long.class))).thenReturn(Optional.of(new Secretaria(5L, "Secretaria")));

		try {
			final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelUpdateDTO);
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isOk());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheResponsavelDoesNotExist() {
		doThrow(new EntityNotFoundException("Responsável não encontrado")).when(responsavelService).update(any(ResponsavelUpdateDTO.class));
		when(secretariaService.findById(any(Long.class))).thenReturn(Optional.of(new Secretaria(5L, "Secretaria")));

		try {
			final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelUpdateDTO);
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheIdIsNull() {
		try {
			final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(null, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelUpdateDTO);
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheNomeIsBlank() {
		try {
			final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "", "responsavel@facilit.com", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelUpdateDTO);
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheEmailIsBlank() {
		try {
			final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelUpdateDTO);
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheEmailIsInvalid() {
		try {
			final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelUpdateDTO);
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheEmailIsNotUnique() {
		try {
			final Secretaria secretaria = new Secretaria(2L, "Secretaria");
			final Responsavel responsavel = new Responsavel(7L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

			when(responsavelService.findByEmail(anyString())).thenReturn(Optional.of(responsavel));

			final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelUpdateDTO);
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_When_TheSecretariaDoesNotExist() {
		doNothing().when(responsavelService).update(any(ResponsavelUpdateDTO.class));
		when(secretariaService.findById(anyLong())).thenReturn(Optional.empty());

		try {
			final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(responsavelUpdateDTO);
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void delete_Should_ReturnOk_When_DeletingAResponsavel() {
		final String endpoint = URL + "/1";

		doNothing().when(responsavelService).delete(1L);

		try {
			mockMvc.perform(delete(endpoint)).andExpect(status().isOk());
		} catch (final Exception _) {
			fail();
		}
	}
}
