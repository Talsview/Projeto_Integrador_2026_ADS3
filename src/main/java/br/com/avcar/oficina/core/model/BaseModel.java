package br.com.avcar.oficina.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe base de todas as entidades persistentes do sistema.
 *
 * Decisão arquitetural: o identificador padrão do sistema passa a ser Long,
 * conforme orientação recebida no projeto. Cada entidade concreta declara sua
 * própria coluna de chave primária, como id_pessoa, id_cliente ou id_veiculo,
 * mantendo fidelidade ao modelo físico da oficina.
 *
 * A exclusão física não deve ser usada para registros operacionais da oficina;
 * por isso, o campo ativo permite exclusão lógica e preservação de histórico.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel implements Serializable {

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = Boolean.TRUE;

    @Column(name = "data_hora_criacao", nullable = false, updatable = false)
    private LocalDateTime dataHoraCriacao;

    @Column(name = "data_hora_atualizacao")
    private LocalDateTime dataHoraAtualizacao;

    /**
     * Função: Representa ou apoia os dados usados na operação get id.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    public abstract Long getId();

    /**
     * Função: Representa ou apoia os dados usados na operação set id.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    public abstract void setId(Long id);

    @PrePersist
    /**
     * Função: Representa ou apoia os dados usados na operação pre persist.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    public void prePersist() {
        if (this.ativo == null) {
            this.ativo = Boolean.TRUE;
        }
        this.dataHoraCriacao = LocalDateTime.now();
        this.dataHoraAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    /**
     * Função: Representa ou apoia os dados usados na operação pre update.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    public void preUpdate() {
        this.dataHoraAtualizacao = LocalDateTime.now();
    }
}
