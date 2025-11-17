package br.com.facilit.kanban.validation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.facilit.kanban.dto.shared.Mailable;
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.service.ResponsavelService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Mailable> {

	private final ResponsavelService responsavelService;

    @Override
    public boolean isValid(final Mailable mailable, final ConstraintValidatorContext context) {
        if (mailable == null || mailable.getEmail() == null) {
            return true;
        }

        final Optional<Responsavel> existingResponsavel = responsavelService.findByEmail(mailable.getEmail());

        if (existingResponsavel.isEmpty()) {
            return true;
        }

        if (isSameResponsavel(mailable, existingResponsavel.get())) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("email")
                .addConstraintViolation();

        return false;
    }

    private boolean isSameResponsavel(final Mailable mailable, final Responsavel existingResponsavel) {
    	return mailable.getId() != null && existingResponsavel.getId().equals(mailable.getId());
    }
}
