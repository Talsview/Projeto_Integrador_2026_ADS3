package br.com.avcar.oficina.core.mapper;

import br.com.avcar.oficina.core.dto.BaseDTO;
import br.com.avcar.oficina.core.model.BaseModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

/**
 * Contrato genérico da camada Mapper.
 *
 * <p>E representa a entidade persistente do domínio e deve herdar de BaseModel.
 * D representa o DTO trafegado pela API e deve herdar de BaseDTO.</p>
 *
 * <p>Alguns mappers do sistema dependem de relações de domínio para montar a entidade,
 * como Cliente + Veículo em Ordem de Serviço ou Serviço + Colaborador em Item de Serviço.
 * Por isso, os métodos básicos possuem implementação padrão. Quando a conversão direta
 * existir, o mapper concreto sobrescreve o método; quando a conversão exigir dependências,
 * o mapper mantém métodos específicos sem perder o vínculo com a estrutura genérica.</p>
 */
public interface IGenericMapper<E extends BaseModel, D extends BaseDTO> {

    /**
     * Converte uma entidade persistente em DTO.
     * Mappers com conversão direta sobrescrevem este método.
     */
    default D toDto(E entity) {
        throw new UnsupportedOperationException("Este mapper exige método específico para converter entidade em DTO.");
    }

    /**
     * Converte um DTO em entidade persistente.
     * Mappers que dependem de outras entidades usam métodos específicos e mantêm este contrato como base.
     */
    default E toEntity(D dto) {
        throw new UnsupportedOperationException("Este mapper exige método específico para converter DTO em entidade.");
    }

    /**
     * Converte uma lista de entidades em lista de DTOs usando o método genérico toDto.
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
     * Converte uma página de entidades em página de DTOs usando o método genérico toDto.
     */
    default Page<D> toDtoPage(Page<E> page) {
        if (page == null) {
            return Page.empty();
        }

        return page.map(this::toDto);
    }
}
