package br.com.avcar.oficina.core.validation;

/**
 * Utilitário central para validação de documentos brasileiros.
 *
 * Esta classe valida CPF e CNPJ pelo cálculo oficial dos dígitos verificadores,
 * evitando aceitar documentos com tamanho correto, mas matematicamente inválidos.
 * A validação é usada no backend para garantir integridade mesmo quando a API for
 * chamada fora do Angular, por exemplo via Swagger ou Postman.
 */
public final class DocumentoValidationUtils {

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    private DocumentoValidationUtils() {
    }

    /**
     * Função: Valida os dados necessários para a operação somente digitos.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static String somenteDigitos(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static boolean cpfValido(String valor) {
        String cpf = somenteDigitos(valor);
        if (cpf == null || cpf.length() != 11 || todosDigitosIguais(cpf)) {
            return false;
        }

        int primeiroDigito = calcularDigitoCpf(cpf, 9, 10);
        int segundoDigito = calcularDigitoCpf(cpf, 10, 11);

        return Character.getNumericValue(cpf.charAt(9)) == primeiroDigito
                && Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public static boolean cnpjValido(String valor) {
        String cnpj = somenteDigitos(valor);
        if (cnpj == null || cnpj.length() != 14 || todosDigitosIguais(cnpj)) {
            return false;
        }

        int primeiroDigito = calcularDigitoCnpj(cnpj, 12, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        int segundoDigito = calcularDigitoCnpj(cnpj, 13, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

        return Character.getNumericValue(cnpj.charAt(12)) == primeiroDigito
                && Character.getNumericValue(cnpj.charAt(13)) == segundoDigito;
    }

    /**
     * Função: Valida os dados necessários para a operação calcular digito cpf.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static int calcularDigitoCpf(String cpf, int quantidadeDigitos, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < quantidadeDigitos; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (pesoInicial - i);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    /**
     * Função: Valida os dados necessários para a operação calcular digito cnpj.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static int calcularDigitoCnpj(String cnpj, int quantidadeDigitos, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < quantidadeDigitos; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    /**
     * Função: Valida os dados necessários para a operação todos digitos iguais.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private static boolean todosDigitosIguais(String valor) {
        char primeiro = valor.charAt(0);
        for (int i = 1; i < valor.length(); i++) {
            if (valor.charAt(i) != primeiro) {
                return false;
            }
        }
        return true;
    }
}
