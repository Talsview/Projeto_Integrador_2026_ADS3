package br.com.avcar.oficina.business.servico.repository;

import br.com.avcar.oficina.business.servico.model.ServicoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicoRepository extends IGenericRepository<ServicoModel> {

    @Query("""
           SELECT s
             FROM ServicoModel s
            WHERE s.ativo = true
              AND (
                    LOWER(s.nomeServico) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(s.descricao, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    Page<ServicoModel> search(@Param("termo") String termo, Pageable pageable);

    @Query("""
           SELECT s
             FROM ServicoModel s
            WHERE s.ativo = true
              AND EXISTS (
                    SELECT 1
                      FROM ServicoInternoModel si
                     WHERE si.servico = s
                       AND si.ativo = true
              )
           """)
    Page<ServicoModel> findServicosInternosAtivos(Pageable pageable);

    @Query("""
           SELECT s
             FROM ServicoModel s
            WHERE s.ativo = true
              AND EXISTS (
                    SELECT 1
                      FROM ServicoTerceirizadoModel st
                     WHERE st.servico = s
                       AND st.ativo = true
              )
           """)
    Page<ServicoModel> findServicosTerceirizadosAtivos(Pageable pageable);

    @Query("""
           SELECT COUNT(s) > 0
             FROM ServicoModel s
            WHERE s.ativo = true
              AND LOWER(s.nomeServico) = LOWER(:nomeServico)
           """)
    boolean existsActiveByNome(@Param("nomeServico") String nomeServico);

    @Query("""
           SELECT COUNT(s) > 0
             FROM ServicoModel s
            WHERE s.ativo = true
              AND s.id <> :id
              AND LOWER(s.nomeServico) = LOWER(:nomeServico)
           """)
    boolean existsActiveByNomeAndIdNot(@Param("nomeServico") String nomeServico, @Param("id") Long id);
}
