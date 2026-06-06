package br.com.avcar.oficina.business.pessoa.controller;

import br.com.avcar.oficina.business.pessoa.dto.FuncaoDTO;
import br.com.avcar.oficina.business.pessoa.service.FuncaoService;
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
 * Controller REST do módulo Função.
 * A View Angular deverá consumir estes endpoints para manter funções como registros.
 */
@RestController
@RequestMapping("/api/funcoes")
public class FuncaoController {

    private final FuncaoService funcaoService;

    public FuncaoController(FuncaoService funcaoService) {
        this.funcaoService = funcaoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FuncaoDTO>> cadastrar(@RequestBody FuncaoDTO dto) {
        FuncaoDTO saved = funcaoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Função cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncaoDTO>> atualizar(@PathVariable Long id, @RequestBody FuncaoDTO dto) {
        FuncaoDTO updated = funcaoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Função atualizada com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncaoDTO>> buscar(@PathVariable Long id) {
        FuncaoDTO funcao = funcaoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Função localizada com sucesso.", funcao));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FuncaoDTO>>> listar(Pageable pageable) {
        Page<FuncaoDTO> funcoes = funcaoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Funções localizadas com sucesso.", PageResponse.from(funcoes)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<FuncaoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<FuncaoDTO> funcoes = funcaoService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de funções executada com sucesso.", PageResponse.from(funcoes)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        funcaoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Função inativada com sucesso.", null));
    }
}
