package br.com.avcar.oficina.business.ordemservico.controller;

import br.com.avcar.oficina.business.ordemservico.dto.AlterarStatusOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.business.ordemservico.service.OrdemServicoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
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
 * Controller REST do módulo Ordem de Serviço.
 */
@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController extends GenericController<OrdemServicoModel, OrdemServicoDTO, OrdemServicoService> {

    private final OrdemServicoService ordemServicoService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public OrdemServicoController(OrdemServicoService ordemServicoService) {
        super(ordemServicoService);
        this.ordemServicoService = ordemServicoService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de ordemservico, encaminha os dados para o serviço e
     * retorna a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> cadastrar(@RequestBody OrdemServicoDTO dto) {
        OrdemServicoDTO saved = ordemServicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Ordem de Serviço cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de ordemservico, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> atualizar(@PathVariable Long id,
                                                                  @RequestBody OrdemServicoDTO dto) {
        OrdemServicoDTO updated = ordemServicoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Ordem de Serviço atualizada com sucesso.", updated));
    }

    @PatchMapping("/{id}/status")
    /**
     * Função: Atende a rota HTTP responsável por alterar status e repassa a regra ao serviço
     * correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> alterarStatus(@PathVariable Long id,
                                                                      @RequestBody AlterarStatusOrdemServicoDTO dto) {
        OrdemServicoDTO updated = ordemServicoService.alterarStatusManual(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Status da Ordem de Serviço alterado com sucesso.", updated));
    }

    @PatchMapping({"/{id}/enviar-para-execucao", "/{id}/enviar-para-pagamento"})
    /**
     * Função: Atende a rota HTTP responsável por enviar orcamento para execucao e repassa a regra ao
     * serviço correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> enviarOrcamentoParaExecucao(@PathVariable Long id) {
        OrdemServicoDTO updated = ordemServicoService.enviarOrcamentoParaExecucao(id);
        return ResponseEntity.ok(ApiResponse.success("Orçamento enviado para execução com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de ordemservico, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<OrdemServicoDTO>> buscar(@PathVariable Long id) {
        OrdemServicoDTO ordemServico = ordemServicoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Ordem de Serviço localizada com sucesso.", ordemServico));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de ordemservico, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<OrdemServicoResumoDTO>>> listar(Pageable pageable) {
        Page<OrdemServicoResumoDTO> ordens = ordemServicoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Ordens de Serviço localizadas com sucesso.", PageResponse.from(ordens)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de ordemservico, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<OrdemServicoResumoDTO>>> pesquisar(@RequestParam String termo,
                                                                                       Pageable pageable) {
        Page<OrdemServicoResumoDTO> ordens = ordemServicoService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de Ordens de Serviço executada com sucesso.", PageResponse.from(ordens)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<OrdemServicoResumoDTO>>> listarInativos(Pageable pageable) {
        Page<OrdemServicoResumoDTO> registros = ordemServicoService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Ordens de Serviço inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<OrdemServicoResumoDTO>> ativar(@PathVariable Long id) {
        OrdemServicoResumoDTO registro = ordemServicoService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Ordem de Serviço ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        ordemServicoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Ordem de Serviço inativada com sucesso.", null));
    }
}
