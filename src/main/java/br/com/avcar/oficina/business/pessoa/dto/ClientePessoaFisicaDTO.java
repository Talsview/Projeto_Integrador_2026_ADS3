package br.com.avcar.oficina.business.pessoa.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO usado no cadastro e atualização de Cliente Pessoa Física.
 * O campo id herdado de BaseDTO representa o id_cliente.
 */
@Getter
@Setter
public class ClientePessoaFisicaDTO extends BaseDTO {

    private Long pessoaId;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;
}
