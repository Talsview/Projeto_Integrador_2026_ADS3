package br.com.avcar.oficina.business.garantia.repository;

import br.com.avcar.oficina.business.garantia.model.GarantiaServicoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IGarantiaServicoRepository extends IGenericRepository<GarantiaServicoModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<GarantiaServicoModel> findByItemServicoIdAndAtivoTrue(Long idItemServico);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByItemServicoIdAndAtivoTrue(Long idItemServico);

    @Query("""
           SELECT g
             FROM GarantiaServicoModel g
             JOIN g.itemServico isv
            WHERE g.ativo = true
              AND isv.ativo = true
              AND isv.ordemServico.id = :idOrdemServico
           """)
    /**
     * Função: Declara uma consulta derivada pelo Spring Data JPA com base nos campos informados no
     * nome do método.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<GarantiaServicoModel> findByOrdemServico(@Param("idOrdemServico") Long idOrdemServico);

    @Query("""
           SELECT g
             FROM GarantiaServicoModel g
             JOIN g.itemServico isv
            WHERE g.ativo = true
              AND isv.ativo = true
              AND isv.ordemServico.id = :idOrdemServico
           """)
    /**
     * Função: Declara uma consulta derivada pelo Spring Data JPA com base nos campos informados no
     * nome do método.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<GarantiaServicoModel> findByOrdemServico(@Param("idOrdemServico") Long idOrdemServico, Pageable pageable);
}
