package br.com.avcar.oficina.business.pessoa.controller;

import br.com.avcar.oficina.business.pessoa.dto.ColaboradorDTO;
import br.com.avcar.oficina.business.pessoa.dto.ColaboradorResumoDTO;
import br.com.avcar.oficina.business.pessoa.service.ColaboradorService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.pessoa.model.ColaboradorModel;
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
 * Controller REST do módulo Colaborador.
 *
 * Reforço acadêmico: funções como Mecânico e Atendente são recebidas por id_funcao,
 * e não por entidades específicas separadas.
 */
@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController extends GenericController<ColaboradorModel, ColaboradorDTO, ColaboradorService> {

    private final ColaboradorService colaboradorService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ColaboradorController(ColaboradorService colaboradorService) {
        super(colaboradorService);
        this.colaboradorService = colaboradorService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de colaborador, encaminha os dados para o serviço e
     * retorna a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ColaboradorDTO>> cadastrar(@RequestBody ColaboradorDTO dto) {
        ColaboradorDTO saved = colaboradorService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Colaborador cadastrado com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de colaborador, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ColaboradorDTO>> atualizar(@PathVariable Long id, @RequestBody ColaboradorDTO dto) {
        ColaboradorDTO updated = colaboradorService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Colaborador atualizado com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de colaborador, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ColaboradorDTO>> buscar(@PathVariable Long id) {
        ColaboradorDTO colaborador = colaboradorService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Colaborador localizado com sucesso.", colaborador));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de colaborador, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ColaboradorResumoDTO>>> listar(Pageable pageable) {
        Page<ColaboradorResumoDTO> colaboradores = colaboradorService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Colaboradores localizados com sucesso.", PageResponse.from(colaboradores)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de colaborador, delega a busca ao serviço e devolve os dados
     * no formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ColaboradorResumoDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<ColaboradorResumoDTO> colaboradores = colaboradorService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de colaboradores executada com sucesso.", PageResponse.from(colaboradores)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<ColaboradorResumoDTO>>> listarInativos(Pageable pageable) {
        Page<ColaboradorResumoDTO> registros = colaboradorService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Colaboradores inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<ColaboradorResumoDTO>> ativar(@PathVariable Long id) {
        ColaboradorResumoDTO registro = colaboradorService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Colaborador ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        colaboradorService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Colaborador inativado com sucesso.", null));
    }
}
