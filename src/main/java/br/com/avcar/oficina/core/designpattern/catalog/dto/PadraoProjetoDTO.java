package br.com.avcar.oficina.core.designpattern.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO de documentação técnica dos padrões de projeto aplicados no sistema.
 */
@Getter
@AllArgsConstructor
public class PadraoProjetoDTO {

    private String padrao;
    private String localAplicacao;
    private String classePrincipal;
    private String justificativa;
    private String evidenciaFuncional;
}
