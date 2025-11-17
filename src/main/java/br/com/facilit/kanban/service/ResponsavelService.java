package br.com.facilit.kanban.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.facilit.kanban.dto.request.ResponsavelCreationDTO;
import br.com.facilit.kanban.dto.request.ResponsavelUpdateDTO;
import br.com.facilit.kanban.dto.response.ResponsavelDTO;
import br.com.facilit.kanban.mapper.ResponsavelMapper;
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.model.Secretaria;
import br.com.facilit.kanban.repository.ResponsavelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponsavelService {

	private final ResponsavelMapper responsavelMapper;
	private final SecretariaService secretariaService;
	private final ResponsavelRepository responsavelRepository;

    public Optional<Responsavel> findById(final Long id) {
		 return responsavelRepository.findById(id);
	}

    public Optional<ResponsavelDTO> findDTOById(final Long id) {
		 return findById(id).map(responsavelMapper::toDTO);
	}

    public Page<ResponsavelDTO> findAll(final Pageable pageable) {
		 return responsavelRepository.findAll(pageable).map(responsavelMapper::toDTO);
	}

	public Responsavel save(final ResponsavelCreationDTO responsavelCreationDTO) {
		final Responsavel responsavel = responsavelMapper.toEntity(responsavelCreationDTO);

	    final Secretaria secretaria = secretariaService.findById(responsavelCreationDTO.getSecretariaId())
	            .orElseThrow(() -> new EntityNotFoundException("Secretaria não encontrada"));

	    responsavel.setSecretaria(secretaria);

	    return responsavelRepository.save(responsavel);
	}

	public void update(final ResponsavelUpdateDTO responsavelUpdateDTO) throws EntityNotFoundException {
		responsavelRepository.findById(responsavelUpdateDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Responsável não encontrado"));

		 final Secretaria secretaria = secretariaService.findById(responsavelUpdateDTO.getSecretariaId())
		            .orElseThrow(() -> new EntityNotFoundException("Secretaria não encontrada"));

		 final Responsavel responsavel = responsavelMapper.toEntity(responsavelUpdateDTO);
		 responsavel.setSecretaria(secretaria);

		 responsavelRepository.save(responsavel);
	}

	public void delete(final Long id) {
		responsavelRepository.deleteById(id);
	}

	public Optional<Responsavel> findByEmail(final String email) {
		return responsavelRepository.findByEmail(email);
	}
}
