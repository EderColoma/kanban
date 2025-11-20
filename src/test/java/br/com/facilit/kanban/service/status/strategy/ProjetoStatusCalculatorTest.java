package br.com.facilit.kanban.service.status.strategy;

import static br.com.facilit.kanban.model.Status.EM_ANDAMENTO;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.model.Projeto;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProjetoStatusCalculator.class, EmAndamentoStrategy.class})
class ProjetoStatusCalculatorTest {

	@Autowired
	@SuppressWarnings("unused")
	private EmAndamentoStrategy emAndamentoStrategy;

	@Autowired
	private ProjetoStatusCalculator projetoStatusCalculator;

	@Test
	void calculate_Should_ApplyTheMatchingStrategy() {
		final Projeto projeto = new Projeto();
        projeto.setInicioPrevisto(now().minusDays(2));
        projeto.setInicioRealizado(now().minusDays(2));
        projeto.setTerminoPrevisto(now().plusDays(8));
        projeto.setTerminoRealizado(null);

        projetoStatusCalculator.calculate(projeto);

       assertEquals(EM_ANDAMENTO, projeto.getStatus());
       assertEquals(0, projeto.getDiasAtraso());
       assertEquals(80, projeto.getPercentualTempoRestante());
	}

}
