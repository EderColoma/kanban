package br.com.facilit.kanban.service.status.transition.rule;

import static br.com.facilit.kanban.model.Status.ATRASADO;
import static br.com.facilit.kanban.model.Status.A_INICIAR;
import static java.time.LocalDate.now;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.status.transition.StatusTransitionRule;

@Component
public class AIniciarToAtrasadoRule implements StatusTransitionRule {

    @Override
    public Status from() {
        return A_INICIAR;
    }

    @Override
    public Status to() {
        return ATRASADO;
    }

    @Override
    public void validate(final Projeto projeto) throws StatusTransitionException {

        if (projeto.getInicioPrevisto() == null) {
			throw new StatusTransitionException("Início previsto ausente — não é possível marcar como ATRASADO.");
		}

        if (now().isBefore(projeto.getInicioPrevisto())) {
			throw new StatusTransitionException("Não é possível marcar como ATRASADO antes do início previsto.");
		}
    }
}
