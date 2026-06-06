package br.com.avcar.oficina.business.veiculo.repository;

import br.com.avcar.oficina.business.veiculo.model.ModeloModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IModeloRepository extends IGenericRepository<ModeloModel> {

    @Override
    @EntityGraph(attributePaths = "marca")
    Optional<ModeloModel> findByIdAndAtivoTrue(Long id);

    @Override
    @EntityGraph(attributePaths = "marca")
    Page<ModeloModel> findAllByAtivoTrue(Pageable pageable);

    @EntityGraph(attributePaths = "marca")
    Page<ModeloModel> findByMarcaIdAndAtivoTrue(Long marcaId, Pageable pageable);

    @EntityGraph(attributePaths = "marca")
    @Query("""
           SELECT m
             FROM ModeloModel m
             JOIN m.marca ma
            WHERE m.ativo = true
              AND ma.ativo = true
              AND (
                    LOWER(m.nomeModelo) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(ma.nomeMarca) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    Page<ModeloModel> search(@Param("termo") String termo, Pageable pageable);

    @Query("""
           SELECT COUNT(m) > 0
             FROM ModeloModel m
            WHERE m.ativo = true
              AND m.marca.id = :marcaId
              AND LOWER(m.nomeModelo) = LOWER(:nomeModelo)
           """)
    boolean existsActiveByMarcaAndNome(@Param("marcaId") Long marcaId, @Param("nomeModelo") String nomeModelo);

    @Query("""
           SELECT COUNT(m) > 0
             FROM ModeloModel m
            WHERE m.ativo = true
              AND m.id <> :id
              AND m.marca.id = :marcaId
              AND LOWER(m.nomeModelo) = LOWER(:nomeModelo)
           """)
    boolean existsActiveByMarcaAndNomeAndIdNot(@Param("marcaId") Long marcaId,
                                               @Param("nomeModelo") String nomeModelo,
                                               @Param("id") Long id);
}
