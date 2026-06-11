package br.com.avcar.oficina.core.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO base utilizado na comunicação entre Controller, Service e View Angular.
 */
@Getter
@Setter
public abstract class BaseDTO implements Serializable {

    private Long id;
    private Boolean ativo;
    private LocalDateTime dataHoraCriacao;
    private LocalDateTime dataHoraAtualizacao;
}
