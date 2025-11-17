package br.com.facilit.kanban.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ExistingSecretariaValidator.class)
public @interface ExistingSecretaria {

    String message() default "A secretaria informada não existe.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
