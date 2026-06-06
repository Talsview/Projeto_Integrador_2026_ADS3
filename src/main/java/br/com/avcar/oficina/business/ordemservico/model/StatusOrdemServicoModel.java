package br.com.avcar.oficina.business.ordemservico.model;

import br.com.avcar.oficina.core.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Cadastro dos status possíveis da Ordem de Serviço.
 *
 * Regra de negócio: o fluxo oficial é Orçamento, Execução,
 * Pagamento e Finalizado, controlado pela coluna ordemFluxo.
 */
@Getter
@Setter
@Entity
@Table(name = "status_ordem_servico")
public class StatusOrdemServicoModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_ordem_servico", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nome_status", nullable = false, length = 40, unique = true)
    private String nomeStatus;

    @Column(name = "ordem_fluxo", nullable = false, unique = true)
    private Integer ordemFluxo;

    @Column(name = "descricao", length = 255)
    private String descricao;
}
