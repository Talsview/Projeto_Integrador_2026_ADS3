package br.com.avcar.oficina.business.ordemservico.repository;

import br.com.avcar.oficina.business.ordemservico.model.HistoricoStatusOrdemModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoricoStatusOrdemRepository extends IGenericRepository<HistoricoStatusOrdemModel> {

    List<HistoricoStatusOrdemModel> findByOrdemServicoIdAndAtivoTrueOrderByDataStatusAsc(Long idOrdemServico);

    Optional<HistoricoStatusOrdemModel> findFirstByOrdemServicoIdAndAtivoTrueOrderByDataStatusDesc(Long idOrdemServico);

    @Query("""
           SELECT h
             FROM HistoricoStatusOrdemModel h
             JOIN h.statusOrdemServico s
            WHERE h.ativo = true
              AND h.ordemServico.id = :idOrdemServico
         ORDER BY s.ordemFluxo DESC, h.dataStatus DESC
           """)
    List<HistoricoStatusOrdemModel> findHistoricoFluxoDesc(@Param("idOrdemServico") Long idOrdemServico);
}
