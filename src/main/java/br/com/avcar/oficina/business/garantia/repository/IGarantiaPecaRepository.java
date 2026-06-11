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

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<GarantiaPecaModel> findByItemPecaIdAndAtivoTrue(Long idItemPeca);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByItemPecaIdAndAtivoTrue(Long idItemPeca);

    @Query("""
           SELECT g
             FROM GarantiaPecaModel g
             JOIN g.itemPeca ip
            WHERE g.ativo = true
              AND ip.ativo = true
              AND ip.idOrdemServico = :idOrdemServico
           """)
    /**
     * Função: Declara uma consulta derivada pelo Spring Data JPA com base nos campos informados no
     * nome do método.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<GarantiaPecaModel> findByOrdemServico(@Param("idOrdemServico") Long idOrdemServico);

    @Query("""
           SELECT g
             FROM GarantiaPecaModel g
             JOIN g.itemPeca ip
            WHERE g.ativo = true
              AND ip.ativo = true
              AND ip.idOrdemServico = :idOrdemServico
           """)
    /**
     * Função: Declara uma consulta derivada pelo Spring Data JPA com base nos campos informados no
     * nome do método.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<GarantiaPecaModel> findByOrdemServico(@Param("idOrdemServico") Long idOrdemServico, Pageable pageable);
}
