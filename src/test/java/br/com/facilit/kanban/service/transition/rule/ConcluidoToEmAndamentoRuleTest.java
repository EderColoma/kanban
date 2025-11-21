package br.com.facilit.kanban.service.transition.rule;

import static br.com.facilit.kanban.model.Status.CONCLUIDO;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.transition.rule.ConcluidoToEmAndamentoRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConcluidoToEmAndamentoRule.class)
class ConcluidoToEmAndamentoRuleTest {

	@Autowired
	private ConcluidoToEmAndamentoRule concluidoToEmAndamentoRule;

	@Test
    void validate_Should_NOtAllow_WhenTerminoPrevistoIsNull() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(CONCLUIDO);

        assertThrows(StatusTransitionException.class, () -> concluidoToEmAndamentoRule.validate(projeto));
    }

    @Test
    void validate_Should_NotAllow_When_ProjectWouldBecomeLate() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(CONCLUIDO);
        projeto.setTerminoPrevisto(now().minusDays(1));

        assertThrows(StatusTransitionException.class, () -> concluidoToEmAndamentoRule.validate(projeto));
    }

    @Test
    void validate_Should_Allow_When_ProjectWouldNotBecomeLate() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(CONCLUIDO);
        projeto.setTerminoPrevisto(now().plusDays(5));

        assertDoesNotThrow(() -> concluidoToEmAndamentoRule.validate(projeto));
    }

}
