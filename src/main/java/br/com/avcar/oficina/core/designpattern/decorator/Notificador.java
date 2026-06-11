package br.com.avcar.oficina.core.designpattern.decorator;

import br.com.avcar.oficina.core.notification.dto.NotificacaoDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoResultadoDTO;

/**
 * PADRÃO DE PROJETO: DECORATOR.
 *
 * Papel no padrão: Component.
 * Define a operação comum para envio de notificações internas do sistema.
 */
public interface Notificador {

    /**
     * Função: Envia uma notificação operacional e registra a auditoria do envio quando o decorador
     * está aplicado.
     * Uso no sistema: apoia a rastreabilidade de eventos importantes, principalmente mudanças de
     * status da OS.
     */
    NotificacaoResultadoDTO notificar(NotificacaoDTO notificacao);
}
