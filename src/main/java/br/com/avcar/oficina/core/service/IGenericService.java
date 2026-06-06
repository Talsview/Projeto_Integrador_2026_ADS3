package br.com.avcar.oficina.core.service;

import br.com.avcar.oficina.core.model.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGenericService<E extends BaseModel> {

    E findByIdActive(Long id);

    Page<E> findAllActive(Pageable pageable);

    E insert(E entity);

    E update(E entity);

    void delete(Long id);
}
