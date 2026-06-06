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
 * Especialização exclusiva de Serviço para serviços encaminhados a empresa externa.
 *
 * Regra de negócio: mesmo terceirizando, a oficina continua responsável perante o cliente.
 */
@Getter
@Setter
@Entity
@Table(name = "servico_terceirizado")
public class ServicoTerceirizadoModel extends BaseModel {

    @Id
    @Column(name = "id_servico", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_servico", nullable = false)
    private ServicoModel servico;

    @Column(name = "observacao_terceirizacao", columnDefinition = "TEXT")
    private String observacaoTerceirizacao;
}
