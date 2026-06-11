package br.com.avcar.oficina.business.peca.validation;

import br.com.avcar.oficina.business.peca.dto.PecaDTO;
import br.com.avcar.oficina.business.peca.repository.IPecaRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Valida regras de entrada do cadastro de Peça.
 */
@Component
public class PecaValidation {

    private final IPecaRepository pecaRepository;

    public PecaValidation(IPecaRepository pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    public void validateInsert(PecaDTO dto) {
        validateFields(dto);
        String codigoNacional = normalizeUpper(dto.getCodigoNacional());
        if (codigoNacional != null && pecaRepository.existsActiveByCodigoNacional(codigoNacional)) {
            throw new FieldValidationException("Já existe peça ativa com este código nacional.");
        }
    }

    public void validateUpdate(Long id, PecaDTO dto) {
        validateId(id);
        validateFields(dto);
        String codigoNacional = normalizeUpper(dto.getCodigoNacional());
        if (codigoNacional != null && pecaRepository.existsActiveByCodigoNacionalAndIdNot(codigoNacional, id)) {
            throw new FieldValidationException("Já existe outra peça ativa com este código nacional.");
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador da peça é obrigatório.");
        }
    }

    private void validateFields(PecaDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados da peça são obrigatórios.");
        }
        ValidationUtils.validateBusinessText(dto.getNomePeca(), "nome da peça", true);
        ValidationUtils.validateBusinessText(dto.getMarcaPeca(), "marca da peça", false);
        ValidationUtils.validateBusinessText(dto.getModeloAplicavel(), "modelo aplicável", false);
        ValidationUtils.maxLength(dto.getCodigoNacional(), 60, "código nacional");
        ValidationUtils.validateYear(dto.getAnoVeiculo(), "ano do veículo", false);
        ValidationUtils.validateYear(dto.getAnoModelo(), "ano do modelo", false);
        if (dto.getAnoVeiculo() != null && dto.getAnoModelo() != null) {
            ValidationUtils.validateModelYear(dto.getAnoVeiculo(), dto.getAnoModelo());
        }
        if (dto.getIdFornecedorPadrao() != null && dto.getIdFornecedorPadrao() <= 0) {
            throw new FieldValidationException("O fornecedor padrão da peça deve ser válido.");
        }
        if (dto.getValorUnitarioPadrao() != null && dto.getValorUnitarioPadrao().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new FieldValidationException("O valor unitário padrão da peça não pode ser negativo.");
        }
        if (dto.getPrazoGarantiaDias() != null && dto.getPrazoGarantiaDias() < 0) {
            throw new FieldValidationException("O prazo de garantia da peça deve ser maior ou igual a zero.");
        }
        ValidationUtils.maxLength(dto.getDescricao(), 2000, "descrição da peça");
    }

    private void validateAno(Integer ano, String campo) {
        if (ano != null && ano < 1900) {
            throw new FieldValidationException("O " + campo + " da peça deve ser maior ou igual a 1900.");
        }
    }

    private String normalizeUpper(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed.toUpperCase();
    }
}
