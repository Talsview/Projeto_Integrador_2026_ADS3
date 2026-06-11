package br.com.avcar.oficina.core.service;

import br.com.avcar.oficina.core.model.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGenericService<E extends BaseModel> {

    /**
     * Função: Busca um registro ativo pelo identificador informado.
     * Uso no sistema: aplica a regra de inativação lógica nas consultas padrão.
     */
    E findByIdActive(Long id);

    /**
     * Função: Lista apenas registros ativos da entidade.
     * Uso no sistema: evita que cadastros inativados apareçam nas telas principais.
     */
    Page<E> findAllActive(Pageable pageable);

    /**
     * Função: Executa o fluxo padrão de inserção de um registro.
     * Uso no sistema: centraliza validação, persistência e ações pós-cadastro nos serviços base.
     */
    E insert(E entity);

    /**
     * Função: Executa o fluxo padrão de atualização de um registro existente.
     * Uso no sistema: mantém atualização organizada e reaproveitável nos serviços genéricos.
     */
    E update(E entity);

    /**
     * Função: Executa o fluxo padrão de remoção ou inativação conforme a regra do serviço.
     * Uso no sistema: preserva comportamento uniforme entre módulos.
     */
    void delete(Long id);
}
