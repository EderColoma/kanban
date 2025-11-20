package br.com.facilit.kanban.validation;

import static java.util.Objects.isNull;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.common.util.DateValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class LocalDateValidator implements ConstraintValidator<ValidLocalDate, String> {

	private String pattern;

    @Override
    public void initialize(final ValidLocalDate annotation) {
        pattern = annotation.pattern();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return isNull(value) || DateValidator.isValid(value, pattern);
    }

}
