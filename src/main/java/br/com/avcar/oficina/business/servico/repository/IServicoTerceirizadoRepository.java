package br.com.avcar.oficina.business.servico.repository;

import br.com.avcar.oficina.business.servico.model.ServicoTerceirizadoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicoTerceirizadoRepository extends IGenericRepository<ServicoTerceirizadoModel> {
}
