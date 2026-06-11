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

    /**
     * Função: Recebe o notificador original e o repositório de auditoria que serão usados para
     * complementar a notificação.
     * Padrão aplicado: DECORATOR.
     * Justificativa: permite acrescentar persistência de auditoria sem alterar a classe concreta que
     * envia a notificação operacional.
     * Uso no sistema: registra evidências de notificações ligadas principalmente à mudança de status
     * da Ordem de Serviço.
     */
    public NotificadorAuditoriaDecorator(Notificador notificadorDecorado,
                                         INotificacaoAuditoriaRepository auditoriaRepository) {
        super(notificadorDecorado);
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
    /**
     * Função: Envia uma notificação operacional e registra a auditoria do envio quando o decorador
     * está aplicado.
     * Uso no sistema: apoia a rastreabilidade de eventos importantes, principalmente mudanças de
     * status da OS.
     */
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

    /**
     * Função: Transforma o resultado da notificação em uma entidade de auditoria pronta para gravação
     * no banco.
     * Padrão aplicado: DECORATOR.
     * Justificativa: mantém a responsabilidade adicional do Decorator organizada e separada da regra
     * de envio da notificação.
     * Uso no sistema: registra evidências de notificações ligadas principalmente à mudança de status
     * da Ordem de Serviço.
     */
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

    /**
     * Função: Monta uma observação textual indicando o módulo e a referência operacional auditada.
     * Padrão aplicado: DECORATOR.
     * Justificativa: enriquece a notificação decorada com informação útil para rastrear mudanças de
     * status da OS.
     * Uso no sistema: registra evidências de notificações ligadas principalmente à mudança de status
     * da Ordem de Serviço.
     */
    private String montarObservacaoAuditoria(NotificacaoResultadoDTO resultado) {
        return "Notificação persistida para rastreabilidade no módulo " + resultado.getModulo()
                + " e referência " + resultado.getReferencia() + ".";
    }
}
