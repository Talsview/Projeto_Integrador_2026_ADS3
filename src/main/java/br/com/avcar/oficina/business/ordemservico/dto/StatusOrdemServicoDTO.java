package br.com.avcar.oficina.business.ordemservico.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusOrdemServicoDTO extends BaseDTO {

    private String nomeStatus;
    private Integer ordemFluxo;
    private String descricao;
}
