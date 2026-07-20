package br.com.avcar.oficina.business.pessoa.controller;

import br.com.avcar.oficina.business.pessoa.dto.ClienteDetalheDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaFisicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaJuridicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClienteResumoDTO;
import br.com.avcar.oficina.business.pessoa.service.ClienteService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
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
 * Controller REST do módulo Cliente.
 * A View Angular deverá consumir estes endpoints.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController extends GenericController<ClienteModel, ClienteResumoDTO, ClienteService> {

    private final ClienteService clienteService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ClienteController(ClienteService clienteService) {
        super(clienteService);
        this.clienteService = clienteService;
    }

    @PostMapping("/pessoa-fisica")
    /**
     * Função: Recebe a requisição de cadastro de cliente, encaminha os dados para o serviço e retorna
     * a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ClientePessoaFisicaDTO>> cadastrarPessoaFisica(@RequestBody ClientePessoaFisicaDTO dto) {
        ClientePessoaFisicaDTO saved = clienteService.cadastrarPessoaFisica(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Cliente pessoa física cadastrado com sucesso.", saved));
    }

    @PostMapping("/pessoa-juridica")
    /**
     * Função: Recebe a requisição de cadastro de cliente, encaminha os dados para o serviço e retorna
     * a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ClientePessoaJuridicaDTO>> cadastrarPessoaJuridica(@RequestBody ClientePessoaJuridicaDTO dto) {
        ClientePessoaJuridicaDTO saved = clienteService.cadastrarPessoaJuridica(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Cliente pessoa jurídica cadastrado com sucesso.", saved));
    }

    @PutMapping("/pessoa-fisica/{id}")
    /**
     * Função: Recebe a requisição de atualização de cliente, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ClientePessoaFisicaDTO>> atualizarPessoaFisica(@PathVariable Long id,
                                                                                     @RequestBody ClientePessoaFisicaDTO dto) {
        ClientePessoaFisicaDTO updated = clienteService.atualizarPessoaFisica(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cliente pessoa física atualizado com sucesso.", updated));
    }

    @PutMapping("/pessoa-juridica/{id}")
    /**
     * Função: Recebe a requisição de atualização de cliente, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ClientePessoaJuridicaDTO>> atualizarPessoaJuridica(@PathVariable Long id,
                                                                                         @RequestBody ClientePessoaJuridicaDTO dto) {
        ClientePessoaJuridicaDTO updated = clienteService.atualizarPessoaJuridica(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cliente pessoa jurídica atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de cliente, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ClienteDetalheDTO>> buscarDetalhado(@PathVariable Long id) {
        ClienteDetalheDTO detalhe = clienteService.buscarDetalhado(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente localizado com sucesso.", detalhe));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de cliente, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ClienteResumoDTO>>> listar(Pageable pageable) {
        Page<ClienteResumoDTO> clientes = clienteService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Clientes localizados com sucesso.", PageResponse.from(clientes)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de cliente, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ClienteResumoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<ClienteResumoDTO> clientes = clienteService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de clientes executada com sucesso.", PageResponse.from(clientes)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ClienteResumoDTO>>> listarInativos(Pageable pageable) {
        Page<ClienteResumoDTO> registros = clienteService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Clientes inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ClienteResumoDTO>> ativar(@PathVariable Long id) {
        ClienteResumoDTO registro = clienteService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente inativado com sucesso.", null));
    }
}
