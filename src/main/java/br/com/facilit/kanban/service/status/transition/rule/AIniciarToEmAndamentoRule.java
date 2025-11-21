package br.com.facilit.kanban.service.status.transition.rule;

import static br.com.facilit.kanban.model.Status.A_INICIAR;
import static br.com.facilit.kanban.model.Status.EM_ANDAMENTO;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.status.transition.StatusTransitionRule;

@Component
public class AIniciarToEmAndamentoRule implements StatusTransitionRule {

    @Override
    public Status from() {
        return A_INICIAR;
    }

    @Override
    public Status to() {
        return EM_ANDAMENTO;
    }

    @Override
    public void validate(final Projeto projeto) {
        // Sempre permitido.
    }
}
