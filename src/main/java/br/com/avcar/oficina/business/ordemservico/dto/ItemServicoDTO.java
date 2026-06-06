package br.com.avcar.oficina.business.ordemservico.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemServicoDTO extends BaseDTO {

    private Long idOrdemServico;
    private String numeroOs;
    private Long idServico;
    private String nomeServico;
    private Long idColaborador;
    private String nomeColaborador;
    private String descricaoExecucao;
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    private Long idExecucaoServicoTerceirizado;
    private Long idEmpresaTerceirizada;
    private String nomeEmpresaTerceirizada;
    private LocalDateTime dataEnvioTerceirizacao;
    private LocalDateTime dataRetornoTerceirizacao;
    private BigDecimal valorCobradoTerceirizacao;
    private String observacaoTerceirizacao;
}
