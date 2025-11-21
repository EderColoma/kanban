package br.com.facilit.kanban.service.status.transition.rule;

import static br.com.facilit.kanban.model.Status.CONCLUIDO;
import static br.com.facilit.kanban.model.Status.EM_ANDAMENTO;
import static java.time.LocalDate.now;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.status.transition.StatusTransitionRule;

@Component
public class ConcluidoToEmAndamentoRule implements StatusTransitionRule {

    @Override
    public Status from() {
        return CONCLUIDO;
    }

    @Override
    public Status to() {
        return EM_ANDAMENTO;
    }

    @Override
    public void validate(final Projeto projeto) throws StatusTransitionException {
        if (projeto.getTerminoPrevisto() == null) {
            throw new StatusTransitionException("Não é possível retornar para EM_ANDAMENTO — término previsto ausente.");
        }

        final boolean projectWouldBeLate = now().isAfter(projeto.getTerminoPrevisto());

        if (projectWouldBeLate) {
            throw new StatusTransitionException("Não é possível retornar o projeto para EM ANDAMENTO, pois ele ficaria ATRASADO. Ajuste as datas primeiro.");
        }
    }
}