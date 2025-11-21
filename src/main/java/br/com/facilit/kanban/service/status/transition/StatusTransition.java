package br.com.facilit.kanban.service.status.transition;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;

@Component
public class StatusTransition {

	private final Map<Status, Map<Status, StatusTransitionRule>> statusTable = new EnumMap<>(Status.class);

    @SuppressWarnings("unused")
	public StatusTransition(final List<StatusTransitionRule> rules) {
        for (final StatusTransitionRule rule : rules) {
        	statusTable.computeIfAbsent(rule.from(), k -> new EnumMap<>(Status.class)).put(rule.to(), rule);
        }
    }

    public void validate(final Projeto projeto, final Status status) throws StatusTransitionException {
        if (projeto.getStatus() == status) {
			return;
		}

        final var nextStatus = statusTable.get(projeto.getStatus());
        nextStatus.get(status).validate(projeto);
    }

}
