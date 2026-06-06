package br.com.avcar.oficina.business.pessoa.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO usado no cadastro e atualização de Cliente Pessoa Jurídica.
 * O campo id herdado de BaseDTO representa o id_cliente.
 */
@Getter
@Setter
public class ClientePessoaJuridicaDTO extends BaseDTO {

    private Long pessoaId;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
}
