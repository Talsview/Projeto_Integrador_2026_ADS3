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

    protected GenericService(IGenericRepository<E> repository, IGenericValidation<E> validation) {
        this.repository = repository;
        this.validation = validation;
    }

    @Override
    @Transactional(readOnly = true)
    public E findByIdActive(Long id) {
        if (id == null) {
            throw new BusinessException("O ID é obrigatório para buscar o registro.");
        }

        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Registro não encontrado ou inativo."));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<E> findAllActive(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable);
    }

    @Override
    @Transactional
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
    public void delete(Long id) {
        validation.validateDelete(id);
        E entity = findByIdActive(id);
        beforeDelete(entity);
        entity.setAtivo(Boolean.FALSE);
        repository.save(entity);
        afterDelete(entity);
    }

    protected void beforeInsert(E entity) {
    }

    protected void afterInsert(E savedEntity) {
    }

    protected void beforeUpdate(E entity) {
    }

    protected void afterUpdate(E savedEntity) {
    }

    protected void beforeDelete(E entity) {
    }

    protected void afterDelete(E entity) {
    }
}
