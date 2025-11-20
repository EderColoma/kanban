package br.com.facilit.kanban.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SecretariaTest {

	@Test
    void constructor_Should_AssignTheFields() {
		final Secretaria secretaria = new Secretaria(1L, "Secretaria");

		assertEquals(Long.valueOf(1), secretaria.getId());
		assertEquals("Secretaria", secretaria.getNome());
	}

}
