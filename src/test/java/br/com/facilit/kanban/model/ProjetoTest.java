package br.com.facilit.kanban.model;

import static br.com.facilit.kanban.model.Status.CONCLUIDO;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ProjetoTest {

	@Test
    void constructor_Should_AssignTheFields() {
        final LocalDate inicioPrevisto = LocalDate.of(2025, 1, 10);
        final LocalDate terminoPrevisto = LocalDate.of(2025, 3, 20);
        final LocalDate inicioRealizado = LocalDate.of(2025, 1, 11);
        final LocalDate terminoRealizado = LocalDate.of(2025, 3, 22);

        final Projeto projeto = new Projeto(
        		1L,
        		"Projeto",
        		CONCLUIDO,
                inicioPrevisto,
                terminoPrevisto,
                inicioRealizado,
                terminoRealizado,
                0,
                10,
                Set.of(new Responsavel())
        );

        assertEquals(1L, projeto.getId());
        assertEquals("Projeto", projeto.getNome());
        assertEquals(CONCLUIDO, projeto.getStatus());
        assertEquals(inicioPrevisto, projeto.getInicioPrevisto());
        assertEquals(terminoPrevisto, projeto.getTerminoPrevisto());
        assertEquals(inicioRealizado, projeto.getInicioRealizado());
        assertEquals(terminoRealizado, projeto.getTerminoRealizado());
        assertEquals(0, projeto.getDiasAtraso());
        assertEquals(10, projeto.getPercentualTempoRestante());
        assertEquals(1, projeto.getResponsaveis().size());
    }

}
