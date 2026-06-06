package br.com.avcar.oficina.business.veiculo.controller;

import br.com.avcar.oficina.business.veiculo.dto.TransferenciaProprietarioDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoResumoDTO;
import br.com.avcar.oficina.business.veiculo.service.VeiculoService;
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
 * Controller REST do módulo Veículo.
 *
 * Regra acadêmica: a relação Cliente-Veículo é controlada por HistoricoProprietario,
 * preservando o histórico de donos ao longo do tempo.
 */
@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VeiculoDTO>> cadastrar(@RequestBody VeiculoDTO dto) {
        VeiculoDTO saved = veiculoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Veículo cadastrado com histórico inicial de proprietário.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VeiculoDTO>> atualizar(@PathVariable Long id, @RequestBody VeiculoDTO dto) {
        VeiculoDTO updated = veiculoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Veículo atualizado com sucesso.", updated));
    }

    @PatchMapping("/{id}/transferir-proprietario")
    public ResponseEntity<ApiResponse<VeiculoDTO>> transferirProprietario(@PathVariable Long id,
                                                                          @RequestBody TransferenciaProprietarioDTO dto) {
        VeiculoDTO updated = veiculoService.transferirProprietario(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Proprietário do veículo transferido com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VeiculoDTO>> buscar(@PathVariable Long id) {
        VeiculoDTO veiculo = veiculoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Veículo localizado com sucesso.", veiculo));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<VeiculoResumoDTO>>> listar(Pageable pageable) {
        Page<VeiculoResumoDTO> veiculos = veiculoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Veículos localizados com sucesso.", PageResponse.from(veiculos)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<VeiculoResumoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<VeiculoResumoDTO> veiculos = veiculoService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de veículos executada com sucesso.", PageResponse.from(veiculos)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        veiculoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Veículo inativado com sucesso.", null));
    }
}
