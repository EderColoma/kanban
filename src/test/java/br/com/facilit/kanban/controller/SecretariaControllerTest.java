package br.com.facilit.kanban.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
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

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.facilit.kanban.dto.SecretariaDTO;
import br.com.facilit.kanban.dto.SecretariaUpdateDTO;
import br.com.facilit.kanban.model.Secretaria;
import br.com.facilit.kanban.service.SecretariaService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(SecretariaController.class)
class SecretariaControllerTest {

	@MockitoBean
	private SecretariaService secretariaService;

	@Autowired
	@InjectMocks
	private SecretariaController secretariaController;

	@Autowired
	private MockMvc mockMvc;

	private static final String URL = "/secretaria";

	@Test
	void findById_Should_ReturnASecretaria_When_ASecretariaIsFound() {

		final String endpoint = URL + "/1";

		when(secretariaService.findById(1L)).thenReturn(Optional.of(new SecretariaDTO(1L, "Secretaria")));

		try {
			mockMvc.perform(get(endpoint))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Secretaria"));
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void findById_Should_ReturnNoContent_When_NoSecretariaIsFound() {

		final String endpoint = URL + "/1";

		when(secretariaService.findById(1L)).thenReturn(Optional.empty());

		try {
			mockMvc.perform(get(endpoint)).andExpect(status().isNoContent());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void create_Should_ReturnOk_When_CreatingASecretaria() {
		when(secretariaService.save(any(SecretariaDTO.class))).thenReturn(new Secretaria(1L, "Secretaria"));

		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(new Secretaria(null, "Secretaria"));
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
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(new Secretaria(null, ""));
			mockMvc.perform(post(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnOk_WhenUpdatingASecretaria() {
		when(secretariaService.findById(1L)).thenReturn(Optional.of(new SecretariaDTO(1L, "Secretaria")));
		doNothing().when(secretariaService).update(any(SecretariaUpdateDTO.class));

		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(new SecretariaUpdateDTO(1L, "Secretaria"));
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isOk());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_WhenTheIdIsNull() {
		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(new SecretariaUpdateDTO(null, "Secretaria"));
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void update_Should_ReturnBadRequest_WhenTheNameIsBlank() {
		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(new SecretariaUpdateDTO(1L, ""));
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
		doThrow(new EntityNotFoundException("Secretaria não encontrada")).when(secretariaService).update(any(SecretariaUpdateDTO.class));

		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonRequestBody = objectMapper.writeValueAsString(new SecretariaUpdateDTO(1L, "Secretaria"));
			mockMvc.perform(put(URL)
				   .contentType(APPLICATION_JSON)
				   .content(jsonRequestBody))
				.andExpect(status().isBadRequest());
		} catch (final Exception _) {
			fail();
		}
	}

	@Test
	void delete_Should_ReturnOk_When_DeletingASecretaria() {
		final String endpoint = URL + "/1";

		doNothing().when(secretariaService).delete(1L);

		try {
			mockMvc.perform(delete(endpoint)).andExpect(status().isOk());
		} catch (final Exception _) {
			fail();
		}
	}
}
