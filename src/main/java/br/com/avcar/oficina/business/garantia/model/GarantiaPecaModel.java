package br.com.avcar.oficina.business.garantia.model;

import br.com.avcar.oficina.business.garantia.enums.ResponsabilidadeGarantiaPeca;
import br.com.avcar.oficina.business.garantia.enums.StatusGarantia;
import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Garantia vinculada à peça aplicada na Ordem de Serviço.
 *
 * Regra de negócio: toda ItemPeca gera GarantiaPeca. A contagem da garantia
 * somente começa após a finalização da OS. Mesmo quando a responsabilidade
 * técnica for do fornecedor, a oficina mantém o atendimento ao cliente.
 */
@Getter
@Setter
@Entity
@Table(name = "garantia_peca")
public class GarantiaPecaModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_garantia_peca", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_item_peca", nullable = false, unique = true)
    private ItemPecaModel itemPeca;

    @Column(name = "prazo_dias", nullable = false)
    private Integer prazoDias;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "responsabilidade", nullable = false, length = 30)
    private ResponsabilidadeGarantiaPeca responsabilidade = ResponsabilidadeGarantiaPeca.FORNECEDOR;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_garantia", nullable = false, length = 30)
    private StatusGarantia statusGarantia = StatusGarantia.AGUARDANDO_FINALIZACAO_OS;


    @Column(name = "data_acionamento")
    private LocalDate dataAcionamento;

    @Column(name = "motivo_acionamento", length = 255)
    private String motivoAcionamento;

    @Column(name = "descricao_defeito", columnDefinition = "TEXT")
    private String descricaoDefeito;

    @Column(name = "responsavel_analise", length = 150)
    private String responsavelAnalise;

    @Column(name = "data_encerramento")
    private LocalDate dataEncerramento;

    @Column(name = "solucao_aplicada", columnDefinition = "TEXT")
    private String solucaoAplicada;

    @Column(name = "custo_assumido_por", length = 80)
    private String custoAssumidoPor;

    @Column(name = "atendimento_realizado")
    private Boolean atendimentoRealizado;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
