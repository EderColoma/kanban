package br.com.facilit.kanban.mapper;

import static br.com.facilit.kanban.model.Status.CONCLUIDO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.facilit.kanban.dto.request.ProjetoCreationDTO;
import br.com.facilit.kanban.dto.request.ProjetoUpdateDTO;
import br.com.facilit.kanban.dto.response.ProjetoDTO;
import br.com.facilit.kanban.dto.response.ResponsavelDTO;
import br.com.facilit.kanban.dto.shared.SecretariaDTO;
import br.com.facilit.kanban.model.Projeto;
import br.com.facilit.kanban.model.Responsavel;
import br.com.facilit.kanban.model.Secretaria;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProjetoMapper.class, ModelMapper.class})
class ProjetoMapperTest {

	@Autowired
	private ProjetoMapper projetoMapper;

	@Test
	void toDTO_Should_ConvertTheEntityObjectToProjetoDTO() {
		final Secretaria secretaria = new Secretaria(1L, "Secretaria");
		final Responsavel responsavel = new Responsavel(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretaria);
		final Projeto projeto = Projeto.builder()
				.id(1L)
				.nome("Projeto")
				.status(CONCLUIDO)
				.inicioPrevisto(LocalDate.of(2024, 1, 1))
				.terminoPrevisto(LocalDate.of(2024, 6, 30))
				.inicioRealizado(LocalDate.of(2024, 1, 5))
				.terminoRealizado(LocalDate.of(2024, 6, 28))
				.diasAtraso(0)
				.percentualTempoRestante(50)
				.responsaveis(Set.of(responsavel))
				.build();

		final ProjetoDTO projetoDTO = projetoMapper.toDTO(projeto);

		assertNotNull(projetoDTO);
		assertEquals(Long.valueOf(1), projetoDTO.getId());
		assertEquals("Projeto", projetoDTO.getNome());
		assertEquals(LocalDate.of(2024, 1, 1), projeto.getInicioPrevisto());
		assertEquals(LocalDate.of(2024, 1, 5), projeto.getInicioRealizado());
		assertEquals(LocalDate.of(2024, 6, 30), projeto.getTerminoPrevisto());
		assertEquals(LocalDate.of(2024, 6, 28), projeto.getTerminoRealizado());
		assertEquals(0, projetoDTO.getDiasAtraso());
		assertEquals(50, projetoDTO.getPercentualTempoRestante());
		assertEquals(1, projetoDTO.getResponsaveis().size());
		assertEquals(Long.valueOf(1), projetoDTO.getResponsaveis().iterator().next().getId());
		assertEquals("Responsavel", projetoDTO.getResponsaveis().iterator().next().getNome());
		assertEquals("responsavel@facilit.com", projetoDTO.getResponsaveis().iterator().next().getEmail());
		assertEquals("Product Owner", projetoDTO.getResponsaveis().iterator().next().getCargo());
		assertEquals("Secretaria", projetoDTO.getResponsaveis().iterator().next().getSecretaria().getNome());
		assertEquals(Long.valueOf(1), projetoDTO.getResponsaveis().iterator().next().getSecretaria().getId());
	}

	@Test
	void toEntity_Should_ConvertTheProjetoDTOToAnEntityObject() {
		final SecretariaDTO secretariaDTO = new SecretariaDTO(1L, "Secretaria");
		final ResponsavelDTO responsavelDTO = new ResponsavelDTO(1L, "Responsavel", "responsavel@facilit.com", "Product Owner", secretariaDTO);
		final ProjetoDTO projetoDTO = ProjetoDTO.builder()
				.id(1L)
				.nome("Projeto")
				.status(CONCLUIDO)
				.inicioPrevisto(LocalDate.of(2024, 1, 1))
				.terminoPrevisto(LocalDate.of(2024, 6, 30))
				.inicioRealizado(LocalDate.of(2024, 1, 5))
				.terminoRealizado(LocalDate.of(2024, 6, 28))
				.diasAtraso(0)
				.percentualTempoRestante(50)
				.responsaveis(Set.of(responsavelDTO))
				.build();

		final Projeto projeto = projetoMapper.toEntity(projetoDTO);

		assertNotNull(projeto);
		assertEquals(Long.valueOf(1), projeto.getId());
		assertEquals("Projeto", projeto.getNome());
		assertEquals(LocalDate.of(2024, 1, 1), projeto.getInicioPrevisto());
		assertEquals(LocalDate.of(2024, 1, 5), projeto.getInicioRealizado());
		assertEquals(LocalDate.of(2024, 6, 30), projeto.getTerminoPrevisto());
		assertEquals(LocalDate.of(2024, 6, 28), projeto.getTerminoRealizado());
		assertEquals(0, projeto.getDiasAtraso());
		assertEquals(50, projeto.getPercentualTempoRestante());
		assertEquals(1, projeto.getResponsaveis().size());
		assertEquals(1, projeto.getResponsaveis().size());
		assertEquals(Long.valueOf(1), projeto.getResponsaveis().iterator().next().getId());
		assertEquals("Responsavel", projeto.getResponsaveis().iterator().next().getNome());
		assertEquals("responsavel@facilit.com", projeto.getResponsaveis().iterator().next().getEmail());
		assertEquals("Product Owner", projeto.getResponsaveis().iterator().next().getCargo());
		assertEquals("Secretaria", projeto.getResponsaveis().iterator().next().getSecretaria().getNome());
		assertEquals(Long.valueOf(1), projeto.getResponsaveis().iterator().next().getSecretaria().getId());
	}

	@Test
	void toEntity_Should_ConvertTheProjetoUpdateDTOToAnEntityObject() {
		final ProjetoUpdateDTO projetoUpdateDTO = ProjetoUpdateDTO.builder()
				.id(1L)
				.nome("Projeto")
				.inicioPrevisto("01-01-2024")
				.terminoPrevisto("30-06-2024")
				.inicioRealizado("05-01-2024")
				.terminoRealizado("28-06-2024")
				.build();

		final Projeto projeto = projetoMapper.toEntity(projetoUpdateDTO);

		assertNotNull(projeto);
		assertEquals(Long.valueOf(1), projeto.getId());
		assertEquals("Projeto", projeto.getNome());
		assertEquals(LocalDate.of(2024, 1, 1), projeto.getInicioPrevisto());
		assertEquals(LocalDate.of(2024, 1, 5), projeto.getInicioRealizado());
		assertEquals(LocalDate.of(2024, 6, 30), projeto.getTerminoPrevisto());
		assertEquals(LocalDate.of(2024, 6, 28), projeto.getTerminoRealizado());
	}

	@Test
	void toEntity_Should_ConvertTheProjetoCreationDTOToAnEntityObject() {
		final ProjetoCreationDTO projetoCreationDTO = ProjetoCreationDTO.builder()
				.nome("Projeto")
				.inicioPrevisto("01-01-2024")
				.terminoPrevisto("30-06-2024")
				.inicioRealizado("05-01-2024")
				.terminoRealizado("28-06-2024")
				.build();

		final Projeto projeto = projetoMapper.toEntity(projetoCreationDTO);

		assertNotNull(projeto);
		assertEquals("Projeto", projeto.getNome());
		assertEquals(LocalDate.of(2024, 1, 1), projeto.getInicioPrevisto());
		assertEquals(LocalDate.of(2024, 1, 5), projeto.getInicioRealizado());
		assertEquals(LocalDate.of(2024, 6, 30), projeto.getTerminoPrevisto());
		assertEquals(LocalDate.of(2024, 6, 28), projeto.getTerminoRealizado());
	}

}
