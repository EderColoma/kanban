package br.com.facilit.kanban.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.facilit.kanban.dto.request.SecretariaUpdateDTO;
import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import br.com.facilit.kanban.mapper.SecretariaMapper;
import br.com.facilit.kanban.model.Secretaria;
import br.com.facilit.kanban.repository.SecretariaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecretariaService {

	private final SecretariaMapper secretariaMapper;
	private final SecretariaRepository secretariaRepository;

	public Optional<Secretaria> findById(Long id) {
		 return secretariaRepository.findById(id);
	}

	public Optional<SecretariaDTO> findDTOById(Long id) {
		 return secretariaRepository.findById(id).map(secretariaMapper::toDTO);
	}

	public Secretaria save(SecretariaDTO secretariaDTO) {
	    return secretariaRepository.save(secretariaMapper.toEntity(secretariaDTO));
	}

	public void update(SecretariaUpdateDTO secretariaUpdateDTO) throws EntityNotFoundException {
		secretariaRepository.findById(secretariaUpdateDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Secretaria não encontrada"));
	    secretariaRepository.save(secretariaMapper.toEntity(secretariaUpdateDTO));
	}

	public void delete(Long id) {
	    secretariaRepository.deleteById(id);
	}

}
