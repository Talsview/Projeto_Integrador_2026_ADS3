package br.com.avcar.oficina.business.pagamento.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de apoio para apresentar a situação financeira consolidada da OS.
 */
@Getter
@Setter
public class ResumoPagamentoOrdemServicoDTO extends BaseDTO {

    private Long idOrdemServico;
    private String numeroOs;
    private BigDecimal valorTotalOrdemServico;
    private BigDecimal valorPago;
    private BigDecimal valorPendente;
    private Boolean quitada;
}
