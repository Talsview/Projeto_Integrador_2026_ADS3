package br.com.avcar.oficina.business.pagamento.dto;

import br.com.avcar.oficina.business.pagamento.enums.FormaPagamento;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.core.dto.BaseDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para entrada e saída dos pagamentos vinculados à Ordem de Serviço.
 */
@Getter
@Setter
public class PagamentoDTO extends BaseDTO {

    private Long idOrdemServico;
    private String numeroOs;
    private FormaPagamento formaPagamento;
    private BigDecimal valorPago;
    private LocalDateTime dataPagamento;
    private StatusPagamento statusPagamento;
    private String observacao;
}
