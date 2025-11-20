package br.com.facilit.kanban.controller;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.facilit.kanban.dto.request.ProjetoCreationDTO;
import br.com.facilit.kanban.dto.request.ProjetoUpdateDTO;
import br.com.facilit.kanban.dto.response.ProjetoDTO;
import br.com.facilit.kanban.service.ProjetoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projeto")
public class ProjetoController {

	private final ProjetoService projetoService;

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoDTO> findById(@PathVariable final Long id) {
        return projetoService.findDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(noContent().build());
    }

    @GetMapping
    public ResponseEntity<Page<ProjetoDTO>> findAll(final Pageable pageable) {
        return ok(projetoService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody final ProjetoCreationDTO projetoCreationDTO) {
        projetoService.save(projetoCreationDTO);
        return ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody final ProjetoUpdateDTO projetoUpdateDTO) {
    	try {
    		projetoService.update(projetoUpdateDTO);
	    } catch (final EntityNotFoundException _) {
	    	return ResponseEntity.badRequest().build();
	    }

		return ResponseEntity.ok().build();
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
		projetoService.delete(id);
		return ResponseEntity.ok().build();
	}
}
