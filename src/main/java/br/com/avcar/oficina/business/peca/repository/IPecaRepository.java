package br.com.avcar.oficina.business.peca.repository;

import br.com.avcar.oficina.business.peca.model.PecaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPecaRepository extends IGenericRepository<PecaModel> {

    @Query("""
           SELECT p
             FROM PecaModel p
             LEFT JOIN p.fornecedorPadrao fp
            WHERE p.ativo = true
              AND (
                    LOWER(p.nomePeca) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(p.codigoNacional, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(p.marcaPeca, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(p.modeloAplicavel, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(fp.nomeFornecedor, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    Page<PecaModel> search(@Param("termo") String termo, Pageable pageable);

    @Query("""
           SELECT COUNT(p) > 0
             FROM PecaModel p
            WHERE p.ativo = true
              AND p.codigoNacional IS NOT NULL
              AND p.codigoNacional = :codigoNacional
           """)
    boolean existsActiveByCodigoNacional(@Param("codigoNacional") String codigoNacional);

    @Query("""
           SELECT COUNT(p) > 0
             FROM PecaModel p
            WHERE p.ativo = true
              AND p.id <> :id
              AND p.codigoNacional IS NOT NULL
              AND p.codigoNacional = :codigoNacional
           """)
    boolean existsActiveByCodigoNacionalAndIdNot(@Param("codigoNacional") String codigoNacional, @Param("id") Long id);
}
