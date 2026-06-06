package br.com.avcar.oficina.business.pagamento.controller;

import br.com.avcar.oficina.business.pagamento.dto.PagamentoDTO;
import br.com.avcar.oficina.business.pagamento.dto.ResumoPagamentoOrdemServicoDTO;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.business.pagamento.service.PagamentoService;
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
 * Controller REST responsável pelos pagamentos vinculados às Ordens de Serviço.
 */
@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PagamentoDTO>> cadastrar(@RequestBody PagamentoDTO dto) {
        PagamentoDTO saved = pagamentoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Pagamento cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PagamentoDTO>> atualizar(@PathVariable Long id,
                                                               @RequestBody PagamentoDTO dto) {
        PagamentoDTO updated = pagamentoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Pagamento atualizado com sucesso.", updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PagamentoDTO>> alterarStatus(@PathVariable Long id,
                                                                   @RequestParam StatusPagamento statusPagamento) {
        PagamentoDTO updated = pagamentoService.alterarStatus(id, statusPagamento);
        return ResponseEntity.ok(ApiResponse.success("Status do pagamento alterado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PagamentoDTO>> buscar(@PathVariable Long id) {
        PagamentoDTO pagamento = pagamentoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Pagamento localizado com sucesso.", pagamento));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PagamentoDTO>>> listar(Pageable pageable) {
        Page<PagamentoDTO> pagamentos = pagamentoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Pagamentos localizados com sucesso.", PageResponse.from(pagamentos)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}")
    public ResponseEntity<ApiResponse<PageResponse<PagamentoDTO>>> listarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                         Pageable pageable) {
        Page<PagamentoDTO> pagamentos = pagamentoService.listarPorOrdemServico(idOrdemServico, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pagamentos da Ordem de Serviço localizados com sucesso.", PageResponse.from(pagamentos)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<PagamentoDTO>>> pesquisarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                            @RequestParam String termo,
                                                                                            Pageable pageable) {
        Page<PagamentoDTO> pagamentos = pagamentoService.pesquisarPorOrdemServico(idOrdemServico, termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de pagamentos executada com sucesso.", PageResponse.from(pagamentos)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}/resumo")
    public ResponseEntity<ApiResponse<ResumoPagamentoOrdemServicoDTO>> resumirPorOrdemServico(@PathVariable Long idOrdemServico) {
        ResumoPagamentoOrdemServicoDTO resumo = pagamentoService.resumirPorOrdemServico(idOrdemServico);
        return ResponseEntity.ok(ApiResponse.success("Resumo financeiro da Ordem de Serviço localizado com sucesso.", resumo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        pagamentoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Pagamento inativado com sucesso.", null));
    }
}
