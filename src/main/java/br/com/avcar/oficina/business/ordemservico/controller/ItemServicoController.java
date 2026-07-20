package br.com.avcar.oficina.business.ordemservico.controller;

import br.com.avcar.oficina.business.ordemservico.dto.ItemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.service.ItemServicoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.core.controller.GenericController;
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
public class ItemServicoController extends GenericController<ItemServicoModel, ItemServicoDTO, ItemServicoService> {

    private final ItemServicoService itemServicoService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ItemServicoController(ItemServicoService itemServicoService) {
        super(itemServicoService);
        this.itemServicoService = itemServicoService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de ordemservico, encaminha os dados para o serviço e
     * retorna a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ItemServicoDTO>> cadastrar(@RequestBody ItemServicoDTO dto) {
        ItemServicoDTO saved = itemServicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Item de Serviço cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de ordemservico, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ItemServicoDTO>> atualizar(@PathVariable Long id,
                                                                 @RequestBody ItemServicoDTO dto) {
        ItemServicoDTO updated = itemServicoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Item de Serviço atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de ordemservico, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ItemServicoDTO>> buscar(@PathVariable Long id) {
        ItemServicoDTO item = itemServicoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Item de Serviço localizado com sucesso.", item));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de ordemservico, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ItemServicoDTO>>> listar(Pageable pageable) {
        Page<ItemServicoDTO> itens = itemServicoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Itens de Serviço localizados com sucesso.", PageResponse.from(itens)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}")
    /**
     * Função: Recebe filtros de consulta de ordemservico, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ItemServicoDTO>>> listarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                           Pageable pageable) {
        Page<ItemServicoDTO> itens = itemServicoService.listarPorOrdemServico(idOrdemServico, pageable);
        return ResponseEntity.ok(ApiResponse.success("Itens de Serviço da OS localizados com sucesso.", PageResponse.from(itens)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}/pesquisar")
    /**
     * Função: Recebe filtros de consulta de ordemservico, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ItemServicoDTO>>> pesquisarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                              @RequestParam String termo,
                                                                                              Pageable pageable) {
        Page<ItemServicoDTO> itens = itemServicoService.pesquisarPorOrdemServico(idOrdemServico, termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de Itens de Serviço executada com sucesso.", PageResponse.from(itens)));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        itemServicoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Item de Serviço inativado com sucesso.", null));
    }
}
