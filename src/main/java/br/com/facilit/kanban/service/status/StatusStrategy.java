package br.com.facilit.kanban.service.status;

import br.com.facilit.kanban.model.Projeto;

public interface StatusStrategy {
	boolean applies(Projeto projeto);
	void apply(Projeto projeto);
}
