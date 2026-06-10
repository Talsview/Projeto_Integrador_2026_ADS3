package br.com.avcar.oficina.core.notification.repository;

import br.com.avcar.oficina.core.notification.model.NotificacaoAuditoriaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificacaoAuditoriaRepository extends IGenericRepository<NotificacaoAuditoriaModel> {

    Page<NotificacaoAuditoriaModel> findAllByAtivoTrueOrderByDataHoraAuditoriaDesc(Pageable pageable);

    List<NotificacaoAuditoriaModel> findTop20ByReferenciaIgnoreCaseAndAtivoTrueOrderByDataHoraAuditoriaDesc(String referencia);
}
