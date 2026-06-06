package br.com.avcar.oficina.business.peca.model;

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
 * Cadastro das peças utilizadas pela oficina.
 *
 * Regra de negócio: a peça deve considerar marca, modelo aplicável, ano do
 * veículo e ano do modelo, pois esses dados apoiam a compra correta e a
 * rastreabilidade da peça aplicada na OS.
 */
@Getter
@Setter
@Entity
@Table(name = "peca")
public class PecaModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peca", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nome_peca", nullable = false, length = 180)
    private String nomePeca;

    @Column(name = "codigo_nacional", length = 60)
    private String codigoNacional;

    @Column(name = "marca_peca", length = 100)
    private String marcaPeca;

    @Column(name = "modelo_aplicavel", length = 100)
    private String modeloAplicavel;

    @Column(name = "ano_veiculo")
    private Integer anoVeiculo;

    @Column(name = "ano_modelo")
    private Integer anoModelo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
}
