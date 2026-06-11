package br.com.avcar.oficina.core.notification.repository;

import br.com.avcar.oficina.core.notification.model.NotificacaoAuditoriaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificacaoAuditoriaRepository extends IGenericRepository<NotificacaoAuditoriaModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Page<NotificacaoAuditoriaModel> findAllByAtivoTrueOrderByDataHoraAuditoriaDesc(Pageable pageable);

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<NotificacaoAuditoriaModel> findTop20ByReferenciaIgnoreCaseAndAtivoTrueOrderByDataHoraAuditoriaDesc(String referencia);
}
