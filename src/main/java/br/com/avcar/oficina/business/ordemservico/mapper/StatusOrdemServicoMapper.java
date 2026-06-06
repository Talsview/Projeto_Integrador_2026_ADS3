package br.com.avcar.oficina.business.ordemservico.mapper;

import br.com.avcar.oficina.business.ordemservico.dto.StatusOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.model.StatusOrdemServicoModel;
import org.springframework.stereotype.Component;

/**
 * Mapper do cadastro de Status da Ordem de Serviço.
 */
@Component
public class StatusOrdemServicoMapper {

    public StatusOrdemServicoDTO toDto(StatusOrdemServicoModel model) {
        if (model == null) {
            return null;
        }
        StatusOrdemServicoDTO dto = new StatusOrdemServicoDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setNomeStatus(model.getNomeStatus());
        dto.setOrdemFluxo(model.getOrdemFluxo());
        dto.setDescricao(model.getDescricao());
        return dto;
    }
}
