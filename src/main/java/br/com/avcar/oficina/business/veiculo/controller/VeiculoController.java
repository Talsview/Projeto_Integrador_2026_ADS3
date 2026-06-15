package br.com.avcar.oficina.business.veiculo.controller;

import br.com.avcar.oficina.business.veiculo.dto.HistoricoProprietarioDTO;
import br.com.avcar.oficina.business.veiculo.dto.TransferenciaProprietarioDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoResumoDTO;
import br.com.avcar.oficina.business.veiculo.service.VeiculoService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
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
import java.util.List;

/**
 * Controller REST do módulo Veículo.
 *
 * Regra acadêmica: a relação Cliente-Veículo é controlada por HistoricoProprietario,
 * preservando o histórico de donos ao longo do tempo.
 */
@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de veiculo, encaminha os dados para o serviço e retorna
     * a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<VeiculoDTO>> cadastrar(@RequestBody VeiculoDTO dto) {
        VeiculoDTO saved = veiculoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Veículo cadastrado com histórico inicial de proprietário.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de veiculo, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<VeiculoDTO>> atualizar(@PathVariable Long id, @RequestBody VeiculoDTO dto) {
        VeiculoDTO updated = veiculoService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Veículo atualizado com sucesso.", updated));
    }

    @PatchMapping("/{id}/transferir-proprietario")
    /**
     * Função: Atende a rota HTTP responsável por transferir proprietario e repassa a regra ao serviço
     * correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<VeiculoDTO>> transferirProprietario(@PathVariable Long id,
                                                                          @RequestBody TransferenciaProprietarioDTO dto) {
        VeiculoDTO updated = veiculoService.transferirProprietario(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Proprietário do veículo transferido com sucesso.", updated));
    }


    @GetMapping("/historico-proprietarios")
    /**
     * Função: consulta todos os vínculos de posse registrados no sistema.
     * Uso no sistema: alimenta a aba de Gestão agrupando clientes sem duplicidade e mantendo
     * proprietários antigos visíveis mesmo depois de uma transferência.
     */
    public ResponseEntity<ApiResponse<List<HistoricoProprietarioDTO>>> listarHistoricoProprietariosConsolidado() {
        List<HistoricoProprietarioDTO> historico = veiculoService.listarHistoricoProprietariosConsolidado();
        return ResponseEntity.ok(ApiResponse.success("Histórico consolidado de proprietários localizado com sucesso.", historico));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de veiculo, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<VeiculoDTO>> buscar(@PathVariable Long id) {
        VeiculoDTO veiculo = veiculoService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Veículo localizado com sucesso.", veiculo));
    }


    @GetMapping("/{id}/historico-proprietarios")
    /**
     * Função: consulta o histórico de proprietários vinculado ao veículo informado.
     * Uso no sistema: permite que a tela Histórico de Proprietários apresente a rastreabilidade
     * completa de posse do veículo, incluindo proprietário atual e proprietários anteriores.
     */
    public ResponseEntity<ApiResponse<List<HistoricoProprietarioDTO>>> listarHistoricoProprietarios(@PathVariable Long id) {
        List<HistoricoProprietarioDTO> historico = veiculoService.listarHistoricoProprietarios(id);
        return ResponseEntity.ok(ApiResponse.success("Histórico de proprietários localizado com sucesso.", historico));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de veiculo, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<VeiculoResumoDTO>>> listar(Pageable pageable) {
        Page<VeiculoResumoDTO> veiculos = veiculoService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Veículos localizados com sucesso.", PageResponse.from(veiculos)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de veiculo, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<VeiculoResumoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<VeiculoResumoDTO> veiculos = veiculoService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de veículos executada com sucesso.", PageResponse.from(veiculos)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<VeiculoResumoDTO>>> listarInativos(Pageable pageable) {
        Page<VeiculoResumoDTO> registros = veiculoService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Veículos inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<VeiculoResumoDTO>> ativar(@PathVariable Long id) {
        VeiculoResumoDTO registro = veiculoService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Veículo ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        veiculoService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Veículo inativado com sucesso.", null));
    }
}
