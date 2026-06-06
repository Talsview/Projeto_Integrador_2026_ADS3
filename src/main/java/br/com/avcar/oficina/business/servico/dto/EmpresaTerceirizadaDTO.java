package br.com.avcar.oficina.business.servico.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para cadastro e consulta de empresas terceirizadas.
 */
@Getter
@Setter
public class EmpresaTerceirizadaDTO extends BaseDTO {

    private String nomeEmpresa;
    private String cnpj;
    private String telefone;
    private String email;
    private String endereco;
}
