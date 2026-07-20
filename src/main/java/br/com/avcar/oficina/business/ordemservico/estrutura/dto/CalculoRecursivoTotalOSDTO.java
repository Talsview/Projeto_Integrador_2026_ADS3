package br.com.avcar.oficina.business.ordemservico.estrutura.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculoRecursivoTotalOSDTO extends BaseDTO {

    private Long idOrdemServico;
    private String numeroOs;
    private Integer quantidadeItensServico;
    private Integer quantidadeItensPeca;
    private BigDecimal totalServicos;
    private BigDecimal totalPecas;
    private BigDecimal totalGeral;
    private String funcaoUtilizada;
    private String justificativa;
}
