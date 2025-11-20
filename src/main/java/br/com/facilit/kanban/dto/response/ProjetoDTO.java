package br.com.facilit.kanban.dto.response;

import java.time.LocalDate;
import java.util.Set;

import br.com.facilit.kanban.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoDTO {

	private Long id;
	private String nome;
    private Status status;
	private LocalDate inicioPrevisto;
    private LocalDate terminoPrevisto;
    private LocalDate inicioRealizado;
    private LocalDate terminoRealizado;
    private Integer diasAtraso;
    private Integer percentualTempoRestante;
    private Set<ResponsavelDTO> responsaveis;

}
