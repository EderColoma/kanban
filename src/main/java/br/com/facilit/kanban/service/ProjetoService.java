package br.com.facilit.kanban.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.facilit.kanban.dto.request.ProjetoCreationDTO;
import br.com.facilit.kanban.dto.request.ProjetoUpdateDTO;
import br.com.facilit.kanban.dto.response.ProjetoDTO;
import br.com.facilit.kanban.mapper.ProjetoMapper;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.repository.ProjetoRepository;
import br.com.facilit.kanban.service.status.strategy.ProjetoStatusCalculator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjetoService {

	private final ProjetoMapper projetoMapper;
	private final ProjetoStatusCalculator projetoStatusCalculator;
	private final ResponsavelService responsavelService;
	private final ProjetoRepository projetoRepository;

    public Optional<Projeto> findById(final Long id) {
        return projetoRepository.findById(id);
    }

    public Optional<ProjetoDTO> findDTOById(final Long id) {
        return projetoRepository.findById(id).map(projetoMapper::toDTO);
    }

    public Page<ProjetoDTO> findAll(final Pageable pageable) {
    	return projetoRepository.findAll(pageable).map(projetoMapper::toDTO);
    }

    public Projeto save(final ProjetoCreationDTO projetoCreationDTO) {
    	final Projeto projeto = projetoMapper.toEntity(projetoCreationDTO);
    	addResponsavelToProjeto(projeto, projetoCreationDTO.getResponsaveisIds());
    	projetoStatusCalculator.calculate(projeto);

        return projetoRepository.save(projeto);
    }

    public Projeto update(final ProjetoUpdateDTO projetoUpdateDTO) {

        final Projeto currentProjeto = findById(projetoUpdateDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com id: " + projetoUpdateDTO.getId()));

        final Projeto newProjeto = projetoMapper.toEntity(projetoUpdateDTO);
        copyFields(newProjeto, currentProjeto);
        addResponsavelToProjeto(currentProjeto, projetoUpdateDTO.getResponsaveisIds());
        projetoStatusCalculator.calculate(currentProjeto);

        return projetoRepository.save(currentProjeto);
    }

    private void copyFields(final Projeto source, final Projeto target) {
        target.setNome(source.getNome());
        target.setStatus(source.getStatus());
        target.setInicioPrevisto(source.getInicioPrevisto());
        target.setTerminoPrevisto(source.getTerminoPrevisto());
        target.setInicioRealizado(source.getInicioRealizado());
        target.setTerminoRealizado(source.getTerminoRealizado());
        target.setResponsaveis(source.getResponsaveis());
    }

    private void addResponsavelToProjeto(final Projeto projeto, final Set<Long> responsavelIds) {
    	projeto.setResponsaveis(new HashSet<>());
    	responsavelIds.forEach(id -> {
     		final Responsavel responsavel = responsavelService.findById(id)
 		            .orElseThrow(() -> new EntityNotFoundException("Responsavel não encontrado: " + id));

     		projeto.getResponsaveis().add(responsavel);
 		});
    }

    public void delete(final Long id) {
		projetoRepository.deleteById(id);
	}
}
