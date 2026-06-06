package br.com.avcar.oficina.business.servico.model;

import br.com.avcar.oficina.core.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * Cadastro geral dos serviços prestados pela oficina.
 *
 * Regra de negócio: a classificação real do serviço é registrada nas
 * especializações ServicoInternoModel ou ServicoTerceirizadoModel.
 */
@Getter
@Setter
@Entity
@Table(name = "servico")
public class ServicoModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nome_servico", nullable = false, length = 150, unique = true)
    private String nomeServico;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "prazo_garantia_dias", nullable = false)
    private Integer prazoGarantiaDias = 90;

    @Column(name = "valor_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorBase = BigDecimal.ZERO;
}
