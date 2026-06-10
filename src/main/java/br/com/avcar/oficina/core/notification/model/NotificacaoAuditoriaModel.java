package br.com.avcar.oficina.core.notification.model;

import br.com.avcar.oficina.core.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Registro persistente de notificações operacionais auditadas.
 *
 * Finalidade: dar rastreabilidade real às notificações geradas por eventos
 * relevantes do sistema, especialmente mudanças de status da Ordem de Serviço.
 */
@Getter
@Setter
@Entity
@Table(name = "notificacao_auditoria")
public class NotificacaoAuditoriaModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacao_auditoria", nullable = false, updatable = false)
    private Long id;

    @Column(name = "modulo", nullable = false, length = 80)
    private String modulo;

    @Column(name = "referencia", nullable = false, length = 80)
    private String referencia;

    @Column(name = "canal", nullable = false, length = 60)
    private String canal;

    @Column(name = "mensagem_original", columnDefinition = "TEXT")
    private String mensagemOriginal;

    @Column(name = "mensagem_processada", nullable = false, columnDefinition = "TEXT")
    private String mensagemProcessada;

    @Column(name = "entregue", nullable = false)
    private Boolean entregue;

    @Column(name = "auditoria_registrada", nullable = false)
    private Boolean auditoriaRegistrada;

    @Column(name = "data_hora_envio", nullable = false)
    private LocalDateTime dataHoraEnvio;

    @Column(name = "data_hora_auditoria", nullable = false)
    private LocalDateTime dataHoraAuditoria;

    @Column(name = "observacao_auditoria", columnDefinition = "TEXT")
    private String observacaoAuditoria;
}
