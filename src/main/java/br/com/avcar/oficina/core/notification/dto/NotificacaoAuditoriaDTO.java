package br.com.avcar.oficina.core.notification.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de consulta dos registros persistidos pelo Decorator de auditoria.
 */
@Getter
@Setter
public class NotificacaoAuditoriaDTO {

    private Long id;
    private String modulo;
    private String referencia;
    private String canal;
    private String mensagemOriginal;
    private String mensagemProcessada;
    private Boolean entregue;
    private Boolean auditoriaRegistrada;
    private LocalDateTime dataHoraEnvio;
    private LocalDateTime dataHoraAuditoria;
    private String observacaoAuditoria;
}
