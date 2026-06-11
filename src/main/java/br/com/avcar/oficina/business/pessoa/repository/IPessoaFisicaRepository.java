package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.PessoaFisicaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface IPessoaFisicaRepository extends IGenericRepository<PessoaFisicaModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByCpfAndAtivoTrue(String cpf);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByCpfAndIdNotAndAtivoTrue(String cpf, Long id);

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<PessoaFisicaModel> findByCpfAndAtivoTrue(String cpf);

    @Override
    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<PessoaFisicaModel> findByIdAndAtivoTrue(Long id);
}
