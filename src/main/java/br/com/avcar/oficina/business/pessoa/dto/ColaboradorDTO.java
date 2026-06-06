package br.com.avcar.oficina.business.pessoa.dto;

import br.com.avcar.oficina.business.pessoa.enums.StatusColaborador;
import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para cadastro e atualização de colaborador.
 *
 * O campo pessoaId permite reaproveitar uma Pessoa já cadastrada como Cliente,
 * mantendo a regra de generalização compartilhada entre Cliente e Colaborador.
 */
@Getter
@Setter
public class ColaboradorDTO extends BaseDTO {

    private Long pessoaId;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private LocalDate dataAdmissao;
    private StatusColaborador statusColaborador;
    private List<Long> funcoesIds = new ArrayList<>();
    private List<ColaboradorFuncaoDTO> funcoes = new ArrayList<>();
}
