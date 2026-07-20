package br.com.avcar.oficina.business.ordemservico.estrutura.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoPesquisaOrdemServicoDTO extends BaseDTO {

    private String estruturaUtilizada;
    private String algoritmoUtilizado;
    private String termoPesquisado;
    private Integer quantidadeEncontrada;
    /**
     * Função: Representa ou apoia os dados usados na operação array list.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    private List<OrdemServicoResumoDTO> resultados = new ArrayList<>();
}
