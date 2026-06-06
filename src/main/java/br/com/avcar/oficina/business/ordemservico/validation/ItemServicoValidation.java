package br.com.avcar.oficina.business.ordemservico.validation;

import br.com.avcar.oficina.business.ordemservico.dto.ItemServicoDTO;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Validações de domínio do ItemServico.
 */
@Component
public class ItemServicoValidation {

    public void validateInsert(ItemServicoDTO dto) {
        validateDto(dto);
    }

    public void validateUpdate(Long id, ItemServicoDTO dto) {
        validateId(id);
        validateDto(dto);
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new RuleValidationException("ID inválido para Item de Serviço.");
        }
    }

    public void validateIdOrdemServico(Long idOrdemServico) {
        if (idOrdemServico == null || idOrdemServico <= 0) {
            throw new RuleValidationException("ID da Ordem de Serviço inválido para consulta de itens de serviço.");
        }
    }

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
        if (dto.getQuantidade() != null && dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuleValidationException("A quantidade do Item de Serviço deve ser maior que zero.");
        }
        if (dto.getValorUnitario() != null && dto.getValorUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuleValidationException("O valor unitário do Item de Serviço não pode ser negativo.");
        }
    }
}
