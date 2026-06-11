package br.com.avcar.oficina.business.garantia.mapper;

import br.com.avcar.oficina.business.garantia.dto.GarantiaServicoDTO;
import br.com.avcar.oficina.business.garantia.enums.StatusGarantia;
import br.com.avcar.oficina.business.garantia.model.GarantiaServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre GarantiaServicoModel e GarantiaServicoDTO.
 */
@Component
public class GarantiaServicoMapper {

    /**
     * Função: Mapeia dados entre camadas durante a operação criar aguardando finalizacao.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public GarantiaServicoModel criarAguardandoFinalizacao(ItemServicoModel itemServico) {
        GarantiaServicoModel model = new GarantiaServicoModel();
        model.setItemServico(itemServico);
        model.setPrazoDias(resolverPrazoDias(itemServico));
        model.setStatusGarantia(StatusGarantia.AGUARDANDO_FINALIZACAO_OS);
        model.setObservacao("Garantia criada automaticamente para serviço da OS. A contagem inicia após finalização.");
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public GarantiaServicoDTO toDto(GarantiaServicoModel model) {
        if (model == null) {
            return null;
        }
        GarantiaServicoDTO dto = new GarantiaServicoDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setPrazoDias(model.getPrazoDias());
        dto.setDataInicio(model.getDataInicio());
        dto.setDataFim(model.getDataFim());
        dto.setStatusGarantia(model.getStatusGarantia());
        dto.setDataAcionamento(model.getDataAcionamento());
        dto.setMotivoAcionamento(model.getMotivoAcionamento());
        dto.setDescricaoDefeito(model.getDescricaoDefeito());
        dto.setResponsavelAnalise(model.getResponsavelAnalise());
        dto.setDataEncerramento(model.getDataEncerramento());
        dto.setSolucaoAplicada(model.getSolucaoAplicada());
        dto.setCustoAssumidoPor(model.getCustoAssumidoPor());
        dto.setAtendimentoRealizado(model.getAtendimentoRealizado());
        dto.setObservacao(model.getObservacao());

        if (model.getItemServico() != null) {
            ItemServicoModel item = model.getItemServico();
            dto.setIdItemServico(item.getId());
            if (item.getOrdemServico() != null) {
                dto.setIdOrdemServico(item.getOrdemServico().getId());
            }
            if (item.getServico() != null) {
                dto.setIdServico(item.getServico().getId());
                dto.setNomeServico(item.getServico().getNomeServico());
            }
            if (item.getColaborador() != null) {
                dto.setIdColaborador(item.getColaborador().getId());
                if (item.getColaborador().getPessoa() != null) {
                    dto.setNomeColaboradorResponsavel(item.getColaborador().getPessoa().getNome());
                }
            }
        }
        return dto;
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação resolver prazo dias.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public Integer resolverPrazoDias(ItemServicoModel itemServico) {
        if (itemServico != null
                && itemServico.getServico() != null
                && itemServico.getServico().getPrazoGarantiaDias() != null) {
            return itemServico.getServico().getPrazoGarantiaDias();
        }
        return 90;
    }
}
