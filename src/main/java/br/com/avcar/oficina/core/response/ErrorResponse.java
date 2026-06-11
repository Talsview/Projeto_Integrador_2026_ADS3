package br.com.avcar.oficina.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String title;
    private final String motive;
    private final String severity;
    private final List<String> details = new ArrayList<>();

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ErrorResponse(String title, String motive, String severity) {
        this.title = title;
        this.motive = motive;
        this.severity = severity;
    }

    /**
     * Função: Adiciona uma informação complementar à resposta retornada pela API.
     * Uso no sistema: permite explicar validações ou detalhes sem mudar a estrutura principal da
     * resposta.
     */
    public void addDetail(String detail) {
        if (detail != null && !detail.isBlank()) {
            details.add(detail);
        }
    }
}
