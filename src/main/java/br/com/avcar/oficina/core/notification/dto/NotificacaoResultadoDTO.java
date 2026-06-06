package br.com.avcar.oficina.core.notification.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Resultado padronizado do envio de uma notificação interna.
 */
@Getter
@Setter
public class NotificacaoResultadoDTO {

    private Boolean entregue;
    private Boolean auditoriaRegistrada;
    private String canal;
    private String modulo;
    private String referencia;
    private String mensagemOriginal;
    private String mensagemProcessada;
    private LocalDateTime dataHoraEnvio;
    private LocalDateTime dataHoraAuditoria;
    private String observacaoAuditoria;
}
