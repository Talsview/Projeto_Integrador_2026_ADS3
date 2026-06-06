package br.com.avcar.oficina.business.ordemservico.model;

import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import br.com.avcar.oficina.core.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Registro da execução externa quando o ItemServico representa serviço
 * terceirizado.
 *
 * Regra de negócio: mesmo terceirizando, a oficina continua responsável
 * perante o cliente.
 */
@Getter
@Setter
@Entity
@Table(name = "execucao_servico_terceirizado")
public class ExecucaoServicoTerceirizadoModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_execucao_servico_terceirizado", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_item_servico", nullable = false, unique = true)
    private ItemServicoModel itemServico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa_terceirizada", nullable = false)
    private EmpresaTerceirizadaModel empresaTerceirizada;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "data_retorno")
    private LocalDateTime dataRetorno;

    @Column(name = "valor_cobrado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorCobrado = BigDecimal.ZERO;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
