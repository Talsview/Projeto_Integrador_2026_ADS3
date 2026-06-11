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
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
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
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
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
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<ServicoModel> findServicosTerceirizadosAtivos(Pageable pageable);

    @Query("""
           SELECT COUNT(s) > 0
             FROM ServicoModel s
            WHERE s.ativo = true
              AND LOWER(s.nomeServico) = LOWER(:nomeServico)
           """)
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsActiveByNome(@Param("nomeServico") String nomeServico);

    @Query("""
           SELECT COUNT(s) > 0
             FROM ServicoModel s
            WHERE s.ativo = true
              AND s.id <> :id
              AND LOWER(s.nomeServico) = LOWER(:nomeServico)
           """)
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsActiveByNomeAndIdNot(@Param("nomeServico") String nomeServico, @Param("id") Long id);
}
