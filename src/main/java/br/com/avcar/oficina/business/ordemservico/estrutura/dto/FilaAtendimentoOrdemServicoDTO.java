package br.com.avcar.oficina.business.ordemservico.estrutura.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilaAtendimentoOrdemServicoDTO extends BaseDTO {

    private String estruturaUtilizada;
    private String justificativa;
    private Integer quantidadeNaFila;
    /**
     * Função: Representa ou apoia os dados usados na operação array list.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    private List<OrdemServicoResumoDTO> ordens = new ArrayList<>();
}
