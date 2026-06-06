package br.com.avcar.oficina.business.veiculo.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para cadastro, atualização e consulta de marcas de veículos.
 */
@Getter
@Setter
public class MarcaDTO extends BaseDTO {

    private String nomeMarca;
}
