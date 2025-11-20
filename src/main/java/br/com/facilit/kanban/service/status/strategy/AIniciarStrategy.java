package br.com.facilit.kanban.service.status.strategy;

import static br.com.facilit.kanban.common.util.PeriodCalculator.calculateRemainingPercentage;
import static br.com.facilit.kanban.model.Status.A_INICIAR;
import static java.util.Objects.isNull;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.StatusStrategy;

@Component
public class AIniciarStrategy implements StatusStrategy {

	@Override
    public boolean applies(final Projeto projeto) {
        return isNull(projeto.getInicioRealizado()) && isNull(projeto.getTerminoRealizado());
    }

    @Override
    public void apply(final Projeto projeto) {
        projeto.setStatus(A_INICIAR);
        projeto.setDiasAtraso(0);
        projeto.setPercentualTempoRestante(calculateRemainingPercentage(projeto.getInicioPrevisto(), projeto.getTerminoPrevisto()));
    }

}
