package br.com.avcar.oficina.business.ordemservico.mapper;

import br.com.avcar.oficina.core.mapper.IGenericMapper;

import br.com.avcar.oficina.business.ordemservico.dto.HistoricoStatusOrdemDTO;
import br.com.avcar.oficina.business.ordemservico.model.HistoricoStatusOrdemModel;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.StatusOrdemServicoModel;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * Mapper da entidade associativa HistoricoStatusOrdem.
 */
@Component
public class HistoricoStatusOrdemMapper implements IGenericMapper<HistoricoStatusOrdemModel, HistoricoStatusOrdemDTO> {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public HistoricoStatusOrdemModel criarHistorico(OrdemServicoModel ordemServico,
                                                     StatusOrdemServicoModel status,
                                                     String observacao) {
        HistoricoStatusOrdemModel model = new HistoricoStatusOrdemModel();
        model.setOrdemServico(ordemServico);
        model.setStatusOrdemServico(status);
        model.setDataStatus(LocalDateTime.now());
        model.setObservacao(normalize(observacao));
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public HistoricoStatusOrdemDTO toDto(HistoricoStatusOrdemModel model) {
        if (model == null) {
            return null;
        }
        HistoricoStatusOrdemDTO dto = new HistoricoStatusOrdemDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setIdOrdemServico(model.getOrdemServico() == null ? null : model.getOrdemServico().getId());
        dto.setDataStatus(model.getDataStatus());
        dto.setObservacao(model.getObservacao());

        if (model.getStatusOrdemServico() != null) {
            dto.setIdStatusOrdemServico(model.getStatusOrdemServico().getId());
            dto.setNomeStatus(model.getStatusOrdemServico().getNomeStatus());
            dto.setOrdemFluxo(model.getStatusOrdemServico().getOrdemFluxo());
        }
        return dto;
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
