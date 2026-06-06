package br.com.avcar.oficina.business.ordemservico.controller;

import br.com.avcar.oficina.business.ordemservico.dto.ItemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.service.ItemServicoService;
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
 * Controller REST dos serviços executados dentro da OS.
 */
@RestController
@RequestMapping("/api/itens-servico")
public class ItemServicoController {

    private final ItemServicoService itemServicoService;

    public ItemServicoController(ItemServicoService itemServicoService) {
        this.itemServicoService = itemServicoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItemServicoDTO>> cadastrar(@RequestBody ItemServicoDTO dto) {
        ItemServicoDTO saved = itemServicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Item de Serviço cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemServicoDTO>> atualizar(@PathVariable Long id,
                                                                 @RequestBody ItemServicoDTO dto) {
        ItemServicoDTO updated = itemServicoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Item de Serviço atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemServicoDTO>> buscar(@PathVariable Long id) {
        ItemServicoDTO item = itemServicoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Item de Serviço localizado com sucesso.", item));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ItemServicoDTO>>> listar(Pageable pageable) {
        Page<ItemServicoDTO> itens = itemServicoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Itens de Serviço localizados com sucesso.", PageResponse.from(itens)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}")
    public ResponseEntity<ApiResponse<PageResponse<ItemServicoDTO>>> listarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                           Pageable pageable) {
        Page<ItemServicoDTO> itens = itemServicoService.listarPorOrdemServico(idOrdemServico, pageable);
        return ResponseEntity.ok(ApiResponse.success("Itens de Serviço da OS localizados com sucesso.", PageResponse.from(itens)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<ItemServicoDTO>>> pesquisarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                              @RequestParam String termo,
                                                                                              Pageable pageable) {
        Page<ItemServicoDTO> itens = itemServicoService.pesquisarPorOrdemServico(idOrdemServico, termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de Itens de Serviço executada com sucesso.", PageResponse.from(itens)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        itemServicoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Item de Serviço inativado com sucesso.", null));
    }
}
