package br.com.facilit.kanban.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
@Constraint(validatedBy = LocalDateValidator.class)
public  @interface ValidLocalDate {

	String message() default "Data inválida ou fora do formato esperado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String pattern();

}
