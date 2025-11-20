package br.com.facilit.kanban.service.status.strategy;

import static br.com.facilit.kanban.model.Status.EM_ANDAMENTO;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.model.Projeto;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmAndamentoStrategy.class)
class EmAndamentoStrategyTest {

	@Autowired
	private EmAndamentoStrategy emAndamentoStrategy;

	@Test
    void applies_Should_ReturnTrue_When_InicioRealizadoIsNotNull_And_TerminoPervistoIsAfterToday_And_TerminoRealizadoIsNull() {
        final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(10));
        projeto.setInicioRealizado(now().minusDays(10));
        projeto.setTerminoPrevisto(now().plusDays(5));
        projeto.setTerminoRealizado(null);

        assertTrue(emAndamentoStrategy.applies(projeto));
    }

	@Test
    void applies_Should_ReturnFalse_When_InicioRealizadoIsNull() {
        final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(10));
        projeto.setInicioRealizado(null);
        projeto.setTerminoPrevisto(now().plusDays(5));
        projeto.setTerminoRealizado(null);

        assertFalse(emAndamentoStrategy.applies(projeto));
    }

	@Test
    void applies_Should_ReturnFalse_When_TerminoPrevistoIsBeforeToday() {
        final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(10));
        projeto.setInicioRealizado(now().minusDays(10));
        projeto.setTerminoPrevisto(now().minusDays(5));
        projeto.setTerminoRealizado(null);

        assertFalse(emAndamentoStrategy.applies(projeto));
    }

	@Test
    void applies_Should_ReturnFalse_When_TerminoRealizadoIsNotNull() {
        final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(10));
        projeto.setInicioRealizado(now().minusDays(10));
        projeto.setTerminoPrevisto(now().minusDays(5));
        projeto.setTerminoRealizado(now().minusDays(5));

        assertFalse(emAndamentoStrategy.applies(projeto));
    }

    @Test
    void shouldSetStatusEmAndamento() {
    	 final Projeto projeto = new Projeto();
         projeto.setInicioPrevisto(now().minusDays(2));
         projeto.setInicioRealizado(now().minusDays(2));
         projeto.setTerminoPrevisto(now().plusDays(8));
         projeto.setTerminoRealizado(null);

        emAndamentoStrategy.apply(projeto);

        assertEquals(EM_ANDAMENTO, projeto.getStatus());
        assertEquals(0, projeto.getDiasAtraso());
		assertEquals(80, projeto.getPercentualTempoRestante());
    }

}
