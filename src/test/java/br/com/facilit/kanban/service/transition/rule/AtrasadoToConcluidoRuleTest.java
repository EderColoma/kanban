package br.com.facilit.kanban.service.transition.rule;

import static br.com.facilit.kanban.model.Status.ATRASADO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.transition.rule.AtrasadoToConcluidoRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AtrasadoToConcluidoRule.class)
class AtrasadoToConcluidoRuleTest {

	@Autowired
	private AtrasadoToConcluidoRule atrasadoToConcluidoRule;

	@Test
    void validate_Should_AllowTransition() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(ATRASADO);

        assertDoesNotThrow(() -> atrasadoToConcluidoRule.validate(projeto));
    }

}
