package br.com.facilit.kanban.service.status.strategy;

import static br.com.facilit.kanban.model.Status.CONCLUIDO;
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
@ContextConfiguration(classes = ConcluidoStrategy.class)
class ConcluidoStrategyTest {

	@Autowired
	private ConcluidoStrategy concluidoStrategy;

	@Test
    void applies_Should_ReturnTrue_When_TerminoRealizadoIsNotNull() {
        final Projeto projeto = new Projeto();
        projeto.setTerminoRealizado(now());

        assertTrue(concluidoStrategy.applies(projeto));
    }

    @Test
    void applies_Should_ReturnFalse_When_TerminoRealizadoIsNull() {
        assertFalse(concluidoStrategy.applies(new Projeto()));
    }

    @Test
    void apply_Should_SetStatusConcluido_And_DiasAtrasoZero_AndPercentualTempoRestanteZero() {
        final Projeto projeto = new Projeto();
        projeto.setTerminoRealizado(now());
        projeto.setDiasAtraso(5);
        projeto.setPercentualTempoRestante(20);

        concluidoStrategy.apply(projeto);

        assertEquals(CONCLUIDO, projeto.getStatus());
        assertEquals(0, projeto.getDiasAtraso());
        assertEquals(0, projeto.getPercentualTempoRestante());
    }

}
