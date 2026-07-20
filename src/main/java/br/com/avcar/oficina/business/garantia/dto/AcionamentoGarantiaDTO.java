package br.com.avcar.oficina.business.garantia.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;

import br.com.avcar.oficina.business.garantia.enums.ResponsabilidadeGarantiaPeca;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para registrar o atendimento operacional de garantia.
 *
 * O mesmo contrato é usado no acionamento e no encerramento. No acionamento,
 * são utilizados dados como motivo, defeito relatado, responsável pela análise
 * e responsabilidade. No encerramento, são utilizados dados como solução
 * aplicada, custo assumido e indicação de substituição/refação.
 */
@Getter
@Setter
public class AcionamentoGarantiaDTO extends BaseDTO {

    private LocalDate dataAcionamento;
    private String motivoAcionamento;
    private String descricaoDefeito;
    private String responsavelAnalise;
    private ResponsabilidadeGarantiaPeca responsabilidade;

    private LocalDate dataEncerramento;
    private String solucaoAplicada;
    private String custoAssumidoPor;
    private Boolean atendimentoRealizado;

    private String observacao;
}
