package br.com.avcar.oficina.business.peca.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para cadastro e consulta de peças.
 */
@Getter
@Setter
public class PecaDTO extends BaseDTO {

    private String nomePeca;
    private String codigoNacional;
    private String marcaPeca;
    private String modeloAplicavel;
    private Integer anoVeiculo;
    private Integer anoModelo;
    private Long idFornecedorPadrao;
    private String nomeFornecedorPadrao;
    private BigDecimal valorUnitarioPadrao;
    private Integer prazoGarantiaDias;
    private String descricao;
}
