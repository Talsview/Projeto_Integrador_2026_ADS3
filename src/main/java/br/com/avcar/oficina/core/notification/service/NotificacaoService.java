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

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public NotificacaoService(INotificacaoAuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
        this.notificador = new NotificadorAuditoriaDecorator(new NotificadorOperacional(), auditoriaRepository);
    }

    /**
     * Função: Envia uma notificação operacional e registra a auditoria do envio quando o decorador
     * está aplicado.
     * Uso no sistema: apoia a rastreabilidade de eventos importantes, principalmente mudanças de
     * status da OS.
     */
    public NotificacaoResultadoDTO notificar(NotificacaoDTO dto) {
        return notificador.notificar(dto);
    }

    /**
     * Função: Consulta ou altera o status operacional, registrando a evolução do processo quando
     * necessário.
     * Uso no sistema: mantém o fluxo Orçamento, Execução, Pagamento e Finalizado rastreável.
     */
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

    /**
     * Função: Consulta registros de notificacao aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<NotificacaoAuditoriaDTO> listarAuditorias(Pageable pageable) {
        return auditoriaRepository.findAllByAtivoTrueOrderByDataHoraAuditoriaDesc(pageable)
                .map(this::toDto);
    }

    /**
     * Função: Consulta registros de notificacao aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public java.util.List<NotificacaoAuditoriaDTO> listarAuditoriasPorReferencia(String referencia) {
        return auditoriaRepository.findTop20ByReferenciaIgnoreCaseAndAtivoTrueOrderByDataHoraAuditoriaDesc(referencia)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Função: Consulta ou altera o status operacional, registrando a evolução do processo quando
     * necessário.
     * Uso no sistema: mantém o fluxo Orçamento, Execução, Pagamento e Finalizado rastreável.
     */
    private String montarMensagemMudancaStatus(String numeroOs, String novoStatus, String observacao) {
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("A Ordem de Serviço ").append(numeroOs)
                .append(" foi alterada para o status ").append(novoStatus).append('.');
        if (observacao != null && !observacao.isBlank()) {
            mensagem.append(" Observação: ").append(observacao.trim());
        }
        return mensagem.toString();
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
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
