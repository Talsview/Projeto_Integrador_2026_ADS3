package br.com.avcar.oficina.core.service;

import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.model.BaseModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import br.com.avcar.oficina.core.validation.IGenericValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service genérico da aplicação.
 *
 * Centraliza operações comuns de CRUD e preserva pontos de extensão para os
 * Services específicos de cada módulo da oficina.
 */
public abstract class GenericService<E extends BaseModel> implements IGenericService<E> {

    protected final IGenericRepository<E> repository;
    protected final IGenericValidation<E> validation;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    protected GenericService(IGenericRepository<E> repository, IGenericValidation<E> validation) {
        this.repository = repository;
        this.validation = validation;
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Função: Busca um registro ativo pelo identificador informado.
     * Uso no sistema: aplica a regra de inativação lógica nas consultas padrão.
     */
    public E findByIdActive(Long id) {
        if (id == null) {
            throw new BusinessException("O ID é obrigatório para buscar o registro.");
        }

        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Registro não encontrado ou inativo."));
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Função: Lista apenas registros ativos da entidade.
     * Uso no sistema: evita que cadastros inativados apareçam nas telas principais.
     */
    public Page<E> findAllActive(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable);
    }

    @Override
    @Transactional
    /**
     * Função: Executa o fluxo padrão de inserção de um registro.
     * Uso no sistema: centraliza validação, persistência e ações pós-cadastro nos serviços base.
     */
    public E insert(E entity) {
        validation.validateInsert(entity);
        beforeInsert(entity);
        entity.setAtivo(Boolean.TRUE);
        E savedEntity = repository.save(entity);
        afterInsert(savedEntity);
        return savedEntity;
    }

    @Override
    @Transactional
    /**
     * Função: Executa o fluxo padrão de atualização de um registro existente.
     * Uso no sistema: mantém atualização organizada e reaproveitável nos serviços genéricos.
     */
    public E update(E entity) {
        validation.validateUpdate(entity);
        findByIdActive(entity.getId());
        beforeUpdate(entity);
        entity.setAtivo(Boolean.TRUE);
        E savedEntity = repository.save(entity);
        afterUpdate(savedEntity);
        return savedEntity;
    }

    @Override
    @Transactional
    /**
     * Função: Executa o fluxo padrão de remoção ou inativação conforme a regra do serviço.
     * Uso no sistema: preserva comportamento uniforme entre módulos.
     */
    public void delete(Long id) {
        validation.validateDelete(id);
        E entity = findByIdActive(id);
        beforeDelete(entity);
        entity.setAtivo(Boolean.FALSE);
        repository.save(entity);
        afterDelete(entity);
    }

    /**
     * Função: Executa validações ou ajustes necessários antes de inserir um registro.
     * Uso no sistema: permite especializar regras de cadastro sem duplicar o fluxo principal.
     */
    protected void beforeInsert(E entity) {
    }

    /**
     * Função: Executa ações complementares após inserir um registro.
     * Uso no sistema: permite criar vínculos ou atualizar dados derivados depois do cadastro.
     */
    protected void afterInsert(E savedEntity) {
    }

    /**
     * Função: Executa validações ou ajustes necessários antes de atualizar um registro.
     * Uso no sistema: impede alterações inválidas antes da gravação.
     */
    protected void beforeUpdate(E entity) {
    }

    /**
     * Função: Executa ações complementares após atualizar um registro.
     * Uso no sistema: permite sincronizar vínculos e dados dependentes.
     */
    protected void afterUpdate(E savedEntity) {
    }

    /**
     * Função: Executa validações antes de remover ou inativar um registro.
     * Uso no sistema: evita perda de rastreabilidade ou quebra de vínculos importantes.
     */
    protected void beforeDelete(E entity) {
    }

    /**
     * Função: Executa ações complementares após remover ou inativar um registro.
     * Uso no sistema: permite atualizar telas, vínculos ou auditorias depois da operação.
     */
    protected void afterDelete(E entity) {
    }
}
