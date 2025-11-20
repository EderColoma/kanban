package br.com.facilit.kanban.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ResponsavelTest {

	@Test
    void constructor_Should_AssignTheFields() {
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", new Secretaria());

		assertEquals(Long.valueOf(1), responsavel.getId());
		assertEquals("Responsavel", responsavel.getNome());
		assertEquals("responsavel@facilit.com", responsavel.getEmail());
		assertEquals("Product Owner", responsavel.getCargo());
		assertNotNull(responsavel.getSecretaria());
	}

}
