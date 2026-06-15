package br.com.avcar.oficina.business.veiculo.repository;

import br.com.avcar.oficina.business.veiculo.model.HistoricoProprietarioModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoricoProprietarioRepository extends IGenericRepository<HistoricoProprietarioModel> {

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa", "veiculo", "veiculo.modelo", "veiculo.modelo.marca"})
    @Query("select h from HistoricoProprietarioModel h "
            + "where h.ativo = true "
            + "order by h.cliente.pessoa.nome asc, h.veiculo.placa asc, h.dataInicioPosse desc")
    /**
     * Função: retorna todos os registros ativos do histórico de proprietários, incluindo
     * proprietários atuais e anteriores.
     * Uso no sistema: alimenta a tela de Gestão sem duplicar clientes e sem apagar a
     * visualização de clientes que já não são proprietários atuais de um veículo.
     */
    List<HistoricoProprietarioModel> findAllAtivosComClienteEVeiculoOrdenados();

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa", "veiculo", "veiculo.modelo", "veiculo.modelo.marca"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<HistoricoProprietarioModel> findByVeiculoIdAndAtivoTrueOrderByDataInicioPosseDesc(Long veiculoId);

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa", "veiculo", "veiculo.modelo", "veiculo.modelo.marca"})
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
