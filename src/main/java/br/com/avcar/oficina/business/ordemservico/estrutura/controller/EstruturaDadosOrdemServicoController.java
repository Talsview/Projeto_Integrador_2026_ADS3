package br.com.avcar.oficina.business.ordemservico.estrutura.controller;

import br.com.avcar.oficina.business.ordemservico.estrutura.dto.CalculoRecursivoTotalOSDTO;
import br.com.avcar.oficina.business.ordemservico.estrutura.dto.FilaAtendimentoOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.estrutura.dto.ResultadoOrdenacaoOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.estrutura.dto.ResultadoPesquisaOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.estrutura.enums.CriterioOrdenacaoOrdemServico;
import br.com.avcar.oficina.business.ordemservico.estrutura.service.EstruturaDadosOrdemServicoService;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.core.controller.GenericController;
import br.com.avcar.oficina.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST com as evidências de Estrutura de Dados I aplicadas ao módulo
 * de Ordem de Serviço.
 */
@RestController
@RequestMapping("/api/estrutura-dados/ordens-servico")
public class EstruturaDadosOrdemServicoController extends GenericController<OrdemServicoModel, OrdemServicoResumoDTO, EstruturaDadosOrdemServicoService> {

    private final EstruturaDadosOrdemServicoService service;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public EstruturaDadosOrdemServicoController(EstruturaDadosOrdemServicoService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/fila-atendimento")
    /**
     * Função: Atende a rota HTTP responsável por fila atendimento e repassa a regra ao serviço
     * correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<FilaAtendimentoOrdemServicoDTO>> filaAtendimento() {
        FilaAtendimentoOrdemServicoDTO dto = service.montarFilaAtendimento();
        return ResponseEntity.ok(ApiResponse.success("Fila de atendimento das Ordens de Serviço gerada com sucesso.", dto));
    }

    @GetMapping("/ordenar")
    /**
     * Função: Atende a rota HTTP responsável por ordenar e repassa a regra ao serviço correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ResultadoOrdenacaoOrdemServicoDTO>> ordenar(
            @RequestParam(defaultValue = "DATA_ABERTURA") CriterioOrdenacaoOrdemServico criterio) {
        ResultadoOrdenacaoOrdemServicoDTO dto = service.ordenar(criterio);
        return ResponseEntity.ok(ApiResponse.success("Ordenação manual das Ordens de Serviço executada com sucesso.", dto));
    }

    @GetMapping("/pesquisar-linear")
    /**
     * Função: Recebe filtros de consulta de ordemservico, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ResultadoPesquisaOrdemServicoDTO>> pesquisarLinear(@RequestParam(required = false) String termo) {
        ResultadoPesquisaOrdemServicoDTO dto = service.pesquisarLinear(termo);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa linear das Ordens de Serviço executada com sucesso.", dto));
    }

    @GetMapping("/{idOrdemServico}/total-recursivo")
    /**
     * Função: Atende a rota HTTP responsável por calcular total recursivo e repassa a regra ao serviço
     * correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<CalculoRecursivoTotalOSDTO>> calcularTotalRecursivo(@PathVariable Long idOrdemServico) {
        CalculoRecursivoTotalOSDTO dto = service.calcularTotalRecursivo(idOrdemServico);
        return ResponseEntity.ok(ApiResponse.success("Cálculo recursivo do total da Ordem de Serviço executado com sucesso.", dto));
    }
}
