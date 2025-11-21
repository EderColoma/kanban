package br.com.facilit.kanban.service.transition.rule;

import static br.com.facilit.kanban.model.Status.EM_ANDAMENTO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.transition.rule.EmAndamentoToAiniciarRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmAndamentoToAiniciarRule.class)
class EmAndamentoToAIniciarRuleTest {

	@Autowired
	private EmAndamentoToAiniciarRule emAndamentoToAiniciarRule;

	@Test
    void validate_Should_AllowTransition() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(EM_ANDAMENTO);

        assertDoesNotThrow(() -> emAndamentoToAiniciarRule.validate(projeto));
    }

}
