package br.com.avcar.oficina.business.pagamento.repository;

import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.business.pagamento.model.PagamentoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPagamentoRepository extends IGenericRepository<PagamentoModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<PagamentoModel> findByOrdemServicoIdAndAtivoTrue(Long idOrdemServico, Pageable pageable);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<PagamentoModel> findByOrdemServicoIdAndAtivoTrue(Long idOrdemServico);

    @Query("""
           SELECT SUM(p.valorPago)
             FROM PagamentoModel p
            WHERE p.ativo = true
              AND p.ordemServico.id = :idOrdemServico
              AND p.statusPagamento = :statusPagamento
           """)
    BigDecimal somarValorPorStatus(@Param("idOrdemServico") Long idOrdemServico,
                                    @Param("statusPagamento") StatusPagamento statusPagamento);

    @Query("""
           SELECT p
             FROM PagamentoModel p
             JOIN p.ordemServico os
            WHERE p.ativo = true
              AND os.id = :idOrdemServico
              AND (
                    LOWER(os.numeroOs) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(COALESCE(p.observacao, '')) LIKE LOWER(CONCAT('%', :termo, '%'))
              )
           """)
    Page<PagamentoModel> searchByOrdemServico(@Param("idOrdemServico") Long idOrdemServico,
                                               @Param("termo") String termo,
                                               Pageable pageable);
}
