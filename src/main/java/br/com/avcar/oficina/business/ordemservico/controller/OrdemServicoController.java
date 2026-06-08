package br.com.avcar.oficina.business.ordemservico.controller;

import br.com.avcar.oficina.business.ordemservico.dto.AlterarStatusOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.business.ordemservico.service.OrdemServicoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST do módulo Ordem de Serviço.
 */
@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(OrdemServicoService ordemServicoService) {
        this.ordemServicoService = ordemServicoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> cadastrar(@RequestBody OrdemServicoDTO dto) {
        OrdemServicoDTO saved = ordemServicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Ordem de Serviço cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> atualizar(@PathVariable Long id,
                                                                  @RequestBody OrdemServicoDTO dto) {
        OrdemServicoDTO updated = ordemServicoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Ordem de Serviço atualizada com sucesso.", updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> alterarStatus(@PathVariable Long id,
                                                                      @RequestBody AlterarStatusOrdemServicoDTO dto) {
        OrdemServicoDTO updated = ordemServicoService.alterarStatusManual(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Status da Ordem de Serviço alterado com sucesso.", updated));
    }

    @PatchMapping("/{id}/enviar-para-pagamento")
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> enviarOrcamentoParaPagamento(@PathVariable Long id) {
        OrdemServicoDTO updated = ordemServicoService.enviarOrcamentoParaPagamento(id);
        return ResponseEntity.ok(ApiResponse.success("Orçamento enviado para pagamento com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> buscar(@PathVariable Long id) {
        OrdemServicoDTO ordemServico = ordemServicoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Ordem de Serviço localizada com sucesso.", ordemServico));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrdemServicoResumoDTO>>> listar(Pageable pageable) {
        Page<OrdemServicoResumoDTO> ordens = ordemServicoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Ordens de Serviço localizadas com sucesso.", PageResponse.from(ordens)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<OrdemServicoResumoDTO>>> pesquisar(@RequestParam String termo,
                                                                                       Pageable pageable) {
        Page<OrdemServicoResumoDTO> ordens = ordemServicoService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de Ordens de Serviço executada com sucesso.", PageResponse.from(ordens)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        ordemServicoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Ordem de Serviço inativada com sucesso.", null));
    }
}
