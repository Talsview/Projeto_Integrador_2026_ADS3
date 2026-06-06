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

    NotificacaoResultadoDTO notificar(NotificacaoDTO notificacao);
}
