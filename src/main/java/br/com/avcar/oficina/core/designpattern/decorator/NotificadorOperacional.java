package br.com.avcar.oficina.core.designpattern.decorator;

import br.com.avcar.oficina.core.notification.dto.NotificacaoDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoResultadoDTO;
import java.time.LocalDateTime;

/**
 * PADRÃO DE PROJETO: DECORATOR.
 *
 * Papel no padrão: ConcreteComponent.
 * Implementa o comportamento básico de envio de uma notificação interna.
 *
 * Observação acadêmica: neste projeto, a notificação é local e operacional,
 * evitando dependência obrigatória de internet ou serviços externos.
 */
public class NotificadorOperacional implements Notificador {

    @Override
    /**
     * Função: Envia uma notificação operacional e registra a auditoria do envio quando o decorador
     * está aplicado.
     * Uso no sistema: apoia a rastreabilidade de eventos importantes, principalmente mudanças de
     * status da OS.
     */
    public NotificacaoResultadoDTO notificar(NotificacaoDTO notificacao) {
        NotificacaoResultadoDTO resultado = new NotificacaoResultadoDTO();
        resultado.setEntregue(Boolean.TRUE);
        resultado.setAuditoriaRegistrada(Boolean.FALSE);
        resultado.setCanal(valorOuPadrao(notificacao.getCanal(), "INTERNO_LOCAL"));
        resultado.setModulo(valorOuPadrao(notificacao.getModulo(), "SISTEMA"));
        resultado.setReferencia(valorOuPadrao(notificacao.getReferencia(), "SEM_REFERENCIA"));
        resultado.setMensagemOriginal(notificacao.getMensagem());
        resultado.setMensagemProcessada(formatarMensagem(notificacao));
        resultado.setDataHoraEnvio(LocalDateTime.now());
        return resultado;
    }

    /**
     * Função: Padroniza uma mensagem antes de enviá-la como resposta da API.
     * Uso no sistema: mantém retornos simples e consistentes para o frontend.
     */
    private String formatarMensagem(NotificacaoDTO notificacao) {
        String titulo = valorOuPadrao(notificacao.getTitulo(), "Notificação do sistema");
        String mensagem = valorOuPadrao(notificacao.getMensagem(), "Sem mensagem informada.");
        return titulo + " - " + mensagem;
    }

    /**
     * Função: Retorna o valor informado ou um valor padrão quando o campo está vazio.
     * Uso no sistema: evita nulos em mensagens e respostas simples da API.
     */
    private String valorOuPadrao(String valor, String padrao) {
        return valor == null || valor.isBlank() ? padrao : valor.trim();
    }
}
