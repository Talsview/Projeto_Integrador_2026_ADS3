package br.com.avcar.oficina.business.garantia.model;

import br.com.avcar.oficina.business.garantia.enums.StatusGarantia;
import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
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
 * Garantia vinculada ao serviço executado na Ordem de Serviço.
 *
 * Regra de negócio: todo ItemServico gera GarantiaServico, com prazo variável
 * conforme o tipo de serviço cadastrado.
 */
@Getter
@Setter
@Entity
@Table(name = "garantia_servico")
public class GarantiaServicoModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_garantia_servico", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_item_servico", nullable = false, unique = true)
    private ItemServicoModel itemServico;

    @Column(name = "prazo_dias", nullable = false)
    private Integer prazoDias;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_garantia", nullable = false, length = 30)
    private StatusGarantia statusGarantia = StatusGarantia.AGUARDANDO_FINALIZACAO_OS;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
