package br.com.avcar.oficina.business.veiculo.model;

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
 * Representa a marca de um veículo atendido pela oficina.
 */
@Getter
@Setter
@Entity
@Table(name = "marca")
public class MarcaModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nome_marca", nullable = false, length = 100, unique = true)
    private String nomeMarca;
}
