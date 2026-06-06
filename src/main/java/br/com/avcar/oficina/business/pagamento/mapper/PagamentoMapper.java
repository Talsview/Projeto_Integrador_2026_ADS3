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

    public PagamentoModel toModel(PagamentoDTO dto, OrdemServicoModel ordemServico) {
        PagamentoModel model = new PagamentoModel();
        model.setOrdemServico(ordemServico);
        atualizarModel(model, dto, ordemServico);
        return model;
    }

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

    public PagamentoDTO toDto(PagamentoModel model) {
        if (model == null) {
            return null;
        }
        PagamentoDTO dto = new PagamentoDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
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

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
