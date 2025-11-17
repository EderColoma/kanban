package br.com.facilit.kanban.dto.response;

import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsavelDTO {

	private Long id;
	private String nome;
	private String email;
	private String cargo;
	private SecretariaDTO secretaria;

}
