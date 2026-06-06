package br.com.avcar.oficina.business.veiculo.controller;

import br.com.avcar.oficina.business.veiculo.dto.ModeloDTO;
import br.com.avcar.oficina.business.veiculo.service.ModeloService;
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
 * Controller REST do módulo Modelo.
 */
@RestController
@RequestMapping("/api/modelos")
public class ModeloController {

    private final ModeloService modeloService;

    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ModeloDTO>> cadastrar(@RequestBody ModeloDTO dto) {
        ModeloDTO saved = modeloService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Modelo cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModeloDTO>> atualizar(@PathVariable Long id, @RequestBody ModeloDTO dto) {
        ModeloDTO updated = modeloService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Modelo atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ModeloDTO>> buscar(@PathVariable Long id) {
        ModeloDTO modelo = modeloService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Modelo localizado com sucesso.", modelo));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ModeloDTO>>> listar(Pageable pageable) {
        Page<ModeloDTO> modelos = modeloService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Modelos localizados com sucesso.", PageResponse.from(modelos)));
    }

    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<ApiResponse<PageResponse<ModeloDTO>>> listarPorMarca(@PathVariable Long marcaId, Pageable pageable) {
        Page<ModeloDTO> modelos = modeloService.listarPorMarca(marcaId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Modelos da marca localizados com sucesso.", PageResponse.from(modelos)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<ModeloDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<ModeloDTO> modelos = modeloService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de modelos executada com sucesso.", PageResponse.from(modelos)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        modeloService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Modelo inativado com sucesso.", null));
    }
}
