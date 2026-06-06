package br.com.avcar.oficina.business.peca.controller;

import br.com.avcar.oficina.business.peca.dto.PecaDTO;
import br.com.avcar.oficina.business.peca.service.PecaService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST do módulo Peça.
 */
@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaService pecaService;

    public PecaController(PecaService pecaService) {
        this.pecaService = pecaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PecaDTO>> cadastrar(@RequestBody PecaDTO dto) {
        PecaDTO saved = pecaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Peça cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PecaDTO>> atualizar(@PathVariable Long id, @RequestBody PecaDTO dto) {
        PecaDTO updated = pecaService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Peça atualizada com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PecaDTO>> buscar(@PathVariable Long id) {
        PecaDTO peca = pecaService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Peça localizada com sucesso.", peca));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PecaDTO>>> listar(Pageable pageable) {
        Page<PecaDTO> pecas = pecaService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Peças localizadas com sucesso.", PageResponse.from(pecas)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<PecaDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<PecaDTO> pecas = pecaService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de peças executada com sucesso.", PageResponse.from(pecas)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        pecaService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Peça inativada com sucesso.", null));
    }
}
