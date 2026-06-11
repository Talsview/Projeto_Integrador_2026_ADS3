package br.com.avcar.oficina.business.veiculo.repository;

import br.com.avcar.oficina.business.veiculo.model.VeiculoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IVeiculoRepository extends IGenericRepository<VeiculoModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByPlacaAndAtivoTrue(String placa);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByPlacaAndIdNotAndAtivoTrue(String placa, Long id);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByChassiAndAtivoTrue(String chassi);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByChassiAndIdNotAndAtivoTrue(String chassi, Long id);

    @Override
    @EntityGraph(attributePaths = {"modelo", "modelo.marca"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<VeiculoModel> findByIdAndAtivoTrue(Long id);

    @Override
    @EntityGraph(attributePaths = {"modelo", "modelo.marca"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<VeiculoModel> findAllByAtivoTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"modelo", "modelo.marca"})
    @Query("""
           SELECT v
             FROM VeiculoModel v
             JOIN v.modelo mo
             JOIN mo.marca ma
            WHERE v.ativo = true
              AND mo.ativo = true
              AND ma.ativo = true
              AND (
                    LOWER(v.placa) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(v.chassi, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(mo.nomeModelo) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(ma.nomeMarca) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<VeiculoModel> search(@Param("termo") String termo, Pageable pageable);
}
