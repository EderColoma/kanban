package br.com.facilit.kanban.service.status.strategy;

import static br.com.facilit.kanban.model.Status.ATRASADO;
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
@ContextConfiguration(classes = AtrasadoStrategy.class)
class AtrasadoStrategyTest {

	@Autowired
	private AtrasadoStrategy atrasadoStrategy;

	@Test
    void applies_Should_ReturnTrue_When_InicioPrevistoIsBeforeToday_And_InicioRealizadoIsNull() {
        final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(2));
        projeto.setInicioRealizado(null);
        projeto.setTerminoPrevisto(now().plusDays(2));
        projeto.setTerminoRealizado(now().plusDays(2));

        assertTrue(atrasadoStrategy.applies(projeto));
    }

	@Test
    void applies_Should_ReturnTrue_When_TerminoPrevistoIsBeforeToday_And_TerminoRealizadoIsNull() {
        final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(2));
        projeto.setInicioRealizado(now().minusDays(2));
        projeto.setTerminoPrevisto(now().minusDays(2));
        projeto.setTerminoRealizado(null);

        assertTrue(atrasadoStrategy.applies(projeto));
    }

    @Test
    void applies_Should_ReturnFalse_When_InicioRealizadoIsNotNull_And_TerminoRealizadoIsNotNull() {
        final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(2));
        projeto.setInicioRealizado(now().minusDays(2));
        projeto.setTerminoPrevisto(now().plusDays(2));
        projeto.setTerminoRealizado(now().plusDays(2));

        assertFalse(atrasadoStrategy.applies(projeto));
    }

    @Test
    void applies_Should_ReturnFalse_When_TerminoPrevistoIsToday() {
        final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(2));
        projeto.setInicioRealizado(now().minusDays(2));
        projeto.setTerminoPrevisto(now());
        projeto.setTerminoRealizado(null);

        assertFalse(atrasadoStrategy.applies(projeto));
    }

    @Test
    void applies_Should_ReturnFalse_When_InicioPrevistoIsToday() {
        final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now());
        projeto.setInicioRealizado(null);
        projeto.setTerminoPrevisto(now().plusDays(2));
        projeto.setTerminoRealizado(null);

        assertFalse(atrasadoStrategy.applies(projeto));
    }

    @Test
    void apply_Should_SetStatusAtrasado_And_DiasAtraso_AndPercentualTempoRestante() {
    	final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(2));
        projeto.setInicioRealizado(now().minusDays(2));
        projeto.setTerminoPrevisto(now().minusDays(2));
        projeto.setTerminoRealizado(null);

        atrasadoStrategy.apply(projeto);

        assertEquals(ATRASADO, projeto.getStatus());
        assertEquals(2, projeto.getDiasAtraso());
        assertEquals(0, projeto.getPercentualTempoRestante());
    }

}
