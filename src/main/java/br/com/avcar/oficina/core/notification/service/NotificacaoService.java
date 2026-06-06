package br.com.avcar.oficina.core.notification.service;

import br.com.avcar.oficina.core.designpattern.decorator.Notificador;
import br.com.avcar.oficina.core.designpattern.decorator.NotificadorAuditoriaDecorator;
import br.com.avcar.oficina.core.designpattern.decorator.NotificadorOperacional;
import br.com.avcar.oficina.core.notification.dto.NotificacaoDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoResultadoDTO;
import org.springframework.stereotype.Service;

/**
 * Service de notificação interna do sistema.
 *
 * PADRÃO DE PROJETO APLICADO: DECORATOR.
 *
 * O service monta a cadeia de notificação usando um componente base
 * NotificadorOperacional e o envolve com NotificadorAuditoriaDecorator.
 * Com isso, a auditoria é adicionada sem alterar a classe responsável pelo
 * envio operacional da notificação.
 */
@Service
public class NotificacaoService {

    private final Notificador notificador;

    public NotificacaoService() {
        this.notificador = new NotificadorAuditoriaDecorator(new NotificadorOperacional());
    }

    public NotificacaoResultadoDTO notificar(NotificacaoDTO dto) {
        return notificador.notificar(dto);
    }

    public NotificacaoResultadoDTO notificarMudancaStatusOrdemServico(String numeroOs,
                                                                       String novoStatus,
                                                                       String observacao) {
        NotificacaoDTO dto = new NotificacaoDTO();
        dto.setTitulo("Alteração de status da Ordem de Serviço");
        dto.setModulo("ORDEM_SERVICO");
        dto.setReferencia(numeroOs);
        dto.setCanal("INTERNO_LOCAL");
        dto.setMensagem(montarMensagemMudancaStatus(numeroOs, novoStatus, observacao));
        return notificar(dto);
    }

    private String montarMensagemMudancaStatus(String numeroOs, String novoStatus, String observacao) {
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("A Ordem de Serviço ").append(numeroOs)
                .append(" foi alterada para o status ").append(novoStatus).append('.');
        if (observacao != null && !observacao.isBlank()) {
            mensagem.append(" Observação: ").append(observacao.trim());
        }
        return mensagem.toString();
    }
}
