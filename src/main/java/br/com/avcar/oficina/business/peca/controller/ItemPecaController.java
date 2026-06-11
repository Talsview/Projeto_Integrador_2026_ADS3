package br.com.avcar.oficina.business.peca.controller;

import br.com.avcar.oficina.business.peca.dto.ItemPecaDTO;
import br.com.avcar.oficina.business.peca.service.ItemPecaService;
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
 * Controller REST do módulo ItemPeca.
 */
@RestController
@RequestMapping("/api/itens-peca")
public class ItemPecaController {

    private final ItemPecaService itemPecaService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ItemPecaController(ItemPecaService itemPecaService) {
        this.itemPecaService = itemPecaService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de peca, encaminha os dados para o serviço e retorna a
     * resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ItemPecaDTO>> cadastrar(@RequestBody ItemPecaDTO dto) {
        ItemPecaDTO saved = itemPecaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Item de peça cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de peca, preservando a validação e a regra de negócio
     * no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ItemPecaDTO>> atualizar(@PathVariable Long id, @RequestBody ItemPecaDTO dto) {
        ItemPecaDTO updated = itemPecaService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Item de peça atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ItemPecaDTO>> buscar(@PathVariable Long id) {
        ItemPecaDTO itemPeca = itemPecaService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Item de peça localizado com sucesso.", itemPeca));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ItemPecaDTO>>> listar(Pageable pageable) {
        Page<ItemPecaDTO> itens = itemPecaService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Itens de peça localizados com sucesso.", PageResponse.from(itens)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}")
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ItemPecaDTO>>> listarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                         Pageable pageable) {
        Page<ItemPecaDTO> itens = itemPecaService.listarPorOrdemServico(idOrdemServico, pageable);
        return ResponseEntity.ok(ApiResponse.success("Peças da ordem de serviço localizadas com sucesso.", PageResponse.from(itens)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}/pesquisar")
    /**
     * Função: Recebe filtros de consulta de peca, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ItemPecaDTO>>> pesquisarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                            @RequestParam String termo,
                                                                                            Pageable pageable) {
        Page<ItemPecaDTO> itens = itemPecaService.pesquisarPorOrdemServico(idOrdemServico, termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de peças da ordem de serviço executada com sucesso.", PageResponse.from(itens)));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        itemPecaService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Item de peça inativado com sucesso.", null));
    }
}
