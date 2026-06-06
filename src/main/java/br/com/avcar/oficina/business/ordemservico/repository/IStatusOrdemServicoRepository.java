package br.com.avcar.oficina.business.ordemservico.repository;

import br.com.avcar.oficina.business.ordemservico.model.StatusOrdemServicoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IStatusOrdemServicoRepository extends IGenericRepository<StatusOrdemServicoModel> {

    Optional<StatusOrdemServicoModel> findByNomeStatusAndAtivoTrue(String nomeStatus);

    Optional<StatusOrdemServicoModel> findByOrdemFluxoAndAtivoTrue(Integer ordemFluxo);

    @Query("""
           SELECT s
             FROM StatusOrdemServicoModel s
            WHERE s.ativo = true
         ORDER BY s.ordemFluxo ASC
           """)
    Page<StatusOrdemServicoModel> findAllActiveOrderByFluxo(Pageable pageable);

    @Query("""
           SELECT s
             FROM StatusOrdemServicoModel s
            WHERE s.ativo = true
              AND (
                    LOWER(s.nomeStatus) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(s.descricao, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
         ORDER BY s.ordemFluxo ASC
           """)
    Page<StatusOrdemServicoModel> search(@Param("termo") String termo, Pageable pageable);
}
