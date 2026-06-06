package br.com.avcar.oficina.business.ordemservico.estrutura.dto;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilaAtendimentoOrdemServicoDTO {

    private String estruturaUtilizada;
    private String justificativa;
    private Integer quantidadeNaFila;
    private List<OrdemServicoResumoDTO> ordens = new ArrayList<>();
}
