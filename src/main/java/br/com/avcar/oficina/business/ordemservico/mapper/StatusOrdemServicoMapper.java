package br.com.avcar.oficina.business.ordemservico.mapper;

import br.com.avcar.oficina.core.mapper.IGenericMapper;

import br.com.avcar.oficina.business.ordemservico.dto.StatusOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.model.StatusOrdemServicoModel;
import org.springframework.stereotype.Component;

/**
 * Mapper do cadastro de Status da Ordem de Serviço.
 */
@Component
public class StatusOrdemServicoMapper implements IGenericMapper<StatusOrdemServicoModel, StatusOrdemServicoDTO> {

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public StatusOrdemServicoDTO toDto(StatusOrdemServicoModel model) {
        if (model == null) {
            return null;
        }
        StatusOrdemServicoDTO dto = new StatusOrdemServicoDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setNomeStatus(model.getNomeStatus());
        dto.setOrdemFluxo(model.getOrdemFluxo());
        dto.setDescricao(model.getDescricao());
        return dto;
    }
}
