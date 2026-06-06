package br.com.avcar.oficina.business.pessoa.controller;

import br.com.avcar.oficina.business.pessoa.dto.ClienteDetalheDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaFisicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaJuridicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClienteResumoDTO;
import br.com.avcar.oficina.business.pessoa.service.ClienteService;
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
 * Controller REST do módulo Cliente.
 * A View Angular deverá consumir estes endpoints.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/pessoa-fisica")
    public ResponseEntity<ApiResponse<ClientePessoaFisicaDTO>> cadastrarPessoaFisica(@RequestBody ClientePessoaFisicaDTO dto) {
        ClientePessoaFisicaDTO saved = clienteService.cadastrarPessoaFisica(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Cliente pessoa física cadastrado com sucesso.", saved));
    }

    @PostMapping("/pessoa-juridica")
    public ResponseEntity<ApiResponse<ClientePessoaJuridicaDTO>> cadastrarPessoaJuridica(@RequestBody ClientePessoaJuridicaDTO dto) {
        ClientePessoaJuridicaDTO saved = clienteService.cadastrarPessoaJuridica(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Cliente pessoa jurídica cadastrado com sucesso.", saved));
    }

    @PutMapping("/pessoa-fisica/{id}")
    public ResponseEntity<ApiResponse<ClientePessoaFisicaDTO>> atualizarPessoaFisica(@PathVariable Long id,
                                                                                     @RequestBody ClientePessoaFisicaDTO dto) {
        ClientePessoaFisicaDTO updated = clienteService.atualizarPessoaFisica(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cliente pessoa física atualizado com sucesso.", updated));
    }

    @PutMapping("/pessoa-juridica/{id}")
    public ResponseEntity<ApiResponse<ClientePessoaJuridicaDTO>> atualizarPessoaJuridica(@PathVariable Long id,
                                                                                         @RequestBody ClientePessoaJuridicaDTO dto) {
        ClientePessoaJuridicaDTO updated = clienteService.atualizarPessoaJuridica(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cliente pessoa jurídica atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDetalheDTO>> buscarDetalhado(@PathVariable Long id) {
        ClienteDetalheDTO detalhe = clienteService.buscarDetalhado(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente localizado com sucesso.", detalhe));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ClienteResumoDTO>>> listar(Pageable pageable) {
        Page<ClienteResumoDTO> clientes = clienteService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Clientes localizados com sucesso.", PageResponse.from(clientes)));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ApiResponse<PageResponse<ClienteResumoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<ClienteResumoDTO> clientes = clienteService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de clientes executada com sucesso.", PageResponse.from(clientes)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente inativado com sucesso.", null));
    }
}
