package br.com.avcar.oficina.business.servico.enums;

/**
 * Classificação obrigatória do serviço cadastrado.
 *
 * Regra de negócio: todo Serviço deve ser Interno ou Terceirizado,
 * de forma exclusiva e total, conforme o MER validado.
 */
public enum TipoServico {
    INTERNO,
    TERCEIRIZADO
}
