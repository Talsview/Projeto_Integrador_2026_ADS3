package br.com.avcar.oficina.business.veiculo.repository;

import br.com.avcar.oficina.business.veiculo.model.HistoricoProprietarioModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoricoProprietarioRepository extends IGenericRepository<HistoricoProprietarioModel> {

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa", "veiculo"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<HistoricoProprietarioModel> findByVeiculoIdAndAtivoTrueOrderByDataInicioPosseDesc(Long veiculoId);

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa", "veiculo"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<HistoricoProprietarioModel> findFirstByVeiculoIdAndAtivoTrueAndProprietarioAtualTrue(Long veiculoId);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    boolean existsByVeiculoIdAndAtivoTrueAndProprietarioAtualTrue(Long veiculoId);
}
