package br.com.avcar.oficina.business.garantia.dto;

import br.com.avcar.oficina.business.garantia.enums.ResponsabilidadeGarantiaPeca;
import br.com.avcar.oficina.business.garantia.enums.StatusGarantia;
import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para consulta e manutenção da garantia de peça aplicada em OS.
 */
@Getter
@Setter
public class GarantiaPecaDTO extends BaseDTO {

    private Long idItemPeca;
    private Long idOrdemServico;
    private Long idPeca;
    private String nomePeca;
    private String codigoNacional;
    private Long idFornecedor;
    private String nomeFornecedor;
    private Integer prazoDias;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private ResponsabilidadeGarantiaPeca responsabilidade;
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

