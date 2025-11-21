package br.com.facilit.kanban.service.transition.rule;

import static br.com.facilit.kanban.model.Status.A_INICIAR;
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
import br.com.facilit.kanban.service.status.transition.rule.AIniciarToAtrasadoRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AIniciarToAtrasadoRule.class)
class AIniciarToAtrasadoRuleTest {

	@Autowired
	private AIniciarToAtrasadoRule aIniciarToAtrasadoRule;

	@Test
    void validate_Should_NotAllow_When_InicioPrevistoIsNull() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(A_INICIAR);

        assertThrows(StatusTransitionException.class, () -> aIniciarToAtrasadoRule.validate(projeto));
    }

    @Test
    void validate_Should_NotAllow_When_TodayIsBeforeInicioPrevisto() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(A_INICIAR);
        projeto.setInicioPrevisto(now().plusDays(5));

        assertThrows(StatusTransitionException.class, () -> aIniciarToAtrasadoRule.validate(projeto));
    }

    @Test
    void validate_Should_Allow_When_TodayIAfterInicioPrevisto() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(A_INICIAR);
        projeto.setInicioPrevisto(now().minusDays(1));

        assertDoesNotThrow(() -> aIniciarToAtrasadoRule.validate(projeto));
    }

}
