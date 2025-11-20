package br.com.facilit.kanban.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Status;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

	public Page<Projeto> findAllByStatus(Status status, Pageable pageable);

}
