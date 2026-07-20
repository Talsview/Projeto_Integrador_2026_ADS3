package br.com.avcar.oficina.business.pagamento.controller;

import br.com.avcar.oficina.business.pagamento.dto.PagamentoDTO;
import br.com.avcar.oficina.business.pagamento.dto.ResumoPagamentoOrdemServicoDTO;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.business.pagamento.service.PagamentoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.pagamento.model.PagamentoModel;
import br.com.avcar.oficina.core.controller.GenericController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST responsável pelos pagamentos vinculados às Ordens de Serviço.
 */
@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController extends GenericController<PagamentoModel, PagamentoDTO, PagamentoService> {

    private final PagamentoService pagamentoService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public PagamentoController(PagamentoService pagamentoService) {
        super(pagamentoService);
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de pagamento, encaminha os dados para o serviço e
     * retorna a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PagamentoDTO>> cadastrar(@RequestBody PagamentoDTO dto) {
        PagamentoDTO saved = pagamentoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Pagamento cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de pagamento, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PagamentoDTO>> atualizar(@PathVariable Long id,
                                                               @RequestBody PagamentoDTO dto) {
        PagamentoDTO updated = pagamentoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Pagamento atualizado com sucesso.", updated));
    }

    @PatchMapping("/{id}/status")
    /**
     * Função: Atende a rota HTTP responsável por alterar status e repassa a regra ao serviço
     * correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PagamentoDTO>> alterarStatus(@PathVariable Long id,
                                                                   @RequestParam StatusPagamento statusPagamento) {
        PagamentoDTO updated = pagamentoService.alterarStatus(id, statusPagamento);
        return ResponseEntity.ok(ApiResponse.success("Status do pagamento alterado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de pagamento, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PagamentoDTO>> buscar(@PathVariable Long id) {
        PagamentoDTO pagamento = pagamentoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Pagamento localizado com sucesso.", pagamento));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de pagamento, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<PagamentoDTO>>> listar(Pageable pageable) {
        Page<PagamentoDTO> pagamentos = pagamentoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Pagamentos localizados com sucesso.", PageResponse.from(pagamentos)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}")
    /**
     * Função: Recebe filtros de consulta de pagamento, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<PagamentoDTO>>> listarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                         Pageable pageable) {
        Page<PagamentoDTO> pagamentos = pagamentoService.listarPorOrdemServico(idOrdemServico, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pagamentos da Ordem de Serviço localizados com sucesso.", PageResponse.from(pagamentos)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}/pesquisar")
    /**
     * Função: Recebe filtros de consulta de pagamento, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<PagamentoDTO>>> pesquisarPorOrdemServico(@PathVariable Long idOrdemServico,
                                                                                            @RequestParam String termo,
                                                                                            Pageable pageable) {
        Page<PagamentoDTO> pagamentos = pagamentoService.pesquisarPorOrdemServico(idOrdemServico, termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de pagamentos executada com sucesso.", PageResponse.from(pagamentos)));
    }

    @GetMapping("/ordem-servico/{idOrdemServico}/resumo")
    /**
     * Função: Atende a rota HTTP responsável por resumir por ordem servico e repassa a regra ao
     * serviço correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ResumoPagamentoOrdemServicoDTO>> resumirPorOrdemServico(@PathVariable Long idOrdemServico) {
        ResumoPagamentoOrdemServicoDTO resumo = pagamentoService.resumirPorOrdemServico(idOrdemServico);
        return ResponseEntity.ok(ApiResponse.success("Resumo financeiro da Ordem de Serviço localizado com sucesso.", resumo));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<PagamentoDTO>>> listarInativos(Pageable pageable) {
        Page<PagamentoDTO> registros = pagamentoService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Pagamentos inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PagamentoDTO>> ativar(@PathVariable Long id) {
        PagamentoDTO registro = pagamentoService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Pagamento ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        pagamentoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Pagamento inativado com sucesso.", null));
    }
}
