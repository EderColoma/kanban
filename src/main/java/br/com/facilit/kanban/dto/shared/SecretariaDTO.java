package br.com.facilit.kanban.dto.shared;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretariaDTO {

	private Long id;

	@NotBlank(message = "Nome é obrigatório")
	@Size(max = 255, message = "Nome não pode ter mais de 255 caracteres")
	private String nome;

}
