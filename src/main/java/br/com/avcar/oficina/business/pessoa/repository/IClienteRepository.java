package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IClienteRepository extends IGenericRepository<ClienteModel> {

    @Override
    @EntityGraph(attributePaths = "pessoa")
    Optional<ClienteModel> findByIdAndAtivoTrue(Long id);

    @Override
    @EntityGraph(attributePaths = "pessoa")
    Page<ClienteModel> findAllByAtivoTrue(Pageable pageable);

    @EntityGraph(attributePaths = "pessoa")
    @Query("""
           SELECT c
             FROM ClienteModel c
             JOIN c.pessoa p
            WHERE c.ativo = true
              AND p.ativo = true
              AND (
                    LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(p.email, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(p.telefone, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    Page<ClienteModel> searchByPessoa(@Param("termo") String termo, Pageable pageable);
}
