package br.com.facilit.kanban.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.facilit.kanban.dto.request.ResponsavelUpdateDTO;
import br.com.facilit.kanban.model.Secretaria;
import br.com.facilit.kanban.service.SecretariaService;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

@ExtendWith(MockitoExtension.class)
class ExistingSecretariaValidatorTest {

	@Mock
    private SecretariaService secretariaService;

	@Mock
    private ConstraintValidatorContext context;

	@Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

	@Mock
	private NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

	@Autowired
	@InjectMocks
	private ExistingSecretariaValidator existingSecretariaValidator;

	@Test
    void isValid_Should_ReturnTrue_When_ResponsavelIsNull() {
        assertTrue(existingSecretariaValidator.isValid(null, context));
    }

    @Test
    void isValid_Should_ReturnTrue_When_SecretariaIsNull() {
    	final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", null);

        assertTrue(existingSecretariaValidator.isValid(responsavelUpdateDTO, context));
    }

    @Test
    void isValid_Should_ReturnTrue_When_SecretariaExists() {
    	final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);

        when(secretariaService.findById(anyLong())).thenReturn(Optional.of(new Secretaria(5L, "Secretaria")));

        assertTrue(existingSecretariaValidator.isValid(responsavelUpdateDTO, context));
    }

    @Test
    void isValid_Should_ReturnFalse_When_SecretariaDoesNotExist() {
    	final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);

        when(secretariaService.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(context).disableDefaultConstraintViolation();
		when(context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())).thenReturn(builder);
		when(builder.addPropertyNode(anyString())).thenReturn(nodeBuilderCustomizableContext);

        assertFalse(existingSecretariaValidator.isValid(responsavelUpdateDTO, context));
    }
}
