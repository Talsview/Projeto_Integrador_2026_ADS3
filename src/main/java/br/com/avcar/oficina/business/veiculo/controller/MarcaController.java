package br.com.avcar.oficina.business.veiculo.controller;

import br.com.avcar.oficina.business.veiculo.dto.MarcaDTO;
import br.com.avcar.oficina.business.veiculo.service.MarcaService;
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
 * Controller REST do módulo Marca.
 */
@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MarcaDTO>> cadastrar(@RequestBody MarcaDTO dto) {
        MarcaDTO saved = marcaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Marca cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MarcaDTO>> atualizar(@PathVariable Long id, @RequestBody MarcaDTO dto) {
        MarcaDTO updated = marcaService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Marca atualizada com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MarcaDTO>> buscar(@PathVariable Long id) {
        MarcaDTO marca = marcaService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Marca localizada com sucesso.", marca));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MarcaDTO>>> listar(Pageable pageable) {
        Page<MarcaDTO> marcas = marcaService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Marcas localizadas com sucesso.", PageResponse.from(marcas)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<MarcaDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<MarcaDTO> marcas = marcaService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de marcas executada com sucesso.", PageResponse.from(marcas)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        marcaService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Marca inativada com sucesso.", null));
    }
}
