package br.com.avcar.oficina.business.garantia.dto;

import br.com.avcar.oficina.business.garantia.enums.StatusGarantia;
import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para consulta e manutenção da garantia de serviço executado em OS.
 */
@Getter
@Setter
public class GarantiaServicoDTO extends BaseDTO {

    private Long idItemServico;
    private Long idOrdemServico;
    private Long idServico;
    private String nomeServico;
    private Long idColaborador;
    private String nomeColaboradorResponsavel;
    private Integer prazoDias;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusGarantia statusGarantia;
    private LocalDate dataAcionamento;
    private String motivoAcionamento;
    private String descricaoDefeito;
    private String responsavelAnalise;
    private LocalDate dataEncerramento;
    private String solucaoAplicada;
    private String custoAssumidoPor;
    private Boolean atendimentoRealizado;
    private String observacao;
}

