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
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Especialização exclusiva de Cliente para pessoa física.
 */
@Getter
@Setter
@Entity
@Table(name = "pessoa_fisica")
public class PessoaFisicaModel extends BaseModel {

    @Id
    @Column(name = "id_cliente", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteModel cliente;

    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(name = "rg", length = 30)
    private String rg;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
}
