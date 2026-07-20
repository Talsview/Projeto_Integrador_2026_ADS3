package br.com.avcar.oficina.core.service;

import br.com.avcar.oficina.core.dto.BaseDTO;

/**
 * Base genérica para componentes de negócio que não executam CRUD diretamente.
 *
 * <p>Algumas classes da camada business são componentes auxiliares de regra,
 * como calculadoras, geradores e algoritmos acadêmicos. Elas não devem herdar
 * GenericService quando não possuem repository próprio, mas ainda podem explicitar
 * o uso de Generics no core informando o tipo de DTO/resultado que apoiam.</p>
 */
public abstract class GenericBusinessComponent<R extends BaseDTO> {

    /**
     * Retorna o tipo conceitual de resultado produzido pelo componente.
     */
    protected Class<R> tipoResultado() {
        return null;
    }
}
