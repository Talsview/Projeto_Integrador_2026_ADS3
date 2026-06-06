package br.com.avcar.oficina.business.peca.controller;

import br.com.avcar.oficina.business.peca.dto.FornecedorDTO;
import br.com.avcar.oficina.business.peca.service.FornecedorService;
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
 * Controller REST do módulo Fornecedor.
 */
@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FornecedorDTO>> cadastrar(@RequestBody FornecedorDTO dto) {
        FornecedorDTO saved = fornecedorService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Fornecedor cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FornecedorDTO>> atualizar(@PathVariable Long id, @RequestBody FornecedorDTO dto) {
        FornecedorDTO updated = fornecedorService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Fornecedor atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FornecedorDTO>> buscar(@PathVariable Long id) {
        FornecedorDTO fornecedor = fornecedorService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Fornecedor localizado com sucesso.", fornecedor));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FornecedorDTO>>> listar(Pageable pageable) {
        Page<FornecedorDTO> fornecedores = fornecedorService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Fornecedores localizados com sucesso.", PageResponse.from(fornecedores)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<FornecedorDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<FornecedorDTO> fornecedores = fornecedorService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de fornecedores executada com sucesso.", PageResponse.from(fornecedores)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        fornecedorService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Fornecedor inativado com sucesso.", null));
    }
}
