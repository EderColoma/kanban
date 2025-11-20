package br.com.facilit.kanban.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.service.ResponsavelService;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

@ExtendWith(MockitoExtension.class)
class ExistingResponsavelValidatorTest {

    @Mock
    private ResponsavelService responsavelService;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @Mock
	private NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

    @Autowired
    @InjectMocks
    private ExistingResponsavelValidator existingResponsavelValidator;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    @Test
    void isValid_Should_ReturnTrue_When_ResponsaveisIsNull() {
        assertTrue(existingResponsavelValidator.isValid(null, context));
    }

    @Test
    void isValid_Should_ReturnTrue_When_ResponsaveisIsEmpty() {
        assertTrue(existingResponsavelValidator.isValid(Set.of(), context));
    }

    @Test
    void isValid_Should_ReturnTrue_WhenAllEResponsaveisExist() {
        when(responsavelService.findById(1L)).thenReturn(Optional.of(new Responsavel()));
        when(responsavelService.findById(2L)).thenReturn(Optional.of(new Responsavel()));

        final boolean result = existingResponsavelValidator.isValid(Set.of(1L, 2L), context);
        assertTrue(result);
    }

    @Test
    void isValid_Should_ReturnFalse_When_AtLeastOneResponsavelDoesNotExist() {
        when(responsavelService.findById(1L)).thenReturn(Optional.of(new Responsavel()));
        when(responsavelService.findById(99L)).thenReturn(Optional.empty());
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);

        final boolean result = existingResponsavelValidator.isValid(Set.of(1L, 99L), context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Responsáveis não encontrados: [99]");
    }

    @Test
    void isValid_Should_ReturnFalse_When_AllResponsaveisDoNotExist() {
		when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(responsavelService.findById(anyLong())).thenReturn(Optional.empty());

        final boolean result = existingResponsavelValidator.isValid(Set.of(10L, 20L), context);


        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Responsáveis não encontrados: [10, 20]");
    }

}
