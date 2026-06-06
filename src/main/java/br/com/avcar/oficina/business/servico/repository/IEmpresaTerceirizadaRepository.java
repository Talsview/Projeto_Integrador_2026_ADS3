package br.com.avcar.oficina.business.servico.repository;

import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmpresaTerceirizadaRepository extends IGenericRepository<EmpresaTerceirizadaModel> {

    @Query("""
           SELECT e
             FROM EmpresaTerceirizadaModel e
            WHERE e.ativo = true
              AND (
                    LOWER(e.nomeEmpresa) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(e.cnpj, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(e.telefone, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(e.email, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    Page<EmpresaTerceirizadaModel> search(@Param("termo") String termo, Pageable pageable);

    @Query("""
           SELECT COUNT(e) > 0
             FROM EmpresaTerceirizadaModel e
            WHERE e.ativo = true
              AND e.cnpj IS NOT NULL
              AND e.cnpj = :cnpj
           """)
    boolean existsActiveByCnpj(@Param("cnpj") String cnpj);

    @Query("""
           SELECT COUNT(e) > 0
             FROM EmpresaTerceirizadaModel e
            WHERE e.ativo = true
              AND e.id <> :id
              AND e.cnpj IS NOT NULL
              AND e.cnpj = :cnpj
           """)
    boolean existsActiveByCnpjAndIdNot(@Param("cnpj") String cnpj, @Param("id") Long id);
}
