package br.com.avcar.oficina.core.validation;

import br.com.avcar.oficina.core.exception.FieldValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.regex.Pattern;

/**
 * Utilitário central para validações de campos e regras simples.
 *
 * A classe concentra validações comuns para evitar duplicação entre os módulos
 * e padronizar as mensagens exibidas no Angular e no Swagger.
 */
public final class ValidationUtils {

    /**
     * Função: Valida os dados necessários para a operação pattern.compile.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    /**
     * Função: Valida os dados necessários para a operação pattern.compile.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("^[0-9()\\s-]+$");
    /**
     * Função: Valida os dados necessários para a operação pattern.compile.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static final Pattern TEXTO_NOME_PATTERN = Pattern.compile("^[\\p{L}0-9 .,'ºª&/-]+$");
    /**
     * Função: Valida os dados necessários para a operação pattern.compile.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static final Pattern TEXTO_PESSOA_PATTERN = Pattern.compile("^[\\p{L} .,'ºª-]+$");
    /**
     * Função: Valida os dados necessários para a operação pattern.compile.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static final Pattern PLACA_ANTIGA_PATTERN = Pattern.compile("^[A-Z]{3}[0-9]{4}$");
    /**
     * Função: Valida os dados necessários para a operação pattern.compile.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static final Pattern PLACA_MERCOSUL_PATTERN = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");
    /**
     * Função: Valida os dados necessários para a operação pattern.compile.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static final Pattern CHASSI_PATTERN = Pattern.compile("^[A-HJ-NPR-Z0-9]{17}$");

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    private ValidationUtils() {
    }

    /**
     * Função: Valida os dados necessários para a operação trim to null.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * Função: Valida os dados necessários para a operação require text.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void requireText(String value, String fieldName) {
        if (trimToNull(value) == null) {
            throw new FieldValidationException("O campo " + fieldName + " é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void requireId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O campo " + fieldName + " é obrigatório e deve possuir identificador válido.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação max length.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void maxLength(String value, int max, String fieldName) {
        String trimmed = trimToNull(value);
        if (trimmed != null && trimmed.length() > max) {
            throw new FieldValidationException("O campo " + fieldName + " deve possuir no máximo " + max + " caracteres.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void validatePersonName(String value, String fieldName) {
        requireText(value, fieldName);
        String trimmed = value.trim();
        if (trimmed.length() < 3) {
            throw new FieldValidationException("O campo " + fieldName + " deve possuir pelo menos 3 caracteres.");
        }
        if (trimmed.matches(".*\\d.*")) {
            throw new FieldValidationException("O campo " + fieldName + " não deve conter números.");
        }
        if (!TEXTO_PESSOA_PATTERN.matcher(trimmed).matches()) {
            throw new FieldValidationException("O campo " + fieldName + " contém caracteres inválidos.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void validateBusinessText(String value, String fieldName, boolean required) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            if (required) {
                throw new FieldValidationException("O campo " + fieldName + " é obrigatório.");
            }
            return;
        }
        if (trimmed.length() < 2) {
            throw new FieldValidationException("O campo " + fieldName + " deve possuir pelo menos 2 caracteres.");
        }
        if (trimmed.matches("\\d+")) {
            throw new FieldValidationException("O campo " + fieldName + " não pode conter apenas números.");
        }
        if (!TEXTO_NOME_PATTERN.matcher(trimmed).matches()) {
            throw new FieldValidationException("O campo " + fieldName + " contém caracteres inválidos.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void validateEmail(String email, boolean required) {
        String value = trimToNull(email);
        if (value == null) {
            if (required) {
                throw new FieldValidationException("O campo e-mail é obrigatório.");
            }
            return;
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new FieldValidationException("Informe um e-mail válido.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void validatePhone(String phone, boolean required) {
        String value = trimToNull(phone);
        String digits = DocumentoValidationUtils.somenteDigitos(phone);
        if (value == null) {
            if (required) {
                throw new FieldValidationException("O campo telefone é obrigatório.");
            }
            return;
        }
        if (!TELEFONE_PATTERN.matcher(value).matches()) {
            throw new FieldValidationException("O telefone deve conter apenas números, espaços, parênteses e hífen.");
        }
        if (digits == null || digits.isEmpty()) {
            throw new FieldValidationException("Informe um telefone válido com DDD.");
        }
        if (digits.length() < 10 || digits.length() > 11) {
            throw new FieldValidationException("Informe um telefone válido com DDD.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação not future.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void notFuture(LocalDate date, String fieldName) {
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new FieldValidationException("O campo " + fieldName + " não pode ser uma data futura.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação not future.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void notFuture(LocalDateTime date, String fieldName) {
        if (date != null && date.isAfter(LocalDateTime.now())) {
            throw new FieldValidationException("O campo " + fieldName + " não pode ser uma data futura.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação date not before.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void dateNotBefore(LocalDateTime date, LocalDateTime minDate, String fieldName, String minFieldName) {
        if (date != null && minDate != null && date.isBefore(minDate)) {
            throw new FieldValidationException("O campo " + fieldName + " não pode ser anterior ao campo " + minFieldName + ".");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação date not before.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void dateNotBefore(LocalDate date, LocalDate minDate, String fieldName, String minFieldName) {
        if (date != null && minDate != null && date.isBefore(minDate)) {
            throw new FieldValidationException("O campo " + fieldName + " não pode ser anterior ao campo " + minFieldName + ".");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void validateYear(Integer year, String fieldName, boolean required) {
        if (year == null) {
            if (required) {
                throw new FieldValidationException("O campo " + fieldName + " é obrigatório.");
            }
            return;
        }
        int currentLimit = Year.now().getValue() + 1;
        if (year < 1900 || year > currentLimit) {
            throw new FieldValidationException("O campo " + fieldName + " deve estar entre 1900 e " + currentLimit + ".");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void validateModelYear(Integer manufacturingYear, Integer modelYear) {
        validateYear(modelYear, "ano do modelo", true);
        if (manufacturingYear != null && modelYear != null && modelYear < manufacturingYear - 1) {
            throw new FieldValidationException("O ano do modelo não pode ser muito anterior ao ano de fabricação.");
        }
        if (manufacturingYear != null && modelYear != null && modelYear > manufacturingYear + 1) {
            throw new FieldValidationException("O ano do modelo não pode ser superior a um ano após a fabricação.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação normalize placa.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static String normalizePlaca(String placa) {
        String value = trimToNull(placa);
        return value == null ? null : value.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void validatePlaca(String placa) {
        String normalized = normalizePlaca(placa);
        if (normalized == null) {
            throw new FieldValidationException("A placa do veículo é obrigatória.");
        }
        if (!PLACA_ANTIGA_PATTERN.matcher(normalized).matches()
                && !PLACA_MERCOSUL_PATTERN.matcher(normalized).matches()) {
            throw new FieldValidationException("A placa deve estar no formato antigo ABC1234 ou Mercosul ABC1D23.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação normalize chassi.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static String normalizeChassi(String chassi) {
        String value = trimToNull(chassi);
        return value == null ? null : value.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void validateChassi(String chassi) {
        String normalized = normalizeChassi(chassi);
        if (normalized != null && !CHASSI_PATTERN.matcher(normalized).matches()) {
            throw new FieldValidationException("O chassi deve possuir 17 caracteres alfanuméricos válidos, sem as letras I, O e Q.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação positive.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void positive(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new FieldValidationException("O campo " + fieldName + " deve ser maior que zero.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação non negative.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void nonNegative(BigDecimal value, String fieldName) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new FieldValidationException("O campo " + fieldName + " não pode ser negativo.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação non negative.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static void nonNegative(Integer value, String fieldName) {
        if (value != null && value < 0) {
            throw new FieldValidationException("O campo " + fieldName + " não pode ser negativo.");
        }
    }
}
