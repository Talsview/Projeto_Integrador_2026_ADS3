package br.com.avcar.oficina.business.veiculo.controller;

import br.com.avcar.oficina.business.veiculo.dto.MarcaDTO;
import br.com.avcar.oficina.business.veiculo.service.MarcaService;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
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
 * Controller REST do módulo Marca.
 */
@RestController
@RequestMapping("/api/marcas")
public class MarcaController extends GenericController<MarcaModel, MarcaDTO, MarcaService> {

    private final MarcaService marcaService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public MarcaController(MarcaService marcaService) {
        super(marcaService);
        this.marcaService = marcaService;
    }

    @PostMapping
    /**
     * Função: Recebe a requisição de cadastro de veiculo, encaminha os dados para o serviço e retorna
     * a resposta da operação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<MarcaDTO>> cadastrar(@RequestBody MarcaDTO dto) {
        MarcaDTO saved = marcaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Marca cadastrada com sucesso.", saved));
    }

    @PutMapping("/{id}")
    /**
     * Função: Recebe a requisição de atualização de veiculo, preservando a validação e a regra de
     * negócio no serviço.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<MarcaDTO>> atualizar(@PathVariable Long id, @RequestBody MarcaDTO dto) {
        MarcaDTO updated = marcaService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Marca atualizada com sucesso.", updated));
    }

    @GetMapping("/{id}")
    /**
     * Função: Recebe filtros de consulta de veiculo, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<MarcaDTO>> buscar(@PathVariable Long id) {
        MarcaDTO marca = marcaService.buscar(id);
        return ResponseEntity.ok(ApiResponse.success("Marca localizada com sucesso.", marca));
    }

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de veiculo, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<MarcaDTO>>> listar(Pageable pageable) {
        Page<MarcaDTO> marcas = marcaService.listar(pageable);
        return ResponseEntity.ok(ApiResponse.success("Marcas localizadas com sucesso.", PageResponse.from(marcas)));
    }

    @GetMapping("/pesquisar")
    /**
     * Função: Recebe filtros de consulta de veiculo, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<MarcaDTO>>> pesquisar(@RequestParam String termo, Pageable pageable) {
        Page<MarcaDTO> marcas = marcaService.pesquisar(termo, pageable);
        return ResponseEntity.ok(ApiResponse.success("Pesquisa de marcas executada com sucesso.", PageResponse.from(marcas)));
    }



    @GetMapping("/inativos")
    /**
     * Função: Atende a requisição de consulta de registros inativados e devolve os dados para a tela
     * de reativação.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<MarcaDTO>>> listarInativos(Pageable pageable) {
        Page<MarcaDTO> registros = marcaService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponse.success("Marcas inativos localizados.", PageResponse.from(registros)));
    }

    @PatchMapping("/{id}/ativar")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<MarcaDTO>> ativar(@PathVariable Long id) {
        MarcaDTO registro = marcaService.ativar(id);
        return ResponseEntity.ok(ApiResponse.success("Marca ativado.", registro));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a requisição de reativação e delega ao serviço a recuperação do cadastro
     * inativado.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> inativar(@PathVariable Long id) {
        marcaService.inativar(id);
        return ResponseEntity.ok(ApiResponse.success("Marca inativada com sucesso.", null));
    }
}
