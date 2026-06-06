package br.com.avcar.oficina.core.designpattern.decorator;

import br.com.avcar.oficina.core.notification.dto.NotificacaoDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoResultadoDTO;

/**
 * PADRÃO DE PROJETO: DECORATOR.
 *
 * Papel no padrão: Decorator.
 * Mantém uma referência para outro Notificador e permite adicionar novos
 * comportamentos sem alterar a implementação base.
 */
public abstract class NotificadorDecorator implements Notificador {

    protected final Notificador notificadorDecorado;

    protected NotificadorDecorator(Notificador notificadorDecorado) {
        this.notificadorDecorado = notificadorDecorado;
    }

    @Override
    public NotificacaoResultadoDTO notificar(NotificacaoDTO notificacao) {
        return notificadorDecorado.notificar(notificacao);
    }
}
