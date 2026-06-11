package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.PessoaJuridicaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface IPessoaJuridicaRepository extends IGenericRepository<PessoaJuridicaModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByCnpjAndAtivoTrue(String cnpj);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByCnpjAndIdNotAndAtivoTrue(String cnpj, Long id);

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<PessoaJuridicaModel> findByCnpjAndAtivoTrue(String cnpj);

    @Override
    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<PessoaJuridicaModel> findByIdAndAtivoTrue(Long id);
}
