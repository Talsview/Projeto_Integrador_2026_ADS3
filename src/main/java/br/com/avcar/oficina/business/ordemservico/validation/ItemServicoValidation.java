package br.com.avcar.oficina.business.ordemservico.validation;

import br.com.avcar.oficina.business.ordemservico.dto.ItemServicoDTO;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Validações de domínio do ItemServico.
 */
@Component
public class ItemServicoValidation {

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateInsert(ItemServicoDTO dto) {
        validateDto(dto);
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateUpdate(Long id, ItemServicoDTO dto) {
        validateId(id);
        validateDto(dto);
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new RuleValidationException("ID inválido para Item de Serviço.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateIdOrdemServico(Long idOrdemServico) {
        if (idOrdemServico == null || idOrdemServico <= 0) {
            throw new RuleValidationException("ID da Ordem de Serviço inválido para consulta de itens de serviço.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private void validateDto(ItemServicoDTO dto) {
        if (dto == null) {
            throw new RuleValidationException("Os dados do Item de Serviço são obrigatórios.");
        }
        if (dto.getIdOrdemServico() == null || dto.getIdOrdemServico() <= 0) {
            throw new RuleValidationException("A Ordem de Serviço do item é obrigatória.");
        }
        if (dto.getIdServico() == null || dto.getIdServico() <= 0) {
            throw new RuleValidationException("O Serviço cadastrado é obrigatório para o item.");
        }
        if (dto.getIdColaborador() == null || dto.getIdColaborador() <= 0) {
            throw new RuleValidationException("Todo Item de Serviço deve possuir colaborador responsável.");
        }
        if (dto.getQuantidade() == null || dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuleValidationException("A quantidade do Item de Serviço deve ser maior que zero.");
        }
        if (dto.getValorUnitario() == null || dto.getValorUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuleValidationException("O valor unitário do Item de Serviço não pode ser negativo.");
        }
        if (dto.getValorTotal() != null && dto.getValorTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuleValidationException("O valor total do Item de Serviço não pode ser negativo.");
        }
        ValidationUtils.notFuture(dto.getDataInicio(), "data de início do serviço");
        ValidationUtils.dateNotBefore(dto.getDataFim(), dto.getDataInicio(), "data de fim do serviço", "data de início do serviço");
        ValidationUtils.maxLength(dto.getDescricaoExecucao(), 2000, "descrição da execução");
        ValidationUtils.maxLength(dto.getObservacaoTerceirizacao(), 2000, "observação da terceirização");
        if (dto.getDataEnvioTerceirizacao() != null) {
            ValidationUtils.notFuture(dto.getDataEnvioTerceirizacao(), "data de envio da terceirização");
        }
        if (dto.getDataRetornoTerceirizacao() != null) {
            ValidationUtils.notFuture(dto.getDataRetornoTerceirizacao(), "data de retorno da terceirização");
        }
        ValidationUtils.dateNotBefore(dto.getDataRetornoTerceirizacao(), dto.getDataEnvioTerceirizacao(), "data de retorno da terceirização", "data de envio da terceirização");
        if (dto.getValorCobradoTerceirizacao() != null && dto.getValorCobradoTerceirizacao().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuleValidationException("O valor cobrado pela terceirização não pode ser negativo.");
        }
    }
}
