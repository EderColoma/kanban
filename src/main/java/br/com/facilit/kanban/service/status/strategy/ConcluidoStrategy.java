package br.com.facilit.kanban.service.status.strategy;

import static br.com.facilit.kanban.model.Status.CONCLUIDO;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.StatusStrategy;

@Component
public class ConcluidoStrategy implements StatusStrategy {

    @Override
    public boolean applies(final Projeto projeto) {
        return projeto.getTerminoRealizado() != null;
    }

    @Override
    public void apply(final Projeto projeto) {
        projeto.setStatus(CONCLUIDO);
        projeto.setDiasAtraso(0);
        projeto.setPercentualTempoRestante(0);
    }
}