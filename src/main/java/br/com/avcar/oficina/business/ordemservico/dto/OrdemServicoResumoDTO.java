package br.com.avcar.oficina.business.ordemservico.dto;

import br.com.avcar.oficina.business.ordemservico.enums.PrioridadeOrdemServico;
import br.com.avcar.oficina.core.dto.BaseDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemServicoResumoDTO extends BaseDTO {

    private String numeroOs;
    private Long idCliente;
    private String nomeCliente;
    private Long idVeiculo;
    private String placaVeiculo;
    private String descricaoVeiculo;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFinalizacao;
    private PrioridadeOrdemServico prioridade;
    private BigDecimal valorTotal;
    private String statusAtual;
}
