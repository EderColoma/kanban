package br.com.facilit.kanban.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(Period.List.class)
@Constraint(validatedBy = PeriodValidator.class)
public @interface Period {

	String message() default "Intervalo de datas inválido";

    String startField();
    String endField();
    String pattern();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
    	Period[] value();
    }

}
