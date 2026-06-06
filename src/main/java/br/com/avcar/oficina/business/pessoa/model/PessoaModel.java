package br.com.avcar.oficina.business.pessoa.model;

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
 * Representa os dados comuns de uma pessoa cadastrada no sistema.
 * Regra acadêmica: toda Pessoa deve estar associada a Cliente, Colaborador ou ambos.
 */
@Getter
@Setter
@Entity
@Table(name = "pessoa")
public class PessoaModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "telefone", length = 30)
    private String telefone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "endereco", length = 255)
    private String endereco;
}
