package br.com.facilit.kanban.service.status.transition.rule;

import static br.com.facilit.kanban.model.Status.ATRASADO;
import static br.com.facilit.kanban.model.Status.EM_ANDAMENTO;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.status.transition.StatusTransitionRule;

@Component
public class EmAndamentoToAtrasadoRule implements StatusTransitionRule {

    @Override
    public Status from() {
        return EM_ANDAMENTO;
    }

    @Override
    public Status to() {
        return ATRASADO;
    }

    @Override
    public void validate(final Projeto projeto) throws StatusTransitionException {
		throw new StatusTransitionException("Não permitido. Remover Início Realizado ou ajustar Início/Término Previsto para a data de hoje");
    }
}
