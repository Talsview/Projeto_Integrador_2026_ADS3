package br.com.avcar.oficina.core.controller;

import br.com.avcar.oficina.core.dto.BaseDTO;
import br.com.avcar.oficina.core.mapper.IGenericMapper;
import br.com.avcar.oficina.core.model.BaseModel;
import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.PageResponse;
import br.com.avcar.oficina.core.service.IGenericService;
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

/**
 * Controller genérico para endpoints REST.
 *
 * E = Entity/Model
 * D = DTO
 * S = Service específico
 * M = Mapper específico
 */
public abstract class GenericController<
        E extends BaseModel,
        D extends BaseDTO,
        S extends IGenericService<E>,
        M extends IGenericMapper<E, D>> {

    protected final S service;
    protected final M mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    protected GenericController(S service, M mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    /**
     * Função: Atende a rota HTTP responsável por find by id e repassa a regra ao serviço
     * correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<D>> findById(@PathVariable Long id) {
        E entity = service.findByIdActive(id);
        D dto = mapper.toDto(entity);
        return ResponseEntity.ok(ApiResponse.success("Registro localizado com sucesso.", dto));
    }

    @GetMapping
    /**
     * Função: Atende a rota HTTP responsável por find all e repassa a regra ao serviço correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<PageResponse<D>>> findAll(Pageable pageable) {
        Page<E> entities = service.findAllActive(pageable);
        Page<D> dtos = mapper.toDtoPage(entities);
        return ResponseEntity.ok(ApiResponse.success("Registros localizados com sucesso.", PageResponse.from(dtos)));
    }

    @PostMapping
    /**
     * Função: Atende a rota HTTP responsável por insert e repassa a regra ao serviço correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<D>> insert(@RequestBody D dto) {
        E entity = mapper.toEntity(dto);
        E saved = service.insert(entity);
        D responseDto = mapper.toDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Registro cadastrado com sucesso.", responseDto));
    }

    @PutMapping("/{id}")
    /**
     * Função: Atende a rota HTTP responsável por update e repassa a regra ao serviço correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<D>> update(@PathVariable Long id, @RequestBody D dto) {
        E entity = mapper.toEntity(dto);
        entity.setId(id);
        E updated = service.update(entity);
        return ResponseEntity.ok(ApiResponse.success("Registro atualizado com sucesso.", mapper.toDto(updated)));
    }

    @DeleteMapping("/{id}")
    /**
     * Função: Atende a rota HTTP responsável por delete e repassa a regra ao serviço correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Registro inativado com sucesso.", null));
    }
}
