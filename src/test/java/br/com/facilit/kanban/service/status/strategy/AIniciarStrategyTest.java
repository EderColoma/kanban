package br.com.facilit.kanban.service.status.strategy;

import static br.com.facilit.kanban.model.Status.A_INICIAR;
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
@ContextConfiguration(classes = AIniciarStrategy.class)
class AIniciarStrategyTest {

	@Autowired
	private AIniciarStrategy aIniciarStrategy;

	@Test
	void applies_Should_ReturnTrue_When_InicioRealizadoIsNull_And_TerminoRealizadoIsNull() {
		assertTrue(aIniciarStrategy.applies(new Projeto()));
	}

	@Test
	void applies_Should_ReturnFalse_When_InicioRealizadoIsNotNull() {
		final Projeto projeto = new Projeto();
		projeto.setInicioRealizado(now().minusDays(5));

		assertFalse(aIniciarStrategy.applies(projeto));
	}

	@Test
	void applies_Should_ReturnFalse_When_TerminoRealizadoIsNotNull() {
		final Projeto projeto = new Projeto();
		projeto.setTerminoRealizado(now().minusDays(5));

		assertFalse(aIniciarStrategy.applies(projeto));
	}

	@Test
	void applies_Should_ReturnFalse_When_InicioRealizadoIsNotNull_And_TerminoRealizadoIsNotNull() {
		final Projeto projeto = new Projeto();
		projeto.setInicioRealizado(now().minusDays(15));
		projeto.setTerminoRealizado(now().minusDays(5));

		assertFalse(aIniciarStrategy.applies(projeto));
	}

	@Test
	void apply_Should_SetStatusAIniciar_And_DiasAtrasoZero_AndPercentualTempoRestante() {
		final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(2));
        projeto.setTerminoPrevisto(now().plusDays(8));

		aIniciarStrategy.apply(projeto);

		assertEquals(A_INICIAR, projeto.getStatus());
		assertEquals(0, projeto.getDiasAtraso());
		assertEquals(80, projeto.getPercentualTempoRestante());
	}

}
