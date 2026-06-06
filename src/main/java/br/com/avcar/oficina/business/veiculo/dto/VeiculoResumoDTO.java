package br.com.avcar.oficina.business.veiculo.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO resumido para listagem e pesquisa de veículos.
 */
@Getter
@Setter
public class VeiculoResumoDTO extends BaseDTO {

    private String placa;
    private String chassi;
    private String cor;
    private Integer anoVeiculo;
    private Integer anoModelo;
    private Integer quilometragemAtual;
    private Long marcaId;
    private String nomeMarca;
    private Long modeloId;
    private String nomeModelo;
    private Long proprietarioAtualId;
    private String nomeProprietarioAtual;
}
