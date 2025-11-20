package br.com.facilit.kanban.model;

import static jakarta.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.NONE;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "responsavel")
@EntityListeners(AuditingEntityListener.class)
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

	@Setter(NONE)
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Setter(NONE)
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public Responsavel(final Long id, final String nome, final String email, final String cargo, final Secretaria secretaria) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cargo = cargo;
		this.secretaria = secretaria;
	}

}
