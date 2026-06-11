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
    /**
     * Função: Representa ou apoia os dados usados na operação array list.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    private List<Long> funcoesIds = new ArrayList<>();
    /**
     * Função: Representa ou apoia os dados usados na operação array list.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    private List<ColaboradorFuncaoDTO> funcoes = new ArrayList<>();
}
