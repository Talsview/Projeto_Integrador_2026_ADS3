package br.com.avcar.oficina.business.pagamento.model;

import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.pagamento.enums.FormaPagamento;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.core.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Registro de pagamento gerado por uma Ordem de Serviço.
 *
 * Regra de negócio: uma OrdemServico pode gerar nenhum, um ou vários pagamentos.
 * Apenas pagamentos com status PAGO compõem a quitação financeira da OS.
 */
@Getter
@Setter
@Entity
@Table(name = "pagamento")
public class PagamentoModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ordem_servico", nullable = false)
    private OrdemServicoModel ordemServico;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 40)
    private FormaPagamento formaPagamento;

    @Column(name = "valor_pago", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorPago = BigDecimal.ZERO;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 30)
    private StatusPagamento statusPagamento = StatusPagamento.PENDENTE;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
