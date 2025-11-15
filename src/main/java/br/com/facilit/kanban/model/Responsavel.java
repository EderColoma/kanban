package br.com.facilit.kanban.model;

import static jakarta.persistence.GenerationType.SEQUENCE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "responsavel")
@NoArgsConstructor
@AllArgsConstructor
public class Responsavel {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_responsavel")
	@SequenceGenerator(name = "seq_responsavel", sequenceName = "seq_responsavel", allocationSize = 1)
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "email")
	private String email;

	@Column(name = "cargo")
	private String cargo;

	@ManyToOne
	@JoinColumn(name = "secretaria_id")
	private Secretaria secretaria;

}
