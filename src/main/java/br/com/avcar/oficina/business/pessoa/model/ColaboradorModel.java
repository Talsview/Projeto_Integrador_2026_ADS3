package br.com.avcar.oficina.business.pessoa.model;

import br.com.avcar.oficina.business.pessoa.enums.StatusColaborador;
import br.com.avcar.oficina.core.model.BaseModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a pessoa que trabalha na oficina.
 *
 * Regra de negócio: uma Pessoa pode ser Cliente, Colaborador ou ambos.
 * Por isso, Colaborador referencia Pessoa e não replica dados pessoais.
 */
@Getter
@Setter
@Entity
@Table(name = "colaborador")
public class ColaboradorModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pessoa", nullable = false, unique = true)
    private PessoaModel pessoa;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_colaborador", nullable = false, length = 30)
    private StatusColaborador statusColaborador = StatusColaborador.ATIVO;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = false)
    /**
     * Função: Representa ou apoia os dados usados na operação array list.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    private List<ColaboradorFuncaoModel> funcoes = new ArrayList<>();
}
