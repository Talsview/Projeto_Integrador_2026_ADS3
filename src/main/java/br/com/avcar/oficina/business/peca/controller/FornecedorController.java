package br.com.avcar.oficina.business.peca.controller;

import br.com.avcar.oficina.business.peca.dto.FornecedorDTO;
import br.com.avcar.oficina.business.peca.service.FornecedorService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
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
 * Controller REST do módulo Fornecedor.
 */
@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController extends GenericController<FornecedorModel, FornecedorDTO, FornecedorService> {

    private final FornecedorService fornecedorService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public FornecedorController(FornecedorService fornecedorService) {
        super(fornecedorService);
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de peca, encaminha os dados para o serviço e retorna a
     * resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<FornecedorDTO>> cadastrar(@RequestBody FornecedorDTO dto) {
        FornecedorDTO saved = fornecedorService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Fornecedor cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de peca, preservando a validação e a regra de negócio
     * no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<FornecedorDTO>> atualizar(@PathVariable Long id, @RequestBody FornecedorDTO dto) {
        FornecedorDTO updated = fornecedorService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Fornecedor atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<FornecedorDTO>> buscar(@PathVariable Long id) {
        FornecedorDTO fornecedor = fornecedorService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Fornecedor localizado com sucesso.", fornecedor));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<FornecedorDTO>>> listar(Pageable pageable) {
        Page<FornecedorDTO> fornecedores = fornecedorService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Fornecedores localizados com sucesso.", PageResponse.from(fornecedores)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<FornecedorDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<FornecedorDTO> fornecedores = fornecedorService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de fornecedores executada com sucesso.", PageResponse.from(fornecedores)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<FornecedorDTO>>> listarInativos(Pageable pageable) {
        Page<FornecedorDTO> registros = fornecedorService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Fornecedores inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<FornecedorDTO>> ativar(@PathVariable Long id) {
        FornecedorDTO registro = fornecedorService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Fornecedor ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        fornecedorService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Fornecedor inativado com sucesso.", null));
    }
}
