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

import br.com.facilit.kanban.dto.request.ResponsavelCreationDTO;
import br.com.facilit.kanban.dto.request.ResponsavelUpdateDTO;
import br.com.facilit.kanban.dto.response.ResponsavelDTO;
import br.com.facilit.kanban.service.ResponsavelService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/responsavel")
public class ResponsavelController {

	private final ResponsavelService responsavelService;

	@GetMapping
	public ResponseEntity<Page<ResponsavelDTO>> findAll(final Pageable pageable) {
		return ok(responsavelService.findAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponsavelDTO> findById(@PathVariable("id") final Long id) {
		return responsavelService.findDTOById(id)
		        .map(ResponseEntity::ok)
		        .orElseGet(() -> noContent().build());

	}

	@PostMapping
	public ResponseEntity<Void> create(@Valid @RequestBody final ResponsavelCreationDTO responsavelCreationDTO) {
		responsavelService.save(responsavelCreationDTO);
		return ResponseEntity.ok().build();
	}

	@PutMapping
	public ResponseEntity<Void> update(@Valid @RequestBody final ResponsavelUpdateDTO responsavelUpdateDTO) {
		try {
	    	responsavelService.update(responsavelUpdateDTO);
	    } catch (final EntityNotFoundException e) {
	    	return ResponseEntity.badRequest().build();
	    }

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
		responsavelService.delete(id);
		return ResponseEntity.ok().build();
	}

}
