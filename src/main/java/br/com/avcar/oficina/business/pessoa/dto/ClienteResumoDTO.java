package br.com.avcar.oficina.business.pessoa.dto;

import br.com.avcar.oficina.business.pessoa.enums.TipoCliente;
import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResumoDTO extends BaseDTO {

    private Long pessoaId;
    private TipoCliente tipoCliente;
    private String nome;
    private String documento;
    private String telefone;
    private String email;
}
