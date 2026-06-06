package br.com.avcar.oficina.business.pessoa.dto;

import br.com.avcar.oficina.business.pessoa.enums.StatusColaborador;
import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColaboradorResumoDTO extends BaseDTO {

    private Long pessoaId;
    private String nome;
    private String telefone;
    private String email;
    private LocalDate dataAdmissao;
    private StatusColaborador statusColaborador;
    private String funcoes;
}
