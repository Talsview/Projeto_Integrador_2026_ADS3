package br.com.avcar.oficina.business.ordemservico.repository;

import br.com.avcar.oficina.business.ordemservico.model.ExecucaoServicoTerceirizadoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IExecucaoServicoTerceirizadoRepository extends IGenericRepository<ExecucaoServicoTerceirizadoModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<ExecucaoServicoTerceirizadoModel> findByItemServicoIdAndAtivoTrue(Long idItemServico);
}
