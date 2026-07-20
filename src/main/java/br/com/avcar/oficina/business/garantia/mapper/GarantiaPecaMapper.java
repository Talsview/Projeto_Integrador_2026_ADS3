package br.com.avcar.oficina.business.garantia.mapper;

import br.com.avcar.oficina.core.mapper.IGenericMapper;

import br.com.avcar.oficina.business.garantia.dto.GarantiaPecaDTO;
import br.com.avcar.oficina.business.garantia.enums.ResponsabilidadeGarantiaPeca;
import br.com.avcar.oficina.business.garantia.enums.StatusGarantia;
import br.com.avcar.oficina.business.garantia.model.GarantiaPecaModel;
import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre GarantiaPecaModel e GarantiaPecaDTO.
 */
@Component
public class GarantiaPecaMapper implements IGenericMapper<GarantiaPecaModel, GarantiaPecaDTO> {

    /**
     * Função: Mapeia dados entre camadas durante a operação criar aguardando finalizacao.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public GarantiaPecaModel criarAguardandoFinalizacao(ItemPecaModel itemPeca) {
        GarantiaPecaModel model = new GarantiaPecaModel();
        model.setItemPeca(itemPeca);
        model.setPrazoDias(resolverPrazoDias(itemPeca));
        model.setResponsabilidade(ResponsabilidadeGarantiaPeca.FORNECEDOR);
        model.setStatusGarantia(StatusGarantia.AGUARDANDO_FINALIZACAO_OS);
        model.setObservacao("Garantia criada automaticamente para peça aplicada na OS. A contagem inicia após finalização.");
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public GarantiaPecaDTO toDto(GarantiaPecaModel model) {
        if (model == null) {
            return null;
        }
        GarantiaPecaDTO dto = new GarantiaPecaDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setPrazoDias(model.getPrazoDias());
        dto.setDataInicio(model.getDataInicio());
        dto.setDataFim(model.getDataFim());
        dto.setResponsabilidade(model.getResponsabilidade());
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

        if (model.getItemPeca() != null) {
            ItemPecaModel item = model.getItemPeca();
            dto.setIdItemPeca(item.getId());
            dto.setIdOrdemServico(item.getIdOrdemServico());
            if (item.getPeca() != null) {
                dto.setIdPeca(item.getPeca().getId());
                dto.setNomePeca(item.getPeca().getNomePeca());
                dto.setCodigoNacional(item.getPeca().getCodigoNacional());
            }
            if (item.getFornecedor() != null) {
                dto.setIdFornecedor(item.getFornecedor().getId());
                dto.setNomeFornecedor(item.getFornecedor().getNomeFornecedor());
            }
        }
        return dto;
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação resolver prazo dias.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public Integer resolverPrazoDias(ItemPecaModel itemPeca) {
        if (itemPeca != null
                && itemPeca.getPeca() != null
                && itemPeca.getPeca().getPrazoGarantiaDias() != null) {
            return itemPeca.getPeca().getPrazoGarantiaDias();
        }
        return 90;
    }
}
