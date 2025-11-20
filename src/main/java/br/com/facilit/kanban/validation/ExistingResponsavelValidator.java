package br.com.facilit.kanban.validation;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.service.ResponsavelService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistingResponsavelValidator implements ConstraintValidator<ExistingResponsavel, Set<Long>> {

    private final ResponsavelService responsavelService;

    @Override
    public boolean isValid(final Set<Long> ids, final ConstraintValidatorContext context) {
        if (isNull(ids) || ids.isEmpty()) {
            return true;
        }

        final List<Long> notFoundResponsaveis = new ArrayList<>();
        ids.forEach(id -> {
        	if (isNull(id) || responsavelService.findById(id).isEmpty()) {
            	notFoundResponsaveis.add(id);
            }
        });

        if (!notFoundResponsaveis.isEmpty()) {
        	if(notFoundResponsaveis.contains(null)) {
        		notFoundResponsaveis.remove(null);
        		notFoundResponsaveis.sort(null);
        		notFoundResponsaveis.add(null);
        	} else {
        		notFoundResponsaveis.sort(null);
        	}

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Responsáveis não encontrados: " + notFoundResponsaveis).addConstraintViolation();

            return false;
        }

        return true;
    }

}
