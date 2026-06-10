package br.com.avcar.oficina.core.designpattern.decorator;

import br.com.avcar.oficina.core.notification.dto.NotificacaoDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoResultadoDTO;
import br.com.avcar.oficina.core.notification.model.NotificacaoAuditoriaModel;
import br.com.avcar.oficina.core.notification.repository.INotificacaoAuditoriaRepository;
import java.time.LocalDateTime;

/**
 * PADRÃO DE PROJETO: DECORATOR.
 *
 * Papel no padrão: ConcreteDecorator.
 * Acrescenta auditoria persistente ao resultado da notificação sem modificar a
 * classe NotificadorOperacional.
 *
 * Aplicação reformulada no sistema: toda notificação relevante, como alteração
 * de status da Ordem de Serviço, passa a gerar um registro na tabela
 * notificacao_auditoria. Assim, o Decorator agrega rastreabilidade real ao
 * sistema e deixa de ser apenas um exemplo demonstrativo.
 */
public class NotificadorAuditoriaDecorator extends NotificadorDecorator {

    private final INotificacaoAuditoriaRepository auditoriaRepository;

    public NotificadorAuditoriaDecorator(Notificador notificadorDecorado,
                                         INotificacaoAuditoriaRepository auditoriaRepository) {
        super(notificadorDecorado);
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
    public NotificacaoResultadoDTO notificar(NotificacaoDTO notificacao) {
        NotificacaoResultadoDTO resultado = super.notificar(notificacao);
        resultado.setDataHoraAuditoria(LocalDateTime.now());
        resultado.setObservacaoAuditoria(montarObservacaoAuditoria(resultado));

        NotificacaoAuditoriaModel auditoria = montarAuditoria(resultado);
        auditoria = auditoriaRepository.saveAndFlush(auditoria);

        resultado.setIdAuditoria(auditoria.getId());
        resultado.setAuditoriaRegistrada(Boolean.TRUE);
        return resultado;
    }

    private NotificacaoAuditoriaModel montarAuditoria(NotificacaoResultadoDTO resultado) {
        NotificacaoAuditoriaModel auditoria = new NotificacaoAuditoriaModel();
        auditoria.setModulo(resultado.getModulo());
        auditoria.setReferencia(resultado.getReferencia());
        auditoria.setCanal(resultado.getCanal());
        auditoria.setMensagemOriginal(resultado.getMensagemOriginal());
        auditoria.setMensagemProcessada(resultado.getMensagemProcessada());
        auditoria.setEntregue(resultado.getEntregue());
        auditoria.setAuditoriaRegistrada(Boolean.TRUE);
        auditoria.setDataHoraEnvio(resultado.getDataHoraEnvio());
        auditoria.setDataHoraAuditoria(resultado.getDataHoraAuditoria());
        auditoria.setObservacaoAuditoria(resultado.getObservacaoAuditoria());
        return auditoria;
    }

    private String montarObservacaoAuditoria(NotificacaoResultadoDTO resultado) {
        return "Notificação persistida para rastreabilidade no módulo " + resultado.getModulo()
                + " e referência " + resultado.getReferencia() + ".";
    }
}
