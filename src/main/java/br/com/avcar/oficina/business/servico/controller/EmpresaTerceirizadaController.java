package br.com.avcar.oficina.business.servico.controller;

import br.com.avcar.oficina.business.servico.dto.EmpresaTerceirizadaDTO;
import br.com.avcar.oficina.business.servico.service.EmpresaTerceirizadaService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
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
 * Controller REST do módulo Empresa Terceirizada.
 */
@RestController
@RequestMapping("/api/empresas-terceirizadas")
public class EmpresaTerceirizadaController extends GenericController<EmpresaTerceirizadaModel, EmpresaTerceirizadaDTO, EmpresaTerceirizadaService> {

    private final EmpresaTerceirizadaService empresaService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public EmpresaTerceirizadaController(EmpresaTerceirizadaService empresaService) {
        super(empresaService);
        this.empresaService = empresaService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de servico, encaminha os dados para o serviço e retorna
     * a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<EmpresaTerceirizadaDTO>> cadastrar(@RequestBody EmpresaTerceirizadaDTO dto) {
        EmpresaTerceirizadaDTO saved = empresaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Empresa terceirizada cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de servico, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<EmpresaTerceirizadaDTO>> atualizar(@PathVariable Long id,
                                                                         @RequestBody EmpresaTerceirizadaDTO dto) {
        EmpresaTerceirizadaDTO updated = empresaService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Empresa terceirizada atualizada com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de servico, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<EmpresaTerceirizadaDTO>> buscar(@PathVariable Long id) {
        EmpresaTerceirizadaDTO empresa = empresaService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Empresa terceirizada localizada com sucesso.", empresa));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de servico, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<EmpresaTerceirizadaDTO>>> listar(Pageable pageable) {
        Page<EmpresaTerceirizadaDTO> empresas = empresaService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Empresas terceirizadas localizadas com sucesso.", PageResponse.from(empresas)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de servico, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<EmpresaTerceirizadaDTO>>> pesquisar(@RequestParam String termo,
                                                                                        Pageable pageable) {
        Page<EmpresaTerceirizadaDTO> empresas = empresaService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de empresas terceirizadas executada com sucesso.", PageResponse.from(empresas)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<EmpresaTerceirizadaDTO>>> listarInativos(Pageable pageable) {
        Page<EmpresaTerceirizadaDTO> registros = empresaService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Empresas terceirizadas inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<EmpresaTerceirizadaDTO>> ativar(@PathVariable Long id) {
        EmpresaTerceirizadaDTO registro = empresaService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Empresa terceirizada ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        empresaService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Empresa terceirizada inativada com sucesso.", null));
    }
}
