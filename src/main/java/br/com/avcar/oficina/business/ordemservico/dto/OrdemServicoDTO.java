package br.com.avcar.oficina.business.ordemservico.dto;

import br.com.avcar.oficina.business.ordemservico.enums.PrioridadeOrdemServico;
import br.com.avcar.oficina.core.dto.BaseDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemServicoDTO extends BaseDTO {

    private Long idCliente;
    private String nomeCliente;
    private Long idVeiculo;
    private String placaVeiculo;
    private String descricaoVeiculo;
    private String numeroOs;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataAprovacao;
    private LocalDateTime dataFinalizacao;
    private PrioridadeOrdemServico prioridade;
    private BigDecimal valorTotal;
    private String observacao;
    private String statusAtual;
    /**
     * Função: Representa ou apoia os dados usados na operação array list.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    private List<HistoricoStatusOrdemDTO> historicoStatus = new ArrayList<>();
    /**
     * Função: Representa ou apoia os dados usados na operação array list.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    private List<ItemServicoDTO> itensServico = new ArrayList<>();
}
