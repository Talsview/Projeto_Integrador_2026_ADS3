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

    /**
     * Função: Recebe o notificador original e o repositório de auditoria que serão usados para
     * complementar a notificação.
     * Padrão aplicado: DECORATOR.
     * Justificativa: permite acrescentar persistência de auditoria sem alterar a classe concreta que
     * envia a notificação operacional.
     * Uso no sistema: registra evidências de notificações ligadas principalmente à mudança de status
     * da Ordem de Serviço.
     */
    protected NotificadorDecorator(Notificador notificadorDecorado) {
        this.notificadorDecorado = notificadorDecorado;
    }

    @Override
    /**
     * Função: Envia uma notificação operacional e registra a auditoria do envio quando o decorador
     * está aplicado.
     * Uso no sistema: apoia a rastreabilidade de eventos importantes, principalmente mudanças de
     * status da OS.
     */
    public NotificacaoResultadoDTO notificar(NotificacaoDTO notificacao) {
        return notificadorDecorado.notificar(notificacao);
    }
}
