package br.com.facilit.kanban.service.status.strategy;

import static br.com.facilit.kanban.common.util.PeriodCalculator.calculateRemainingPercentage;
import static br.com.facilit.kanban.model.Status.EM_ANDAMENTO;
import static java.time.LocalDate.now;
import static java.util.Objects.isNull;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.StatusStrategy;

@Component
public class EmAndamentoStrategy implements StatusStrategy {

    @Override
    public boolean applies(final Projeto projeto) {
        return !isNull(projeto.getInicioRealizado()) && projeto.getTerminoPrevisto().isAfter(now()) && isNull(projeto.getTerminoRealizado());
    }

    @Override
    public void apply(final Projeto projeto) {
        projeto.setStatus(EM_ANDAMENTO);
        projeto.setDiasAtraso(0);
        projeto.setPercentualTempoRestante(calculateRemainingPercentage(projeto.getInicioPrevisto(), projeto.getTerminoPrevisto()));
    }
}
