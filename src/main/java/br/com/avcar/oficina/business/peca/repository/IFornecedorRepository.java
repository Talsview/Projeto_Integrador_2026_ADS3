package br.com.avcar.oficina.business.peca.repository;

import br.com.avcar.oficina.business.peca.model.FornecedorModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IFornecedorRepository extends IGenericRepository<FornecedorModel> {

    @Query("""
           SELECT f
             FROM FornecedorModel f
            WHERE f.ativo = true
              AND (
                    LOWER(f.nomeFornecedor) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(f.cnpj, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(f.telefone, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(f.email, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<FornecedorModel> search(@Param("termo") String termo, Pageable pageable);

    @Query("""
           SELECT COUNT(f) > 0
             FROM FornecedorModel f
            WHERE f.ativo = true
              AND f.cnpj IS NOT NULL
              AND f.cnpj = :cnpj
           """)
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsActiveByCnpj(@Param("cnpj") String cnpj);

    @Query("""
           SELECT COUNT(f) > 0
             FROM FornecedorModel f
            WHERE f.ativo = true
              AND f.id <> :id
              AND f.cnpj IS NOT NULL
              AND f.cnpj = :cnpj
           """)
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsActiveByCnpjAndIdNot(@Param("cnpj") String cnpj, @Param("id") Long id);
}
