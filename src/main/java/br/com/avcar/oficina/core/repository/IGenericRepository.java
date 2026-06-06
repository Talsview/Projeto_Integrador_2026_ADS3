package br.com.avcar.oficina.core.repository;

import br.com.avcar.oficina.core.model.BaseModel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repository genérico baseado em Spring Data JPA.
 *
 * O banco físico deve ser criado por script SQL e o JPA deve validar o schema,
 * evitando divergência entre implementação e modelo físico.
 */
@NoRepositoryBean
public interface IGenericRepository<E extends BaseModel> extends JpaRepository<E, Long> {

    Optional<E> findByIdAndAtivoTrue(Long id);

    Page<E> findAllByAtivoTrue(Pageable pageable);
}
