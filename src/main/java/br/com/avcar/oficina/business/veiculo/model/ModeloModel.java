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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa o modelo comercial do veículo, vinculado obrigatoriamente a uma marca.
 */
@Getter
@Setter
@Entity
@Table(name = "modelo", uniqueConstraints = {
        @UniqueConstraint(name = "uk_modelo_marca_nome", columnNames = {"id_marca", "nome_modelo"})
})
public class ModeloModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_marca", nullable = false)
    private MarcaModel marca;

    @Column(name = "nome_modelo", nullable = false, length = 100)
    private String nomeModelo;
}
