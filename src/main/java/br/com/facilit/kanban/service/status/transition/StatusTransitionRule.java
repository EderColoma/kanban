package br.com.facilit.kanban.service.status.transition;

import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;

public interface StatusTransitionRule {

	public Status from();
	public Status to();

    public void validate(Projeto projeto) throws StatusTransitionException;

}
