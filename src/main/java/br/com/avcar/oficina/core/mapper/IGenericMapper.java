package br.com.avcar.oficina.core.mapper;

import br.com.avcar.oficina.core.dto.BaseDTO;
import br.com.avcar.oficina.core.model.BaseModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public interface IGenericMapper<E extends BaseModel, D extends BaseDTO> {

    D toDto(E entity);

    E toEntity(D dto);

    default List<D> toDtoList(List<E> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    default Page<D> toDtoPage(Page<E> page) {
        if (page == null) {
            return Page.empty();
        }

        return page.map(this::toDto);
    }
}
