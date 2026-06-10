package br.com.avcar.oficina.core.notification.service;

import br.com.avcar.oficina.core.designpattern.decorator.Notificador;
import br.com.avcar.oficina.core.designpattern.decorator.NotificadorAuditoriaDecorator;
import br.com.avcar.oficina.core.designpattern.decorator.NotificadorOperacional;
import br.com.avcar.oficina.core.notification.dto.NotificacaoAuditoriaDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoResultadoDTO;
import br.com.avcar.oficina.core.notification.model.NotificacaoAuditoriaModel;
import br.com.avcar.oficina.core.notification.repository.INotificacaoAuditoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service de notificação interna do sistema.
 *
 * PADRÃO DE PROJETO APLICADO: DECORATOR.
 *
 * O service monta a cadeia de notificação usando um componente base
 * NotificadorOperacional e o envolve com NotificadorAuditoriaDecorator.
 * A reformulação faz o decorador persistir a auditoria em banco, tornando o
 * padrão útil para rastrear eventos operacionais, especialmente mudanças de
 * status da Ordem de Serviço.
 */
@Service
public class NotificacaoService {

    private final Notificador notificador;
    private final INotificacaoAuditoriaRepository auditoriaRepository;

    public NotificacaoService(INotificacaoAuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
        this.notificador = new NotificadorAuditoriaDecorator(new NotificadorOperacional(), auditoriaRepository);
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

    public Page<NotificacaoAuditoriaDTO> listarAuditorias(Pageable pageable) {
        return auditoriaRepository.findAllByAtivoTrueOrderByDataHoraAuditoriaDesc(pageable)
                .map(this::toDto);
    }

    public java.util.List<NotificacaoAuditoriaDTO> listarAuditoriasPorReferencia(String referencia) {
        return auditoriaRepository.findTop20ByReferenciaIgnoreCaseAndAtivoTrueOrderByDataHoraAuditoriaDesc(referencia)
                .stream()
                .map(this::toDto)
                .toList();
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

    private NotificacaoAuditoriaDTO toDto(NotificacaoAuditoriaModel model) {
        NotificacaoAuditoriaDTO dto = new NotificacaoAuditoriaDTO();
        dto.setId(model.getId());
        dto.setModulo(model.getModulo());
        dto.setReferencia(model.getReferencia());
        dto.setCanal(model.getCanal());
        dto.setMensagemOriginal(model.getMensagemOriginal());
        dto.setMensagemProcessada(model.getMensagemProcessada());
        dto.setEntregue(model.getEntregue());
        dto.setAuditoriaRegistrada(model.getAuditoriaRegistrada());
        dto.setDataHoraEnvio(model.getDataHoraEnvio());
        dto.setDataHoraAuditoria(model.getDataHoraAuditoria());
        dto.setObservacaoAuditoria(model.getObservacaoAuditoria());
        return dto;
    }
}
