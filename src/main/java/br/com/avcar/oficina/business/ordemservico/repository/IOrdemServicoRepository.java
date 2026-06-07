package br.com.avcar.oficina.business.ordemservico.repository;

import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrdemServicoRepository extends IGenericRepository<OrdemServicoModel> {

    Optional<OrdemServicoModel> findByNumeroOsAndAtivoTrue(String numeroOs);

    boolean existsByNumeroOsIgnoreCase(String numeroOs);

    @Query("""
           SELECT COUNT(os) > 0
             FROM OrdemServicoModel os
            WHERE os.ativo = true
              AND LOWER(os.numeroOs) = LOWER(:numeroOs)
           """)
    boolean existsActiveByNumeroOs(@Param("numeroOs") String numeroOs);

    @Query("""
           SELECT COUNT(os) > 0
             FROM OrdemServicoModel os
            WHERE os.ativo = true
              AND os.id <> :id
              AND LOWER(os.numeroOs) = LOWER(:numeroOs)
           """)
    boolean existsActiveByNumeroOsAndIdNot(@Param("numeroOs") String numeroOs, @Param("id") Long id);


    @Query(value = """
           SELECT COALESCE(MAX(CAST(numero_os AS BIGINT)), 0)
             FROM ordem_servico
            WHERE numero_os ~ '^[0-9]+$'
           """, nativeQuery = true)
    Long buscarMaiorNumeroOsNumerico();

    @Query("""
           SELECT os
             FROM OrdemServicoModel os
             JOIN os.cliente c
             JOIN c.pessoa p
             JOIN os.veiculo v
             JOIN v.modelo m
             JOIN m.marca ma
            WHERE os.ativo = true
              AND (
                    LOWER(os.numeroOs) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(v.placa) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(m.nomeModelo) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(ma.nomeMarca) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(os.observacao, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    Page<OrdemServicoModel> search(@Param("termo") String termo, Pageable pageable);
}
