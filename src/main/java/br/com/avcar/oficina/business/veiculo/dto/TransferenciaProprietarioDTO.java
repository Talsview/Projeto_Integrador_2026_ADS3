package br.com.avcar.oficina.business.veiculo.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO usado na transferência de propriedade de um veículo.
 */
@Getter
@Setter
public class TransferenciaProprietarioDTO extends BaseDTO {

    private Long novoClienteId;
    private LocalDate dataInicioPosse;
    private String observacao;
}
