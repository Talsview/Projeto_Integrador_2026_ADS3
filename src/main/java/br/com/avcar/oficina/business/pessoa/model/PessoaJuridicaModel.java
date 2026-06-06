package br.com.avcar.oficina.business.pessoa.model;

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
 * Especialização exclusiva de Cliente para pessoa jurídica.
 */
@Getter
@Setter
@Entity
@Table(name = "pessoa_juridica")
public class PessoaJuridicaModel extends BaseModel {

    @Id
    @Column(name = "id_cliente", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteModel cliente;

    @Column(name = "cnpj", nullable = false, length = 18, unique = true)
    private String cnpj;

    @Column(name = "razao_social", nullable = false, length = 180)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 180)
    private String nomeFantasia;

    @Column(name = "inscricao_estadual", length = 40)
    private String inscricaoEstadual;
}
