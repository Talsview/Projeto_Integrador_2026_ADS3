package br.com.avcar.oficina.business.peca.model;

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
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade associativa que registra a peça aplicada em uma Ordem de Serviço.
 *
 * Regra de negócio: a OrdemServico utiliza ItemPeca; o ItemPeca é vinculado
 * obrigatoriamente a uma Peca cadastrada e a um Fornecedor identificado.
 * O vínculo com OrdemServico será convertido para relacionamento JPA direto
 * quando o módulo de OS for implementado.
 */
@Getter
@Setter
@Entity
@Table(name = "item_peca")
public class ItemPecaModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_peca", nullable = false, updatable = false)
    private Long id;

    @Column(name = "id_ordem_servico", nullable = false)
    private Long idOrdemServico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_peca", nullable = false)
    private PecaModel peca;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_fornecedor", nullable = false)
    private FornecedorModel fornecedor;

    @Column(name = "quantidade", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade = BigDecimal.ONE;

    @Column(name = "valor_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorUnitario = BigDecimal.ZERO;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
