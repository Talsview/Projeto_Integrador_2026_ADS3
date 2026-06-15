package br.com.avcar.oficina.business.veiculo.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa um registro histórico de propriedade do veículo.
 */
@Getter
@Setter
public class HistoricoProprietarioDTO extends BaseDTO {

    private Long clienteId;
    private String nomeCliente;
    private Long veiculoId;
    private String placaVeiculo;
    private String nomeMarcaVeiculo;
    private String nomeModeloVeiculo;
    private Integer anoVeiculo;
    private Integer anoModelo;
    private String chassiVeiculo;
    private String corVeiculo;
    private Integer quilometragemAtual;
    private LocalDate dataInicioPosse;
    private LocalDate dataFimPosse;
    private Boolean proprietarioAtual;
    private String observacao;
}
