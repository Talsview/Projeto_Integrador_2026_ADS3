package br.com.avcar.oficina.core.notification.controller;

import br.com.avcar.oficina.core.notification.dto.NotificacaoAuditoriaDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoResultadoDTO;
import br.com.avcar.oficina.core.notification.service.NotificacaoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller auxiliar para demonstrar e consultar o Decorator de auditoria.
 */
@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @PostMapping("/simular")
    /**
     * Função: Atende a rota HTTP responsável por simular e repassa a regra ao serviço correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<NotificacaoResultadoDTO>> simular(@RequestBody NotificacaoDTO dto) {
        NotificacaoResultadoDTO resultado = notificacaoService.notificar(dto);
        return ResponseEntity.ok(ApiResponse.success("Notificação processada e auditada com Decorator persistente.", resultado));
    }

    @GetMapping("/auditoria")
    /**
     * Função: Recebe filtros de consulta de notificacao, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Page<NotificacaoAuditoriaDTO>>> listarAuditorias(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Auditorias de notificação registradas pelo Decorator.", notificacaoService.listarAuditorias(pageable)));
    }

    @GetMapping("/auditoria/referencia")
    /**
     * Função: Recebe filtros de consulta de notificacao, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<List<NotificacaoAuditoriaDTO>>> listarPorReferencia(@RequestParam String referencia) {
        return ResponseEntity.ok(ApiResponse.success("Auditorias localizadas pela referência informada.", notificacaoService.listarAuditoriasPorReferencia(referencia)));
    }
}
