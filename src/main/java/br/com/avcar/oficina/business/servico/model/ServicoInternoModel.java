package br.com.avcar.oficina.business.servico.model;

import br.com.avcar.oficina.core.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Especialização exclusiva de Serviço para serviços executados pela própria oficina.
 */
@Getter
@Setter
@Entity
@Table(name = "servico_interno")
public class ServicoInternoModel extends BaseModel {

    @Id
    @Column(name = "id_servico", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_servico", nullable = false)
    private ServicoModel servico;

    @Column(name = "observacao_interna", columnDefinition = "TEXT")
    private String observacaoInterna;
}
