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
 * Representa uma função exercida por colaborador.
 *
 * Observação acadêmica: Mecânico, Atendente, Secretária, Faxineiro,
 * Estoquista e Gerente não devem ser entidades separadas. Esses papéis
 * devem ser cadastrados como registros desta entidade Funcao.
 */
@Getter
@Setter
@Entity
@Table(name = "funcao")
public class FuncaoModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcao", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nome_funcao", nullable = false, length = 100, unique = true)
    private String nomeFuncao;

    @Column(name = "descricao", length = 255)
    private String descricao;
}
