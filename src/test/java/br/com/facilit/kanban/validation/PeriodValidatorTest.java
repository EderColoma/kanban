package br.com.facilit.kanban.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.facilit.kanban.dto.request.ProjetoCreationDTO;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import jakarta.validation.Payload;

@ExtendWith(MockitoExtension.class)
class PeriodValidatorTest {

	@Mock
    private ConstraintValidatorContext context;

	@Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

	@Mock
	private NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

	@Autowired
	@InjectMocks
	private PeriodValidator periodValidator;

	private void initValidator(final String start, final String end, final String message, final String pattern) {
        final Period annotation = new Period() {
            @Override
            public String startField() { return start; }

            @Override
            public String endField() { return end; }

            @Override
            public String message() { return message; }

            @Override
            public String pattern() { return pattern; }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Period.class;
            }

            @Override
            public Class<?>[] groups() { return new Class[0]; }

			@Override
			@SuppressWarnings("unchecked")
            public Class<? extends Payload>[] payload() { return new Class[0]; }
        };

        periodValidator.initialize(annotation);
    }

	@Test
    void isValid_Should_ReturnTrue_When_TheStartOfTherPeriodIsBeforeTheEnd() {
        initValidator("inicioPrevisto", "terminoPrevisto", "erro", "dd-MM-uuuu");

        final ProjetoCreationDTO projetoCreationDTO = ProjetoCreationDTO.builder()
                .inicioPrevisto("01-01-2025")
                .terminoPrevisto("10-01-2025")
                .build();

        assertTrue(periodValidator.isValid(projetoCreationDTO, context));
    }

	@Test
    void isValid_Should_ReturnFalse_When_TheStartOfTherPeriodIsAfterTheEnd() {
        initValidator("inicioPrevisto", "terminoPrevisto", "Término previsto não pode ser antes do início previsto", "dd-MM-uuuu");

        final ProjetoCreationDTO projetoCreationDTO = ProjetoCreationDTO.builder()
                .inicioPrevisto("10-01-2024")
                .terminoPrevisto("01-01-2024")
                .build();

        assertFalse(periodValidator.isValid(projetoCreationDTO, context));
        verify(context).buildConstraintViolationWithTemplate("Término previsto não pode ser antes do início previsto");
    }

	@Test
    void isValid_Should_ReturnTrue_When_StartDateIsNull() {
        initValidator("inicioPrevisto", "terminoPrevisto", "erro", "dd-MM-uuuu");

        final ProjetoCreationDTO projetoCreationDTO = ProjetoCreationDTO.builder()
                .inicioPrevisto(null)
                .terminoPrevisto("01-01-2024")
                .build();

        assertTrue(periodValidator.isValid(projetoCreationDTO, context));
    }

    @Test
    void isValid_Should_ReturnTrue_When_EndDateIsNull() {
        initValidator("inicioPrevisto", "terminoPrevisto", "erro", "dd-MM-uuuu");

        final ProjetoCreationDTO projetoCreationDTO = ProjetoCreationDTO.builder()
                .inicioPrevisto("01-01-2024")
                .terminoPrevisto(null)
                .build();

        assertTrue(periodValidator.isValid(projetoCreationDTO, context));
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    void isValid_Should_ReturnFalse_When_AFieldNameIsInvalid() {
        initValidator("inicioInexistente", "terminoPrevisto", "erro", "dd-MM-uuuu");

        final ProjetoCreationDTO projetoCreationDTO = ProjetoCreationDTO.builder()
                .terminoPrevisto("01-01-2024")
                .build();

        assertFalse(periodValidator.isValid(projetoCreationDTO, context));
    }

}
