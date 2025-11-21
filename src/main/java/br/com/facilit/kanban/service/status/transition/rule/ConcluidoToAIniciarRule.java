package br.com.facilit.kanban.service.status.transition.rule;

import static br.com.facilit.kanban.model.Status.A_INICIAR;
import static br.com.facilit.kanban.model.Status.CONCLUIDO;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.status.transition.StatusTransitionRule;

public class ConcluidoToAIniciarRule implements StatusTransitionRule {

    @Override
    public Status from() {
        return CONCLUIDO;
    }

    @Override
    public Status to() {
        return A_INICIAR;
    }

    @Override
    public void validate(final Projeto projeto) throws StatusTransitionException {
		throw new StatusTransitionException("Não permitido. Remover Término Realizado e ajustar Início/Término Previsto para data > hoje");
    }

}
