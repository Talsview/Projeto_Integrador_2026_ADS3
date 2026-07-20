package br.com.avcar.oficina.business.pessoa.controller;

import br.com.avcar.oficina.business.pessoa.dto.FuncaoDTO;
import br.com.avcar.oficina.business.pessoa.service.FuncaoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.pessoa.model.FuncaoModel;
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
 * Controller REST do módulo Função.
 * A View Angular deverá consumir estes endpoints para manter funções como registros.
 */
@RestController
@RequestMapping("/api/funcoes")
public class FuncaoController extends GenericController<FuncaoModel, FuncaoDTO, FuncaoService> {

    private final FuncaoService funcaoService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public FuncaoController(FuncaoService funcaoService) {
        super(funcaoService);
        this.funcaoService = funcaoService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de funcao, encaminha os dados para o serviço e retorna a
     * resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<FuncaoDTO>> cadastrar(@RequestBody FuncaoDTO dto) {
        FuncaoDTO saved = funcaoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Função cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de funcao, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<FuncaoDTO>> atualizar(@PathVariable Long id, @RequestBody FuncaoDTO dto) {
        FuncaoDTO updated = funcaoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Função atualizada com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de funcao, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<FuncaoDTO>> buscar(@PathVariable Long id) {
        FuncaoDTO funcao = funcaoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Função localizada com sucesso.", funcao));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de funcao, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<FuncaoDTO>>> listar(Pageable pageable) {
        Page<FuncaoDTO> funcoes = funcaoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Funções localizadas com sucesso.", PageResponse.from(funcoes)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de funcao, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<FuncaoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<FuncaoDTO> funcoes = funcaoService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de funções executada com sucesso.", PageResponse.from(funcoes)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<FuncaoDTO>>> listarInativos(Pageable pageable) {
        Page<FuncaoDTO> registros = funcaoService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Funções inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<FuncaoDTO>> ativar(@PathVariable Long id) {
        FuncaoDTO registro = funcaoService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Função ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        funcaoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Função inativada com sucesso.", null));
    }
}
