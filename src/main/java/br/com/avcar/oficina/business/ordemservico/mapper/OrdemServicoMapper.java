package br.com.avcar.oficina.business.ordemservico.mapper;

import br.com.avcar.oficina.business.ordemservico.dto.HistoricoStatusOrdemDTO;
import br.com.avcar.oficina.business.ordemservico.dto.ItemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.business.ordemservico.enums.PrioridadeOrdemServico;
import br.com.avcar.oficina.business.ordemservico.model.HistoricoStatusOrdemModel;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.veiculo.model.VeiculoModel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável por adaptar a OrdemServicoModel para DTOs de detalhe e
 * resumo utilizados pela View Angular.
 */
@Component
public class OrdemServicoMapper {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public OrdemServicoModel toModel(OrdemServicoDTO dto, ClienteModel cliente, VeiculoModel veiculo) {
        OrdemServicoModel model = new OrdemServicoModel();
        model.setCliente(cliente);
        model.setVeiculo(veiculo);
        atualizarModel(model, dto, cliente, veiculo);
        return model;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarModel(OrdemServicoModel model, OrdemServicoDTO dto, ClienteModel cliente, VeiculoModel veiculo) {
        model.setCliente(cliente);
        model.setVeiculo(veiculo);
        // Número da OS é gerado exclusivamente no service para impedir edição manual e reutilização.
        model.setDataAbertura(dto.getDataAbertura() == null ? LocalDateTime.now() : dto.getDataAbertura());
        model.setPrioridade(dto.getPrioridade() == null ? PrioridadeOrdemServico.NORMAL : dto.getPrioridade());
        model.setValorTotal(dto.getValorTotal() == null ? BigDecimal.ZERO : dto.getValorTotal());
        model.setObservacao(normalize(dto.getObservacao()));
    }

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public OrdemServicoDTO toDetalheDto(OrdemServicoModel ordemServico,
                                        HistoricoStatusOrdemModel statusAtual,
                                        List<HistoricoStatusOrdemDTO> historicoStatus,
                                        List<ItemServicoDTO> itensServico) {
        if (ordemServico == null) {
            return null;
        }
        OrdemServicoDTO dto = new OrdemServicoDTO();
        preencherDadosComuns(dto, ordemServico, statusAtual);
        dto.setDataAprovacao(ordemServico.getDataAprovacao());
        dto.setHistoricoStatus(historicoStatus);
        dto.setItensServico(itensServico);
        return dto;
    }

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public OrdemServicoResumoDTO toResumoDto(OrdemServicoModel ordemServico, HistoricoStatusOrdemModel statusAtual) {
        if (ordemServico == null) {
            return null;
        }
        OrdemServicoResumoDTO dto = new OrdemServicoResumoDTO();
        dto.setId(ordemServico.getId());
        dto.setAtivo(ordemServico.getAtivo());
        dto.setDataHoraCriacao(ordemServico.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(ordemServico.getDataHoraAtualizacao());
        dto.setNumeroOs(ordemServico.getNumeroOs());
        dto.setDataAbertura(ordemServico.getDataAbertura());
        dto.setDataFinalizacao(ordemServico.getDataFinalizacao());
        dto.setPrioridade(ordemServico.getPrioridade());
        dto.setValorTotal(ordemServico.getValorTotal());
        preencherClienteVeiculo(dto, ordemServico);
        dto.setStatusAtual(statusAtual == null ? null : statusAtual.getStatusOrdemServico().getNomeStatus());
        return dto;
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação preencher dados comuns.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private void preencherDadosComuns(OrdemServicoDTO dto,
                                      OrdemServicoModel ordemServico,
                                      HistoricoStatusOrdemModel statusAtual) {
        dto.setId(ordemServico.getId());
        dto.setAtivo(ordemServico.getAtivo());
        dto.setDataHoraCriacao(ordemServico.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(ordemServico.getDataHoraAtualizacao());
        dto.setNumeroOs(ordemServico.getNumeroOs());
        dto.setDataAbertura(ordemServico.getDataAbertura());
        dto.setDataFinalizacao(ordemServico.getDataFinalizacao());
        dto.setPrioridade(ordemServico.getPrioridade());
        dto.setValorTotal(ordemServico.getValorTotal());
        dto.setObservacao(ordemServico.getObservacao());
        preencherClienteVeiculo(dto, ordemServico);
        dto.setStatusAtual(statusAtual == null ? null : statusAtual.getStatusOrdemServico().getNomeStatus());
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação preencher cliente veiculo.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private void preencherClienteVeiculo(OrdemServicoDTO dto, OrdemServicoModel ordemServico) {
        if (ordemServico.getCliente() != null) {
            dto.setIdCliente(ordemServico.getCliente().getId());
            if (ordemServico.getCliente().getPessoa() != null) {
                dto.setNomeCliente(ordemServico.getCliente().getPessoa().getNome());
            }
        }
        if (ordemServico.getVeiculo() != null) {
            dto.setIdVeiculo(ordemServico.getVeiculo().getId());
            dto.setPlacaVeiculo(ordemServico.getVeiculo().getPlaca());
            if (ordemServico.getVeiculo().getModelo() != null) {
                String marca = ordemServico.getVeiculo().getModelo().getMarca() == null
                        ? ""
                        : ordemServico.getVeiculo().getModelo().getMarca().getNomeMarca() + " ";
                dto.setDescricaoVeiculo((marca + ordemServico.getVeiculo().getModelo().getNomeModelo()).trim());
            }
        }
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação preencher cliente veiculo.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private void preencherClienteVeiculo(OrdemServicoResumoDTO dto, OrdemServicoModel ordemServico) {
        if (ordemServico.getCliente() != null) {
            dto.setIdCliente(ordemServico.getCliente().getId());
            if (ordemServico.getCliente().getPessoa() != null) {
                dto.setNomeCliente(ordemServico.getCliente().getPessoa().getNome());
            }
        }
        if (ordemServico.getVeiculo() != null) {
            dto.setIdVeiculo(ordemServico.getVeiculo().getId());
            dto.setPlacaVeiculo(ordemServico.getVeiculo().getPlaca());
            if (ordemServico.getVeiculo().getModelo() != null) {
                String marca = ordemServico.getVeiculo().getModelo().getMarca() == null
                        ? ""
                        : ordemServico.getVeiculo().getModelo().getMarca().getNomeMarca() + " ";
                dto.setDescricaoVeiculo((marca + ordemServico.getVeiculo().getModelo().getNomeModelo()).trim());
            }
        }
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação normalize.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
