package br.com.facilit.kanban.controller;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.facilit.kanban.dto.response.ProjetoDTO;
import br.com.facilit.kanban.exception.StatusTransitionException;
import br.com.facilit.kanban.model.Status;
import br.com.facilit.kanban.service.ProjetoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kanban")
public class KanbanController {

	private final ProjetoService projetoService;

	@GetMapping("/{status}")
    public Page<ProjetoDTO> findAllByStatus(@PathVariable("status") final Status status, final Pageable pageable) {
        return projetoService.findAllByStatus(status, pageable);
    }

	@PatchMapping("/{id}/status")
    public ResponseEntity<Void> transitStatus(@PathVariable final Long id, @RequestParam("status") final Status status) throws StatusTransitionException {
        projetoService.transitStatus(id, status);
        return ok().build();
    }

}
