package br.com.avcar.oficina.core.notification.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para solicitar notificações internas do sistema.
 *
 * A notificação é local e não depende de serviço externo, preservando a
 * exigência do projeto de funcionar sem dependência obrigatória de internet.
 */
@Getter
@Setter
public class NotificacaoDTO {

    private String titulo;
    private String mensagem;
    private String modulo;
    private String referencia;
    private String canal;
    /**
     * Função: Representa ou apoia os dados usados na operação local date time.now.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    private LocalDateTime dataHoraSolicitacao = LocalDateTime.now();
}
