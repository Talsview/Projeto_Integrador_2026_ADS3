package br.com.avcar.oficina.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final int status;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    private ApiResponse(boolean success, int status, String message, T data) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Função: Monta uma resposta padronizada de sucesso.
     * Uso no sistema: mantém os retornos positivos da API consistentes entre os módulos.
     */
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(true, status, message, data);
    }

    /**
     * Função: Monta uma resposta padronizada de sucesso.
     * Uso no sistema: mantém os retornos positivos da API consistentes entre os módulos.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return success(200, message, data);
    }

    /**
     * Função: Monta uma resposta padronizada de erro.
     * Uso no sistema: mantém falhas da API em um formato previsível para o frontend.
     */
    public static <T> ApiResponse<T> error(int status, String message, T data) {
        return new ApiResponse<>(false, status, message, data);
    }
}
