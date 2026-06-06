package br.com.avcar.oficina.business.veiculo.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para cadastro, atualização e consulta de modelos de veículos.
 */
@Getter
@Setter
public class ModeloDTO extends BaseDTO {

    private Long marcaId;
    private String nomeMarca;
    private String nomeModelo;
}
