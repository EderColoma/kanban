package br.com.facilit.kanban.service.status.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.service.status.StatusStrategy;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjetoStatusCalculator {

    private final List<StatusStrategy> strategies;

    public void calculate(final Projeto projeto) {
        strategies.stream()
                .filter(strategy -> strategy.applies(projeto))
                .findFirst()
                .ifPresent(strategy -> strategy.apply(projeto));
    }
}