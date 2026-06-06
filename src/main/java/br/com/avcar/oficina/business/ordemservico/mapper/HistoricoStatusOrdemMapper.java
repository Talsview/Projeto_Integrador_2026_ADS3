package br.com.avcar.oficina.business.ordemservico.mapper;

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
public class HistoricoStatusOrdemMapper {

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

    public HistoricoStatusOrdemDTO toDto(HistoricoStatusOrdemModel model) {
        if (model == null) {
            return null;
        }
        HistoricoStatusOrdemDTO dto = new HistoricoStatusOrdemDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
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

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
