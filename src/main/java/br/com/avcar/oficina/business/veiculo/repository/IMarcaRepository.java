package br.com.avcar.oficina.business.veiculo.repository;

import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IMarcaRepository extends IGenericRepository<MarcaModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByNomeMarcaIgnoreCaseAndAtivoTrue(String nomeMarca);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByNomeMarcaIgnoreCaseAndIdNotAndAtivoTrue(String nomeMarca, Long id);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<MarcaModel> findByNomeMarcaContainingIgnoreCaseAndAtivoTrue(String termo, Pageable pageable);
}
