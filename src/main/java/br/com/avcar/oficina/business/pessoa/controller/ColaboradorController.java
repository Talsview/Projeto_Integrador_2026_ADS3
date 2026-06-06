package br.com.avcar.oficina.business.pessoa.controller;

import br.com.avcar.oficina.business.pessoa.dto.ColaboradorDTO;
import br.com.avcar.oficina.business.pessoa.dto.ColaboradorResumoDTO;
import br.com.avcar.oficina.business.pessoa.service.ColaboradorService;
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
 * Controller REST do módulo Colaborador.
 *
 * Reforço acadêmico: funções como Mecânico e Atendente são recebidas por id_funcao,
 * e não por entidades específicas separadas.
 */
@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ColaboradorDTO>> cadastrar(@RequestBody ColaboradorDTO dto) {
        ColaboradorDTO saved = colaboradorService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Colaborador cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ColaboradorDTO>> atualizar(@PathVariable Long id, @RequestBody ColaboradorDTO dto) {
        ColaboradorDTO updated = colaboradorService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Colaborador atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ColaboradorDTO>> buscar(@PathVariable Long id) {
        ColaboradorDTO colaborador = colaboradorService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Colaborador localizado com sucesso.", colaborador));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ColaboradorResumoDTO>>> listar(Pageable pageable) {
        Page<ColaboradorResumoDTO> colaboradores = colaboradorService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Colaboradores localizados com sucesso.", PageResponse.from(colaboradores)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<ColaboradorResumoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<ColaboradorResumoDTO> colaboradores = colaboradorService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de colaboradores executada com sucesso.", PageResponse.from(colaboradores)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        colaboradorService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Colaborador inativado com sucesso.", null));
    }
}
