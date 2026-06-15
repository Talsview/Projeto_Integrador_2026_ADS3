package br.com.avcar.oficina.business.servico.dto;

import br.com.avcar.oficina.business.servico.enums.TipoServico;
import br.com.avcar.oficina.core.dto.BaseDTO;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para cadastro, atualização e consulta de serviços.
 */
@Getter
@Setter
public class ServicoDTO extends BaseDTO {

    private String nomeServico;
    private String descricao;
    private Integer prazoGarantiaDias;
    private BigDecimal valorBase;
    private TipoServico tipoServico;
    private String observacaoInterna;
    private String observacaoTerceirizacao;
    private Long idEmpresaTerceirizadaPadrao;
    private String nomeEmpresaTerceirizadaPadrao;
}
