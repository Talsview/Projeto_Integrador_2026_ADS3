package br.com.avcar.oficina.business.ordemservico.controller;

import br.com.avcar.oficina.business.ordemservico.dto.StatusOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.service.StatusOrdemServicoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para consulta dos status oficiais da OS.
 */
@RestController
@RequestMapping("/api/status-ordem-servico")
public class StatusOrdemServicoController {

    private final StatusOrdemServicoService statusService;

    public StatusOrdemServicoController(StatusOrdemServicoService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StatusOrdemServicoDTO>> buscar(@PathVariable Long id) {
        StatusOrdemServicoDTO status = statusService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Status de Ordem de Serviço localizado com sucesso.", status));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StatusOrdemServicoDTO>>> listar(Pageable pageable) {
        Page<StatusOrdemServicoDTO> status = statusService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Status de Ordem de Serviço localizados com sucesso.", PageResponse.from(status)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<StatusOrdemServicoDTO>>> pesquisar(@RequestParam String termo,
                                                                                       Pageable pageable) {
        Page<StatusOrdemServicoDTO> status = statusService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de status executada com sucesso.", PageResponse.from(status)));
    }
}
