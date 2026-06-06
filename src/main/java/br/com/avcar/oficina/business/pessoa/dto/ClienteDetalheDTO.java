package br.com.avcar.oficina.business.pessoa.dto;

import br.com.avcar.oficina.business.pessoa.enums.TipoCliente;
import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDetalheDTO extends BaseDTO {

    private Long pessoaId;
    private TipoCliente tipoCliente;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;

    private String cpf;
    private String rg;
    private LocalDate dataNascimento;

    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
}
