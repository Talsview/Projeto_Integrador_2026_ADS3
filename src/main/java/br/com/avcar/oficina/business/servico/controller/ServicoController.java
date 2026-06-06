package br.com.avcar.oficina.business.servico.controller;

import br.com.avcar.oficina.business.servico.dto.ServicoDTO;
import br.com.avcar.oficina.business.servico.enums.TipoServico;
import br.com.avcar.oficina.business.servico.service.ServicoService;
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
 * Controller REST do módulo Serviço.
 */
@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServicoDTO>> cadastrar(@RequestBody ServicoDTO dto) {
        ServicoDTO saved = servicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Serviço cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServicoDTO>> atualizar(@PathVariable Long id, @RequestBody ServicoDTO dto) {
        ServicoDTO updated = servicoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Serviço atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServicoDTO>> buscar(@PathVariable Long id) {
        ServicoDTO servico = servicoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Serviço localizado com sucesso.", servico));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ServicoDTO>>> listar(Pageable pageable) {
        Page<ServicoDTO> servicos = servicoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Serviços localizados com sucesso.", PageResponse.from(servicos)));
    }

    @GetMapping("/tipo/{tipoServico}")
    public ResponseEntity<ApiResponse<PageResponse<ServicoDTO>>> listarPorTipo(@PathVariable TipoServico tipoServico,
                                                                                Pageable pageable) {
        Page<ServicoDTO> servicos = servicoService.listarPorTipo(tipoServico, pageable);
        return ResponseEntity.ok(ApiResponse.success("Serviços localizados por tipo com sucesso.", PageResponse.from(servicos)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<ServicoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<ServicoDTO> servicos = servicoService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de serviços executada com sucesso.", PageResponse.from(servicos)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        servicoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Serviço inativado com sucesso.", null));
    }
}
