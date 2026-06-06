package br.com.avcar.oficina.business.ordemservico.repository;

import br.com.avcar.oficina.business.ordemservico.model.ExecucaoServicoTerceirizadoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IExecucaoServicoTerceirizadoRepository extends IGenericRepository<ExecucaoServicoTerceirizadoModel> {

    Optional<ExecucaoServicoTerceirizadoModel> findByItemServicoIdAndAtivoTrue(Long idItemServico);
}
