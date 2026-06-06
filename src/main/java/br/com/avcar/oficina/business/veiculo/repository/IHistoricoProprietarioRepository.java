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
    List<HistoricoProprietarioModel> findByVeiculoIdAndAtivoTrueOrderByDataInicioPosseDesc(Long veiculoId);

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa", "veiculo"})
    Optional<HistoricoProprietarioModel> findFirstByVeiculoIdAndAtivoTrueAndProprietarioAtualTrue(Long veiculoId);

    boolean existsByVeiculoIdAndAtivoTrueAndProprietarioAtualTrue(Long veiculoId);
}
