package br.com.avcar.oficina.business.pessoa.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para cadastro, atualização e consulta de funções.
 */
@Getter
@Setter
public class FuncaoDTO extends BaseDTO {

    private String nomeFuncao;
    private String descricao;
}
