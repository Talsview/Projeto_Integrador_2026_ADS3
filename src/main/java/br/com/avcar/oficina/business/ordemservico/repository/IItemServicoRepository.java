package br.com.avcar.oficina.business.ordemservico.repository;

import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemServicoRepository extends IGenericRepository<ItemServicoModel> {

    Page<ItemServicoModel> findAllByOrdemServicoIdAndAtivoTrue(Long idOrdemServico, Pageable pageable);

    List<ItemServicoModel> findByOrdemServicoIdAndAtivoTrue(Long idOrdemServico);

    boolean existsByOrdemServicoIdAndAtivoTrue(Long idOrdemServico);

    @Query("""
           SELECT i
             FROM ItemServicoModel i
             JOIN i.servico s
             JOIN i.colaborador c
             JOIN c.pessoa p
            WHERE i.ativo = true
              AND i.ordemServico.id = :idOrdemServico
              AND (
                    LOWER(s.nomeServico) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(i.descricaoExecucao, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    Page<ItemServicoModel> searchByOrdemServico(@Param("idOrdemServico") Long idOrdemServico,
                                                 @Param("termo") String termo,
                                                 Pageable pageable);
}
