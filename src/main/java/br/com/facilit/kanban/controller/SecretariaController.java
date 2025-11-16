package br.com.facilit.kanban.controller;

import static org.springframework.http.ResponseEntity.noContent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.facilit.kanban.dto.SecretariaDTO;
import br.com.facilit.kanban.dto.SecretariaUpdateDTO;
import br.com.facilit.kanban.service.SecretariaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/secretaria")
public class SecretariaController {

	private final SecretariaService secretariaService;

	@GetMapping("/{id}")
	public ResponseEntity<SecretariaDTO> findById(@PathVariable("id") Long id) {
		return secretariaService.findById(id)
		        .map(ResponseEntity::ok)
		        .orElseGet(() -> noContent().build());

	}

	@PostMapping
	public ResponseEntity<Void> create(@Valid @RequestBody SecretariaDTO secretariaDTO) {
	    secretariaService.save(secretariaDTO);
		return ResponseEntity.ok().build();
	}

	@PutMapping
	public ResponseEntity<Void> update(@Valid @RequestBody SecretariaUpdateDTO secretariaUpdateDTO) {
	    try {
	    	secretariaService.update(secretariaUpdateDTO);
	    } catch (final EntityNotFoundException _) {
	    	return ResponseEntity.badRequest().build();
	    }

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
	    secretariaService.delete(id);
		return ResponseEntity.ok().build();
	}

}
