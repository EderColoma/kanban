package br.com.facilit.kanban.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.SEQUENCE;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "projeto")
@NoArgsConstructor
@AllArgsConstructor
public class Projeto {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_projeto")
	@SequenceGenerator(name = "seq_projeto", sequenceName = "seq_projeto", allocationSize = 1)
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Enumerated(STRING)
	@Column(name = "status")
    private Status status;

	@Column(name = "inicio_previsto")
	private LocalDate inicioPrevisto;

	@Column(name = "termino_previsto")
    private LocalDate terminoPrevisto;

	@Column(name = "inicio_realizado")
    private LocalDate inicioRealizado;

	@Column(name = "termino_realizado")
    private LocalDate terminoRealizado;

	@Column(name = "dias_atraso")
    private Integer diasAtraso;

	@Column(name = "tempo_restante")
    private Integer percentualTempoRestante;

	@ManyToMany
    @JoinTable(name = "projeto_responsavel",
               joinColumns = @JoinColumn(name = "projeto_id"),
               inverseJoinColumns = @JoinColumn(name = "responsavel_id"))
    private Set<Responsavel> responsaveis;
}
