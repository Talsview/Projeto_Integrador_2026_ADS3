package br.com.avcar.oficina.business.pessoa.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO da entidade associativa ColaboradorFuncao.
 */
@Getter
@Setter
public class ColaboradorFuncaoDTO extends BaseDTO {

    private Long colaboradorId;
    private Long funcaoId;
    private String nomeFuncao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}
