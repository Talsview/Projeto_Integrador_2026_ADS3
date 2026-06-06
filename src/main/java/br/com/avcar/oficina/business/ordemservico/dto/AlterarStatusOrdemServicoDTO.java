package br.com.avcar.oficina.business.ordemservico.dto;

import br.com.avcar.oficina.business.ordemservico.enums.StatusFluxoOrdemServico;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarStatusOrdemServicoDTO {

    private StatusFluxoOrdemServico novoStatus;
    private String observacao;
}
