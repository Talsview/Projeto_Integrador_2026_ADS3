package br.com.avcar.oficina.business.peca.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para cadastro e consulta de fornecedores de peças.
 */
@Getter
@Setter
public class FornecedorDTO extends BaseDTO {

    private String nomeFornecedor;
    private String cnpj;
    private String telefone;
    private String email;
    private String endereco;
}
