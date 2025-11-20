package br.com.facilit.kanban.service.status.strategy;

import static br.com.facilit.kanban.model.Status.ATRASADO;
import static java.time.LocalDate.now;
import static java.util.Objects.isNull;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.StatusStrategy;

@Component
public class AtrasadoStrategy implements StatusStrategy {

    @Override
    public boolean applies(final Projeto projeto) {
        return isInicioAtrasado(projeto) || isTerminoAtrasado(projeto);
    }

    @Override
    public void apply(final Projeto projeto) {
        projeto.setStatus(ATRASADO);
        projeto.setDiasAtraso(projeto.getTerminoPrevisto().until(now()).getDays());
        projeto.setPercentualTempoRestante(0);
    }

    private boolean isInicioAtrasado(final Projeto projeto) {
    	return projeto.getInicioPrevisto().isBefore(now()) && isNull(projeto.getInicioRealizado());
    }

    private boolean isTerminoAtrasado(final Projeto projeto) {
		return projeto.getTerminoPrevisto().isBefore(now()) && isNull(projeto.getTerminoRealizado());
	}

}
