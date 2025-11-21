package br.com.facilit.kanban.service.status.transition.rule;

import static br.com.facilit.kanban.model.Status.ATRASADO;
import static br.com.facilit.kanban.model.Status.A_INICIAR;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.status.transition.StatusTransitionRule;

@Component
public class AtrasadoToAIniciarRule implements StatusTransitionRule {

    @Override
    public Status from() {
        return ATRASADO;
    }

    @Override
    public Status to() {
        return A_INICIAR;
    }

    @Override
    public void validate(final Projeto projeto) throws StatusTransitionException {
		throw new StatusTransitionException("Não permitido. Remover Início Realizado e ajustar Início/Término Previsto para data > hoje.");
    }
}
