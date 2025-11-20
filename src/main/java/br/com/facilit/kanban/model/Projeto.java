package br.com.facilit.kanban.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.NONE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projeto")
@EntityListeners(AuditingEntityListener.class)
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

	@Setter(NONE)
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Setter(NONE)
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public Projeto(final Long id, final String nome, final Status status, final LocalDate inicioPrevisto, final LocalDate terminoPrevisto, final LocalDate inicioRealizado, final LocalDate terminoRealizado, final Integer diasAtraso, final Integer percentualTempoRestante, final Set<Responsavel> responsaveis) {
		this.id = id;
		this.nome = nome;
		this.status = status;
		this.inicioPrevisto = inicioPrevisto;
		this.terminoPrevisto = terminoPrevisto;
		this.inicioRealizado = inicioRealizado;
		this.terminoRealizado = terminoRealizado;
		this.diasAtraso = diasAtraso;
		this.percentualTempoRestante = percentualTempoRestante;
		this.responsaveis = responsaveis;
	}

}
