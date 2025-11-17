package br.com.facilit.kanban.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.model.Secretaria;
import br.com.facilit.kanban.service.ResponsavelService;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

@ExtendWith(MockitoExtension.class)
class UniqueEmailValidatorTest {

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
	private UniqueEmailValidator uniqueEmailValidator;

    @Test
    void isValid_Should_ReturnTrue_When_EmailDoesNotExist() {
    	final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);

        when(responsavelService.findByEmail(anyString())).thenReturn(Optional.empty());

        assertTrue(uniqueEmailValidator.isValid(responsavelUpdateDTO, context));
    }

    @Test
    void isValid_Should_ReturnTrue_When_EmailExistsButBelongsToSameId() {
    	final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);

    	final Secretaria secretaria = new Secretaria(2L, "Secretaria");
    	final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

    	when(responsavelService.findByEmail(anyString())).thenReturn(Optional.of(responsavel));

    	assertTrue(uniqueEmailValidator.isValid(responsavelUpdateDTO, context));
    }

    @Test
    void isValid_Should_ReturnFalse_When_EmailExistsAndBelongsToDifferentId() {
    	final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);

    	final Secretaria secretaria = new Secretaria(2L, "Secretaria");
    	final Responsavel responsavel = new Responsavel(7L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);

    	when(responsavelService.findByEmail(anyString())).thenReturn(Optional.of(responsavel));
    	doNothing().when(context).disableDefaultConstraintViolation();
		when(context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())).thenReturn(builder);
		when(builder.addPropertyNode("email")).thenReturn(nodeBuilderCustomizableContext);

    	assertFalse(uniqueEmailValidator.isValid(responsavelUpdateDTO, context));
    }

    @Test
    void isValid_Should_ReturnTrue_When_ResponsavelIsNull() {
        assertTrue(uniqueEmailValidator.isValid(null, context));
    }

    @Test
    void isValid_Should_ReturnTrue_When_EmailIsNull() {
    	final ResponsavelUpdateDTO responsavelUpdateDTO = new ResponsavelUpdateDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", 5L);

        assertTrue(uniqueEmailValidator.isValid(responsavelUpdateDTO, context));
    }

}
