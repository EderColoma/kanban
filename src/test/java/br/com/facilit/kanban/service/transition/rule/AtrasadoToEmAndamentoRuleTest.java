package br.com.facilit.kanban.service.transition.rule;

import static br.com.facilit.kanban.model.Status.ATRASADO;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.transition.rule.AtrasadoToEmAndamentoRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AtrasadoToEmAndamentoRule.class)
class AtrasadoToEmAndamentoRuleTest {

	@Autowired
	private AtrasadoToEmAndamentoRule atrasadoToEmAndamentoRule;

    @Test
    void validate_Should_NeverAllow() {
        final Projeto projeto = new Projeto();
        projeto.setStatus(ATRASADO);

        assertThrows(StatusTransitionException.class, () -> atrasadoToEmAndamentoRule.validate(projeto));
    }

}
