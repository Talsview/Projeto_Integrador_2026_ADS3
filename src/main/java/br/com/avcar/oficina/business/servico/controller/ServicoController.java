package br.com.avcar.oficina.business.servico.controller;

import br.com.avcar.oficina.business.servico.dto.ServicoDTO;
import br.com.avcar.oficina.business.servico.enums.TipoServico;
import br.com.avcar.oficina.business.servico.service.ServicoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.servico.model.ServicoModel;
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
 * Controller REST do módulo Serviço.
 */
@RestController
@RequestMapping("/api/servicos")
public class ServicoController extends GenericController<ServicoModel, ServicoDTO, ServicoService> {

    private final ServicoService servicoService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ServicoController(ServicoService servicoService) {
        super(servicoService);
        this.servicoService = servicoService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de servico, encaminha os dados para o serviço e retorna
     * a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ServicoDTO>> cadastrar(@RequestBody ServicoDTO dto) {
        ServicoDTO saved = servicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Serviço cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de servico, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ServicoDTO>> atualizar(@PathVariable Long id, @RequestBody ServicoDTO dto) {
        ServicoDTO updated = servicoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Serviço atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de servico, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ServicoDTO>> buscar(@PathVariable Long id) {
        ServicoDTO servico = servicoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Serviço localizado com sucesso.", servico));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de servico, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ServicoDTO>>> listar(Pageable pageable) {
        Page<ServicoDTO> servicos = servicoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Serviços localizados com sucesso.", PageResponse.from(servicos)));
    }

    @GetMapping("/tipo/{tipoServico}")
    /**
     * Função: Recebe filtros de consulta de servico, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ServicoDTO>>> listarPorTipo(@PathVariable TipoServico tipoServico,
                                                                                Pageable pageable) {
        Page<ServicoDTO> servicos = servicoService.listarPorTipo(tipoServico, pageable);
        return ResponseEntity.ok(ApiResponse.success("Serviços localizados por tipo com sucesso.", PageResponse.from(servicos)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de servico, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ServicoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<ServicoDTO> servicos = servicoService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de serviços executada com sucesso.", PageResponse.from(servicos)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ServicoDTO>>> listarInativos(Pageable pageable) {
        Page<ServicoDTO> registros = servicoService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Serviços inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ServicoDTO>> ativar(@PathVariable Long id) {
        ServicoDTO registro = servicoService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Serviço ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        servicoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Serviço inativado com sucesso.", null));
    }
}
