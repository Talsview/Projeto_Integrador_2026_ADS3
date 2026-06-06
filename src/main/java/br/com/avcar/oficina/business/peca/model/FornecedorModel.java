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
 * Fornecedor responsável por peças utilizadas nas Ordens de Serviço.
 *
 * Regra de negócio: toda peça aplicada em OS deve possuir fornecedor
 * identificado, pois a garantia da peça pode ser responsabilidade do fornecedor.
 */
@Getter
@Setter
@Entity
@Table(name = "fornecedor")
public class FornecedorModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nome_fornecedor", nullable = false, length = 180)
    private String nomeFornecedor;

    @Column(name = "cnpj", length = 18)
    private String cnpj;

    @Column(name = "telefone", length = 30)
    private String telefone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "endereco", length = 255)
    private String endereco;
}
