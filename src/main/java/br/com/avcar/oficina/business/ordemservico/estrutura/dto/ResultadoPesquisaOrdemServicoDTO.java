package br.com.avcar.oficina.business.ordemservico.estrutura.dto;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoPesquisaOrdemServicoDTO {

    private String estruturaUtilizada;
    private String algoritmoUtilizado;
    private String termoPesquisado;
    private Integer quantidadeEncontrada;
    private List<OrdemServicoResumoDTO> resultados = new ArrayList<>();
}
