package br.com.facilit.kanban.service.status.transition.rule;

import static br.com.facilit.kanban.model.Status.ATRASADO;
import static br.com.facilit.kanban.model.Status.CONCLUIDO;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.status.transition.StatusTransitionRule;

@Component
public class AtrasadoToConcluidoRule implements StatusTransitionRule {

    @Override
    public Status from() {
        return ATRASADO;
    }

    @Override
    public Status to() {
        return CONCLUIDO;
    }

    @Override
    public void validate(final Projeto projeto) {
        // Sempre permitido.
    }
}
