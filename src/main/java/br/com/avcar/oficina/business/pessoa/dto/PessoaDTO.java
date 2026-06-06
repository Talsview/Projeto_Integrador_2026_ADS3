package br.com.avcar.oficina.business.pessoa.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaDTO extends BaseDTO {

    private String nome;
    private String telefone;
    private String email;
    private String endereco;
}
