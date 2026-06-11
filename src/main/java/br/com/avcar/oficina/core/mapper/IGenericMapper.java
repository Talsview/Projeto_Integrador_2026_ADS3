package br.com.avcar.oficina.core.mapper;

import br.com.avcar.oficina.core.dto.BaseDTO;
import br.com.avcar.oficina.core.model.BaseModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public interface IGenericMapper<E extends BaseModel, D extends BaseDTO> {

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    D toDto(E entity);

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    E toEntity(D dto);

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    default List<D> toDtoList(List<E> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    default Page<D> toDtoPage(Page<E> page) {
        if (page == null) {
            return Page.empty();
        }

        return page.map(this::toDto);
    }
}
