package br.com.avcar.oficina.business.peca.controller;

import br.com.avcar.oficina.business.peca.dto.PecaDTO;
import br.com.avcar.oficina.business.peca.service.PecaService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.peca.model.PecaModel;
import br.com.avcar.oficina.core.controller.GenericController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST do módulo Peça.
 */
@RestController
@RequestMapping("/api/pecas")
public class PecaController extends GenericController<PecaModel, PecaDTO, PecaService> {

    private final PecaService pecaService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public PecaController(PecaService pecaService) {
        super(pecaService);
        this.pecaService = pecaService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de peca, encaminha os dados para o serviço e retorna a
     * resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PecaDTO>> cadastrar(@RequestBody PecaDTO dto) {
        PecaDTO saved = pecaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Peça cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de peca, preservando a validação e a regra de negócio
     * no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PecaDTO>> atualizar(@PathVariable Long id, @RequestBody PecaDTO dto) {
        PecaDTO updated = pecaService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Peça atualizada com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PecaDTO>> buscar(@PathVariable Long id) {
        PecaDTO peca = pecaService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Peça localizada com sucesso.", peca));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<PecaDTO>>> listar(Pageable pageable) {
        Page<PecaDTO> pecas = pecaService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Peças localizadas com sucesso.", PageResponse.from(pecas)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<PecaDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<PecaDTO> pecas = pecaService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de peças executada com sucesso.", PageResponse.from(pecas)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<PecaDTO>>> listarInativos(Pageable pageable) {
        Page<PecaDTO> registros = pecaService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Peças inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PecaDTO>> ativar(@PathVariable Long id) {
        PecaDTO registro = pecaService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Peça ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        pecaService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Peça inativada com sucesso.", null));
    }
}
