package br.com.avcar.oficina.business.veiculo.model;

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
import lombok.Getter;
import lombok.Setter;

/**
 * Representa o veículo atendido pela oficina.
 *
 * Regra de negócio: o proprietário atual não fica gravado diretamente no veículo.
 * A propriedade é controlada pela entidade associativa HistoricoProprietario,
 * preservando todos os proprietários anteriores.
 */
@Getter
@Setter
@Entity
@Table(name = "veiculo")
public class VeiculoModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veiculo", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_modelo", nullable = false)
    private ModeloModel modelo;

    @Column(name = "placa", nullable = false, length = 10)
    private String placa;

    @Column(name = "chassi", length = 30)
    private String chassi;

    @Column(name = "cor", length = 50)
    private String cor;

    @Column(name = "ano_veiculo", nullable = false)
    private Integer anoVeiculo;

    @Column(name = "ano_modelo", nullable = false)
    private Integer anoModelo;

    @Column(name = "quilometragem_atual")
    private Integer quilometragemAtual = 0;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
