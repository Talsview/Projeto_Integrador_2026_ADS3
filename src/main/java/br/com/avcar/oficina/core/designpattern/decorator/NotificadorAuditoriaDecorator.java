package br.com.avcar.oficina.core.designpattern.decorator;

import br.com.avcar.oficina.core.notification.dto.NotificacaoDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoResultadoDTO;
import java.time.LocalDateTime;

/**
 * PADRÃO DE PROJETO: DECORATOR.
 *
 * Papel no padrão: ConcreteDecorator.
 * Acrescenta auditoria ao resultado da notificação sem modificar a classe
 * NotificadorOperacional.
 *
 * Aplicação no sistema: registrar que uma alteração operacional importante,
 * como mudança de status de Ordem de Serviço, gerou notificação auditável.
 */
public class NotificadorAuditoriaDecorator extends NotificadorDecorator {

    public NotificadorAuditoriaDecorator(Notificador notificadorDecorado) {
        super(notificadorDecorado);
    }

    @Override
    public NotificacaoResultadoDTO notificar(NotificacaoDTO notificacao) {
        NotificacaoResultadoDTO resultado = super.notificar(notificacao);
        resultado.setAuditoriaRegistrada(Boolean.TRUE);
        resultado.setDataHoraAuditoria(LocalDateTime.now());
        resultado.setObservacaoAuditoria(montarObservacaoAuditoria(resultado));
        return resultado;
    }

    private String montarObservacaoAuditoria(NotificacaoResultadoDTO resultado) {
        return "Notificação auditada no módulo " + resultado.getModulo()
                + " para a referência " + resultado.getReferencia() + ".";
    }
}
