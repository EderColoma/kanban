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
@Table(name = "secretaria")
@EntityListeners(AuditingEntityListener.class)
public class Secretaria {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_secretaria")
	@SequenceGenerator(name = "seq_secretaria", sequenceName = "seq_secretaria", allocationSize = 1)
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Setter(NONE)
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Setter(NONE)
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public Secretaria(final Long id, final String nome) {
		this.id = id;
		this.nome = nome;
	}

}
