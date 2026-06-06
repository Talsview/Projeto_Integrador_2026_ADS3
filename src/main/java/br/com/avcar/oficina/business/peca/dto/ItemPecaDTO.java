package br.com.avcar.oficina.business.peca.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para registrar a peça aplicada em uma Ordem de Serviço.
 *
 * Observação: a entidade OrdemServico será implementada em etapa posterior.
 * Nesta etapa, o vínculo é preparado pelo campo idOrdemServico para manter
 * compatibilidade com o modelo físico já validado.
 */
@Getter
@Setter
public class ItemPecaDTO extends BaseDTO {

    private Long idOrdemServico;
    private Long idPeca;
    private String nomePeca;
    private String codigoNacional;
    private Long idFornecedor;
    private String nomeFornecedor;
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private String observacao;
}
