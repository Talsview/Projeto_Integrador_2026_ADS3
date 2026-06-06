package br.com.avcar.oficina.business.ordemservico.validation;

import br.com.avcar.oficina.core.exception.RuleValidationException;
import org.springframework.stereotype.Component;

@Component
public class StatusOrdemServicoValidation {

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new RuleValidationException("ID inválido para Status da Ordem de Serviço.");
        }
    }
}
