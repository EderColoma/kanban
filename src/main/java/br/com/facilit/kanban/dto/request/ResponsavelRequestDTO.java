package br.com.facilit.kanban.dto.request;

import br.com.facilit.kanban.dto.shared.Mailable;
import br.com.facilit.kanban.validation.ExistingSecretaria;
import br.com.facilit.kanban.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@UniqueEmail
@ExistingSecretaria
@NoArgsConstructor
@AllArgsConstructor
public abstract class ResponsavelRequestDTO implements Mailable {

	@NotBlank(message = "Nome é obrigatório")
	@Size(max = 255, message = "Nome não pode ter mais de 255 caracteres")
	protected String nome;

	@Email(message = "Email inválido")
	@NotBlank(message = "Email é obrigatório")
	@Size(max = 255, message = "Email não pode ter mais de 255 caracteres")
	protected String email;

	@Size(max = 255, message = "Cargo não pode ter mais de 255 caracteres")
	protected String cargo;

	protected Long secretariaId;

}
