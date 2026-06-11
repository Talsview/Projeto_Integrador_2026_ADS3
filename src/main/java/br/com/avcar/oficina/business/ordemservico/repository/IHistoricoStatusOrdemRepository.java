package br.com.avcar.oficina.business.ordemservico.repository;

import br.com.avcar.oficina.business.ordemservico.model.HistoricoStatusOrdemModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoricoStatusOrdemRepository extends IGenericRepository<HistoricoStatusOrdemModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<HistoricoStatusOrdemModel> findByOrdemServicoIdAndAtivoTrueOrderByDataStatusAsc(Long idOrdemServico);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<HistoricoStatusOrdemModel> findFirstByOrdemServicoIdAndAtivoTrueOrderByDataStatusDesc(Long idOrdemServico);

    @Query("""
           SELECT h
             FROM HistoricoStatusOrdemModel h
             JOIN h.statusOrdemServico s
            WHERE h.ativo = true
              AND h.ordemServico.id = :idOrdemServico
         ORDER BY s.ordemFluxo DESC, h.dataStatus DESC
           """)
    /**
     * Função: Declara uma operação de acesso ao banco que será implementada automaticamente pelo
     * Spring Data JPA.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<HistoricoStatusOrdemModel> findHistoricoFluxoDesc(@Param("idOrdemServico") Long idOrdemServico);
}
