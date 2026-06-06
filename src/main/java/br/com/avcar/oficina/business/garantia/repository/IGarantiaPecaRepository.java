package br.com.avcar.oficina.business.garantia.repository;

import br.com.avcar.oficina.business.garantia.model.GarantiaPecaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IGarantiaPecaRepository extends IGenericRepository<GarantiaPecaModel> {

    Optional<GarantiaPecaModel> findByItemPecaIdAndAtivoTrue(Long idItemPeca);

    boolean existsByItemPecaIdAndAtivoTrue(Long idItemPeca);

    @Query("""
           SELECT g
             FROM GarantiaPecaModel g
             JOIN g.itemPeca ip
            WHERE g.ativo = true
              AND ip.ativo = true
              AND ip.idOrdemServico = :idOrdemServico
           """)
    List<GarantiaPecaModel> findByOrdemServico(@Param("idOrdemServico") Long idOrdemServico);

    @Query("""
           SELECT g
             FROM GarantiaPecaModel g
             JOIN g.itemPeca ip
            WHERE g.ativo = true
              AND ip.ativo = true
              AND ip.idOrdemServico = :idOrdemServico
           """)
    Page<GarantiaPecaModel> findByOrdemServico(@Param("idOrdemServico") Long idOrdemServico, Pageable pageable);
}
