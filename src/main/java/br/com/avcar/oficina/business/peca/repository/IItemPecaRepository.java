package br.com.avcar.oficina.business.peca.repository;

import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemPecaRepository extends IGenericRepository<ItemPecaModel> {

    Page<ItemPecaModel> findAllByIdOrdemServicoAndAtivoTrue(Long idOrdemServico, Pageable pageable);

    List<ItemPecaModel> findByIdOrdemServicoAndAtivoTrue(Long idOrdemServico);

    @Query("""
           SELECT i
             FROM ItemPecaModel i
             JOIN i.peca p
             JOIN i.fornecedor f
            WHERE i.ativo = true
              AND i.idOrdemServico = :idOrdemServico
              AND (
                    LOWER(p.nomePeca) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(p.codigoNacional, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(f.nomeFornecedor) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(i.observacao, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    Page<ItemPecaModel> searchByOrdemServico(@Param("idOrdemServico") Long idOrdemServico,
                                              @Param("termo") String termo,
                                              Pageable pageable);
}
