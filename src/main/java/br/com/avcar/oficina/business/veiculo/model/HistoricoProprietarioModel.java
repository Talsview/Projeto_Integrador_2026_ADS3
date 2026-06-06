package br.com.avcar.oficina.business.veiculo.model;

import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
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
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade associativa entre Cliente e Veiculo.
 *
 * Justificativa de modelagem: o relacionamento possui dados próprios, como
 * período de posse e indicação de proprietário atual. Por isso, foi transformado
 * em entidade associativa para garantir rastreabilidade histórica.
 */
@Getter
@Setter
@Entity
@Table(name = "historico_proprietario")
public class HistoricoProprietarioModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historico_proprietario", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteModel cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_veiculo", nullable = false)
    private VeiculoModel veiculo;

    @Column(name = "data_inicio_posse", nullable = false)
    private LocalDate dataInicioPosse;

    @Column(name = "data_fim_posse")
    private LocalDate dataFimPosse;

    @Column(name = "proprietario_atual", nullable = false)
    private Boolean proprietarioAtual = Boolean.TRUE;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
