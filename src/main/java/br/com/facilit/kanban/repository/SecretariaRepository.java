package br.com.facilit.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.facilit.kanban.model.Secretaria;

@Repository
public interface SecretariaRepository extends JpaRepository<Secretaria, Long>{

}
