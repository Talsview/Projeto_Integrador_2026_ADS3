package br.com.avcar.oficina.business.ordemservico.model;

import br.com.avcar.oficina.core.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade associativa que registra a evolução dos status da OS.
 */
@Getter
@Setter
@Entity
@Table(name = "historico_status_ordem")
public class HistoricoStatusOrdemModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historico_status_ordem", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ordem_servico", nullable = false)
    private OrdemServicoModel ordemServico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_status_ordem_servico", nullable = false)
    private StatusOrdemServicoModel statusOrdemServico;

    @Column(name = "data_status", nullable = false)
    private LocalDateTime dataStatus = LocalDateTime.now();

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
