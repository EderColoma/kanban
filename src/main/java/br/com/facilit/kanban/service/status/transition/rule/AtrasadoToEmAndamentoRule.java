package br.com.facilit.kanban.service.status.transition.rule;

import static br.com.facilit.kanban.model.Status.ATRASADO;
import static br.com.facilit.kanban.model.Status.EM_ANDAMENTO;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.status.transition.StatusTransitionRule;

public class AtrasadoToEmAndamentoRule implements StatusTransitionRule {

    @Override
    public Status from() {
        return ATRASADO;
    }

    @Override
    public Status to() {
        return EM_ANDAMENTO;
    }

    @Override
    public void validate(final Projeto projeto) throws StatusTransitionException {
		throw new StatusTransitionException("Não permitido. Ajustar Início/Término Previsto para data > hoje");
    }
}
