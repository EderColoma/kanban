package br.com.facilit.kanban.service.status.transition.rule;

import static br.com.facilit.kanban.model.Status.ATRASADO;
import static br.com.facilit.kanban.model.Status.CONCLUIDO;
import static java.time.LocalDate.now;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.status.transition.StatusTransitionRule;

@Component
public class ConcluidoToAtrasadoRule implements StatusTransitionRule {

    @Override
    public Status from() {
        return CONCLUIDO;
    }

    @Override
    public Status to() {
        return ATRASADO;
    }

    @Override
    public void validate(final Projeto projeto) throws StatusTransitionException {
    	if (projeto.getTerminoPrevisto() == null) {
            throw new StatusTransitionException("Não é possível mover para ATRASADO — término previsto ausente.");
        }

        final boolean projectWouldBeLate = now().isAfter(projeto.getTerminoPrevisto());

        if (!projectWouldBeLate) {
            throw new StatusTransitionException("Não é possível mover para ATRASADO. Ajuste as datas primeiro.");
        }
    }
}