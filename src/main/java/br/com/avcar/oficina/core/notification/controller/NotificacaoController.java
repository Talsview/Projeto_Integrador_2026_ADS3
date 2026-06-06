package br.com.avcar.oficina.core.notification.controller;

import br.com.avcar.oficina.core.notification.dto.NotificacaoDTO;
import br.com.avcar.oficina.core.notification.dto.NotificacaoResultadoDTO;
import br.com.avcar.oficina.core.notification.service.NotificacaoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller auxiliar para demonstrar o funcionamento do Decorator em Swagger.
 */
@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @PostMapping("/simular")
    public ResponseEntity<ApiResponse<NotificacaoResultadoDTO>> simular(@RequestBody NotificacaoDTO dto) {
        NotificacaoResultadoDTO resultado = notificacaoService.notificar(dto);
        return ResponseEntity.ok(ApiResponse.success("Notificação processada com Decorator de auditoria.", resultado));
    }
}
