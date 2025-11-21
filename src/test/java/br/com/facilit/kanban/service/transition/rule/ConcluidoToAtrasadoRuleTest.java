package br.com.facilit.kanban.service.transition.rule;

import static br.com.facilit.kanban.model.Status.CONCLUIDO;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.transition.rule.ConcluidoToAtrasadoRule;

class ConcluidoToAtrasadoRuleTest {

	private final ConcluidoToAtrasadoRule rule = new ConcluidoToAtrasadoRule();

    @Test
    void validate_Should_NotAllow_When_TerminoPrevistoIsNull() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(CONCLUIDO);

        assertThrows(StatusTransitionException.class, () -> rule.validate(projeto));
    }

    @Test
    void validate_Should_NotAllow_When_ProjectWouldNotBeLate() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(CONCLUIDO);
        projeto.setTerminoPrevisto(now().plusDays(3));

        assertThrows(StatusTransitionException.class, () -> rule.validate(projeto));
    }

    @Test
    void validate_Should_Allow_WhenProjectWouldBeLate() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(CONCLUIDO);
        projeto.setTerminoPrevisto(now().minusDays(3));

        assertDoesNotThrow(() -> rule.validate(projeto));
    }

}
