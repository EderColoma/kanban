package br.com.facilit.kanban.validation;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.dto.request.ResponsavelRequestDTO;
import br.com.facilit.kanban.service.SecretariaService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistingSecretariaValidator implements ConstraintValidator<ExistingSecretaria, ResponsavelRequestDTO> {

    private final SecretariaService secretariaService;

    @Override
    public boolean isValid(final ResponsavelRequestDTO responsavelRequestDTO, final ConstraintValidatorContext context) {

        if (responsavelRequestDTO == null) {
			return true;
		}

        if (responsavelRequestDTO.getSecretariaId() == null) {
			return true;
		}

        if (secretariaService.findById(responsavelRequestDTO.getSecretariaId()).isPresent()) {
			return true;
		}

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("secretaria")
                .addConstraintViolation();

        return false;
    }
}