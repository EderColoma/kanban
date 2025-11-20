package br.com.facilit.kanban.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@ExtendWith(MockitoExtension.class)
class LocalDateValidatorTest {

	@Mock
    private ConstraintValidatorContext context;

	@Autowired
	@InjectMocks
	private LocalDateValidator localDateValidator;

    @Test
    void isValid_Should_ReturnTrue_When_DateIsValid() {
        final ValidLocalDate annotation = createAnnotation("dd-MM-uuuu");
        localDateValidator.initialize(annotation);

        assertTrue(localDateValidator.isValid("30-11-2025", context));
    }

    void isValid_Should_ReturnTrue_When_DateIsNull() {
        final ValidLocalDate annotation = createAnnotation("dd-MM-uuuu");
        localDateValidator.initialize(annotation);

        assertTrue(localDateValidator.isValid(null, context));
    }

    @Test
    void isValid_Should_ReturnFalse_When_DateIsInvalid() {
    	final ValidLocalDate annotation = createAnnotation("dd-MM-uuuu");
    	localDateValidator.initialize(annotation);

        assertFalse(localDateValidator.isValid("31-02-2025", context));
    }

    @Test
    void isValid_Should_ReturnFalse_When_FormatIsInvalid() {
    	final ValidLocalDate annotation = createAnnotation("dd-MM-uuuu");
        localDateValidator.initialize(annotation);

        assertFalse(localDateValidator.isValid("2025/11/20", context));
    }

    private ValidLocalDate createAnnotation(final String pattern) {
        return new ValidLocalDate() {

            @Override
            public String pattern() {
                return pattern;
            }

            @Override
            public String message() {
                return "invalid";
            }

            @Override
            public Class<?>[] groups() {
                return new Class<?>[0];
            }

			@Override
			@SuppressWarnings("unchecked")
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return ValidLocalDate.class;
            }
        };
    }

}
