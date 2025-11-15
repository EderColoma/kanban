package br.com.facilit.kanban.model;

import static jakarta.persistence.GenerationType.SEQUENCE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "secretaria")
@NoArgsConstructor
@AllArgsConstructor
public class Secretaria {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_secretaria")
	@SequenceGenerator(name = "seq_secretaria", sequenceName = "seq_secretaria", allocationSize = 1)
	private Long id;

	@Column(name = "nome")
	private String nome;

}
