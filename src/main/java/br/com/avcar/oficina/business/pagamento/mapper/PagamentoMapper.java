package br.com.avcar.oficina.business.pagamento.mapper;

import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.pagamento.dto.PagamentoDTO;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.business.pagamento.model.PagamentoModel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre PagamentoDTO e PagamentoModel.
 */
@Component
public class PagamentoMapper {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public PagamentoModel toModel(PagamentoDTO dto, OrdemServicoModel ordemServico) {
        PagamentoModel model = new PagamentoModel();
        model.setOrdemServico(ordemServico);
        atualizarModel(model, dto, ordemServico);
        return model;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarModel(PagamentoModel model, PagamentoDTO dto, OrdemServicoModel ordemServico) {
        model.setOrdemServico(ordemServico);
        model.setFormaPagamento(dto.getFormaPagamento());
        model.setValorPago(dto.getValorPago() == null ? BigDecimal.ZERO : dto.getValorPago());
        model.setStatusPagamento(dto.getStatusPagamento() == null ? StatusPagamento.PAGO : dto.getStatusPagamento());
        model.setDataPagamento(dto.getDataPagamento() == null && model.getStatusPagamento() == StatusPagamento.PAGO
                ? LocalDateTime.now()
                : dto.getDataPagamento());
        model.setObservacao(normalize(dto.getObservacao()));
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public PagamentoDTO toDto(PagamentoModel model) {
        if (model == null) {
            return null;
        }
        PagamentoDTO dto = new PagamentoDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        if (model.getOrdemServico() != null) {
            dto.setIdOrdemServico(model.getOrdemServico().getId());
            dto.setNumeroOs(model.getOrdemServico().getNumeroOs());
        }
        dto.setFormaPagamento(model.getFormaPagamento());
        dto.setValorPago(model.getValorPago());
        dto.setDataPagamento(model.getDataPagamento());
        dto.setStatusPagamento(model.getStatusPagamento());
        dto.setObservacao(model.getObservacao());
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
