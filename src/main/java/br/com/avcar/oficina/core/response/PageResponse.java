package br.com.avcar.oficina.core.response;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    private PageResponse(Page<T> source) {
        this.content = source.getContent();
        this.page = source.getNumber();
        this.size = source.getSize();
        this.totalElements = source.getTotalElements();
        this.totalPages = source.getTotalPages();
        this.first = source.isFirst();
        this.last = source.isLast();
    }

    /**
     * Função: Cria uma resposta ou DTO a partir de outro objeto já processado.
     * Uso no sistema: reduz repetição na montagem de retornos entre as camadas.
     */
    public static <T> PageResponse<T> from(Page<T> source) {
        return new PageResponse<>(source);
    }
}
