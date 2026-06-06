package br.com.avcar.oficina.business.garantia.controller;

import br.com.avcar.oficina.business.garantia.dto.AcionamentoGarantiaDTO;
import br.com.avcar.oficina.business.garantia.dto.GarantiaPecaDTO;
import br.com.avcar.oficina.business.garantia.dto.GarantiaServicoDTO;
import br.com.avcar.oficina.business.garantia.service.GarantiaService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST do módulo Garantias.
 */
@RestController
@RequestMapping("/api/garantias")
public class GarantiaController {

    private final GarantiaService garantiaService;

    public GarantiaController(GarantiaService garantiaService) {
        this.garantiaService = garantiaService;
    }

    @GetMapping("/pecas/{id}")
    public ResponseEntity<ApiResponse<GarantiaPecaDTO>> buscarGarantiaPeca(@PathVariable Long id) {
        GarantiaPecaDTO garantia = garantiaService.buscarGarantiaPeca(id);
        return ResponseEntity.ok(ApiResponse.success("Garantia de peça localizada com sucesso.", garantia));
    }

    @GetMapping("/pecas")
    public ResponseEntity<ApiResponse<PageResponse<GarantiaPecaDTO>>> listarGarantiasPeca(Pageable pageable) {
        Page<GarantiaPecaDTO> garantias = garantiaService.listarGarantiasPeca(pageable);
        return ResponseEntity.ok(ApiResponse.success("Garantias de peças localizadas com sucesso.", PageResponse.from(garantias)));
    }

    @GetMapping("/pecas/item-peca/{idItemPeca}")
    public ResponseEntity<ApiResponse<GarantiaPecaDTO>> buscarGarantiaPorItemPeca(@PathVariable Long idItemPeca) {
        GarantiaPecaDTO garantia = garantiaService.buscarGarantiaPorItemPeca(idItemPeca);
        return ResponseEntity.ok(ApiResponse.success("Garantia da peça aplicada localizada com sucesso.", garantia));
    }

    @GetMapping("/pecas/ordem-servico/{idOrdemServico}")
    public ResponseEntity<ApiResponse<PageResponse<GarantiaPecaDTO>>> listarGarantiasPecaPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                                           Pageable pageable) {
        Page<GarantiaPecaDTO> garantias = garantiaService.listarGarantiasPecaPorOrdemServico(idOrdemServico, pageable);
        return ResponseEntity.ok(ApiResponse.success("Garantias de peças da OS localizadas com sucesso.", PageResponse.from(garantias)));
    }

    @PatchMapping("/pecas/{id}/acionar")
    public ResponseEntity<ApiResponse<GarantiaPecaDTO>> acionarGarantiaPeca(@PathVariable Long id,
                                                                             @RequestBody(required = false) AcionamentoGarantiaDTO dto) {
        GarantiaPecaDTO garantia = garantiaService.acionarGarantiaPeca(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Garantia de peça acionada com sucesso.", garantia));
    }

    @PatchMapping("/pecas/{id}/encerrar")
    public ResponseEntity<ApiResponse<GarantiaPecaDTO>> encerrarGarantiaPeca(@PathVariable Long id,
                                                                              @RequestBody(required = false) AcionamentoGarantiaDTO dto) {
        GarantiaPecaDTO garantia = garantiaService.encerrarGarantiaPeca(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Garantia de peça encerrada com sucesso.", garantia));
    }

    @GetMapping("/servicos/{id}")
    public ResponseEntity<ApiResponse<GarantiaServicoDTO>> buscarGarantiaServico(@PathVariable Long id) {
        GarantiaServicoDTO garantia = garantiaService.buscarGarantiaServico(id);
        return ResponseEntity.ok(ApiResponse.success("Garantia de serviço localizada com sucesso.", garantia));
    }

    @GetMapping("/servicos")
    public ResponseEntity<ApiResponse<PageResponse<GarantiaServicoDTO>>> listarGarantiasServico(Pageable pageable) {
        Page<GarantiaServicoDTO> garantias = garantiaService.listarGarantiasServico(pageable);
        return ResponseEntity.ok(ApiResponse.success("Garantias de serviços localizadas com sucesso.", PageResponse.from(garantias)));
    }

    @GetMapping("/servicos/item-servico/{idItemServico}")
    public ResponseEntity<ApiResponse<GarantiaServicoDTO>> buscarGarantiaPorItemServico(@PathVariable Long idItemServico) {
        GarantiaServicoDTO garantia = garantiaService.buscarGarantiaPorItemServico(idItemServico);
        return ResponseEntity.ok(ApiResponse.success("Garantia do serviço executado localizada com sucesso.", garantia));
    }

    @GetMapping("/servicos/ordem-servico/{idOrdemServico}")
    public ResponseEntity<ApiResponse<PageResponse<GarantiaServicoDTO>>> listarGarantiasServicoPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                                                Pageable pageable) {
        Page<GarantiaServicoDTO> garantias = garantiaService.listarGarantiasServicoPorOrdemServico(idOrdemServico, pageable);
        return ResponseEntity.ok(ApiResponse.success("Garantias de serviços da OS localizadas com sucesso.", PageResponse.from(garantias)));
    }

    @PatchMapping("/servicos/{id}/acionar")
    public ResponseEntity<ApiResponse<GarantiaServicoDTO>> acionarGarantiaServico(@PathVariable Long id,
                                                                                   @RequestBody(required = false) AcionamentoGarantiaDTO dto) {
        GarantiaServicoDTO garantia = garantiaService.acionarGarantiaServico(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Garantia de serviço acionada com sucesso.", garantia));
    }

    @PatchMapping("/servicos/{id}/encerrar")
    public ResponseEntity<ApiResponse<GarantiaServicoDTO>> encerrarGarantiaServico(@PathVariable Long id,
                                                                                    @RequestBody(required = false) AcionamentoGarantiaDTO dto) {
        GarantiaServicoDTO garantia = garantiaService.encerrarGarantiaServico(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Garantia de serviço encerrada com sucesso.", garantia));
    }
}
