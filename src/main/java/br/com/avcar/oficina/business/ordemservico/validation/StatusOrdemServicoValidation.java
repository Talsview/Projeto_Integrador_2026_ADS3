package br.com.avcar.oficina.business.ordemservico.validation;

import br.com.avcar.oficina.core.exception.RuleValidationException;
import org.springframework.stereotype.Component;

@Component
public class StatusOrdemServicoValidation {

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new RuleValidationException("ID inválido para Status da Ordem de Serviço.");
        }
    }
}
