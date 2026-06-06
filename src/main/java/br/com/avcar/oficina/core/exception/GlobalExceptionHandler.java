package br.com.avcar.oficina.core.exception;

import br.com.avcar.oficina.core.response.ApiResponse;
import br.com.avcar.oficina.core.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratador global de exceções da API REST.
 *
 * Padroniza as respostas de erro para que o Angular receba sempre o mesmo
 * contrato de resposta, independentemente do módulo acessado.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBaseException(BaseException exception) {
        HttpStatus status = exception instanceof FieldValidationException
                ? HttpStatus.BAD_REQUEST
                : HttpStatus.UNPROCESSABLE_ENTITY;

        ErrorResponse error = new ErrorResponse(
                exception.getTitle(),
                exception.getMotive(),
                exception.getSeverity().name()
        );
        error.addDetail(exception.getMessage());

        return ResponseEntity.status(status)
                .body(ApiResponse.error(status.value(), exception.getTitle(), error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        ErrorResponse error = new ErrorResponse(
                "Erro de Validação",
                "METHOD_ARGUMENT_NOT_VALID",
                Severity.WARNING.name()
        );

        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> error.addDetail(fieldError.getField() + ": " + fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Erro de Validação", error));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        ErrorResponse error = new ErrorResponse(
                "Requisição Inválida",
                "HTTP_MESSAGE_NOT_READABLE",
                Severity.WARNING.name()
        );
        error.addDetail("O corpo da requisição está vazio ou possui formato inválido.");

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Requisição Inválida", error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUnexpectedException(Exception exception) {
        ErrorResponse error = new ErrorResponse(
                "Erro Interno",
                "INTERNAL_SERVER_ERROR",
                Severity.FATAL.name()
        );
        error.addDetail("Ocorreu um erro inesperado. Verifique os logs da aplicação.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro Interno", error));
    }
}
