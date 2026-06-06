package br.com.avcar.oficina.business.ordemservico.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoricoStatusOrdemDTO extends BaseDTO {

    private Long idOrdemServico;
    private Long idStatusOrdemServico;
    private String nomeStatus;
    private Integer ordemFluxo;
    private LocalDateTime dataStatus;
    private String observacao;
}
