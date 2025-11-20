package br.com.facilit.kanban.validation;

import static java.time.format.ResolverStyle.STRICT;
import static java.util.Objects.isNull;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeriodValidator implements ConstraintValidator<Period, Object> {

	private String startField;
    private String endField;
    private String message;
    private DateTimeFormatter formatter;

    @Override
    public void initialize(final Period period) {
    	startField = period.startField();
        endField = period.endField();
        message = period.message();
        formatter = DateTimeFormatter.ofPattern(period.pattern()).withResolverStyle(STRICT);
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        try {

        	final Object objStartDate = getFieldValue(object, startField);
        	final Object objEndDate = getFieldValue(object, endField);

			if (isNull(objStartDate) || isNull(objEndDate)) {
				return true;
			}

            final LocalDate startDate = LocalDate.parse(objStartDate.toString(), formatter);
            final LocalDate endDate = LocalDate.parse(objEndDate.toString(), formatter);

            if (endDate.isBefore(startDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                       .addPropertyNode(endField)
                       .addConstraintViolation();
                return false;
            }

        } catch (final Exception _) {
            return false;
        }

        return true;
    }

    private Object getFieldValue(final Object object, final String fieldName) throws Exception {
        final Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

}
