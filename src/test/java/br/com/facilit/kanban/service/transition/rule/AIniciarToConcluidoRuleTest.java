package br.com.facilit.kanban.service.transition.rule;

import static br.com.facilit.kanban.model.Status.A_INICIAR;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.transition.rule.AIniciarToConcluidoRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AIniciarToConcluidoRule.class)
class AIniciarToConcluidoRuleTest {

	@Autowired
	private AIniciarToConcluidoRule aIniciarToConcluidoRule;

	@Test
    void validate_Should_AllowTransition() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(A_INICIAR);

        assertDoesNotThrow(() -> aIniciarToConcluidoRule.validate(projeto));
    }

}
