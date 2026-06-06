package br.com.avcar.oficina.business.servico.controller;

import br.com.avcar.oficina.business.servico.dto.EmpresaTerceirizadaDTO;
import br.com.avcar.oficina.business.servico.service.EmpresaTerceirizadaService;
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
 * Controller REST do módulo Empresa Terceirizada.
 */
@RestController
@RequestMapping("/api/empresas-terceirizadas")
public class EmpresaTerceirizadaController {

    private final EmpresaTerceirizadaService empresaService;

    public EmpresaTerceirizadaController(EmpresaTerceirizadaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpresaTerceirizadaDTO>> cadastrar(@RequestBody EmpresaTerceirizadaDTO dto) {
        EmpresaTerceirizadaDTO saved = empresaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Empresa terceirizada cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpresaTerceirizadaDTO>> atualizar(@PathVariable Long id,
                                                                         @RequestBody EmpresaTerceirizadaDTO dto) {
        EmpresaTerceirizadaDTO updated = empresaService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Empresa terceirizada atualizada com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpresaTerceirizadaDTO>> buscar(@PathVariable Long id) {
        EmpresaTerceirizadaDTO empresa = empresaService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Empresa terceirizada localizada com sucesso.", empresa));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<EmpresaTerceirizadaDTO>>> listar(Pageable pageable) {
        Page<EmpresaTerceirizadaDTO> empresas = empresaService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Empresas terceirizadas localizadas com sucesso.", PageResponse.from(empresas)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<EmpresaTerceirizadaDTO>>> pesquisar(@RequestParam String termo,
                                                                                        Pageable pageable) {
        Page<EmpresaTerceirizadaDTO> empresas = empresaService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de empresas terceirizadas executada com sucesso.", PageResponse.from(empresas)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        empresaService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Empresa terceirizada inativada com sucesso.", null));
    }
}
