package br.com.avcar.oficina.business.ordemservico.estrutura.dto;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.business.ordemservico.estrutura.enums.CriterioOrdenacaoOrdemServico;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoOrdenacaoOrdemServicoDTO {

    private String algoritmoUtilizado;
    private String padraoProjetoAplicado;
    private CriterioOrdenacaoOrdemServico criterio;
    private Integer quantidadeOrdenada;
    private List<OrdemServicoResumoDTO> ordens = new ArrayList<>();
}
