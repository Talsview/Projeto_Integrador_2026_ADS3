package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.ColaboradorModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IColaboradorRepository extends IGenericRepository<ColaboradorModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByPessoaIdAndAtivoTrue(Long pessoaId);

    @Override
    @EntityGraph(attributePaths = {"pessoa", "funcoes", "funcoes.funcao"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<ColaboradorModel> findByIdAndAtivoTrue(Long id);

    @Override
    @EntityGraph(attributePaths = {"pessoa", "funcoes", "funcoes.funcao"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<ColaboradorModel> findAllByAtivoTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"pessoa", "funcoes", "funcoes.funcao"})
    @Query("""
           SELECT c
             FROM ColaboradorModel c
             JOIN c.pessoa p
            WHERE c.ativo = true
              AND p.ativo = true
              AND (
                    LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(p.email, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(p.telefone, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<ColaboradorModel> searchByPessoa(@Param("termo") String termo, Pageable pageable);
}
