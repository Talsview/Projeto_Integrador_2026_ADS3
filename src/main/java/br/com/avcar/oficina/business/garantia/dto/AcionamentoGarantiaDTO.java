package br.com.avcar.oficina.business.garantia.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para registrar observações de acionamento ou encerramento de garantia.
 */
@Getter
@Setter
public class AcionamentoGarantiaDTO {

    private String observacao;
}
