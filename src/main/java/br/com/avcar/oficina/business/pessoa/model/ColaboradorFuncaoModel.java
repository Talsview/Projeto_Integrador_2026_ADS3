package br.com.avcar.oficina.business.pessoa.model;

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
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade associativa entre Colaborador e Funcao.
 *
 * Justificativa de modelagem: o relacionamento possui dados próprios,
 * como data_inicio e data_fim, permitindo preservar histórico das funções
 * exercidas pelo colaborador ao longo do tempo.
 */
@Getter
@Setter
@Entity
@Table(name = "colaborador_funcao")
public class ColaboradorFuncaoModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador_funcao", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_colaborador", nullable = false)
    private ColaboradorModel colaborador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_funcao", nullable = false)
    private FuncaoModel funcao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio = LocalDate.now();

    @Column(name = "data_fim")
    private LocalDate dataFim;
}
