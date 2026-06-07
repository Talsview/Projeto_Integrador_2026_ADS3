package br.com.avcar.oficina.business.peca.validation;

import br.com.avcar.oficina.business.peca.dto.FornecedorDTO;
import br.com.avcar.oficina.business.peca.repository.IFornecedorRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.validation.DocumentoValidationUtils;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Valida regras de entrada do cadastro de Fornecedor.
 */
@Component
public class FornecedorValidation {

    private final IFornecedorRepository fornecedorRepository;

    public FornecedorValidation(IFornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public void validateInsert(FornecedorDTO dto) {
        validateFields(dto);
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && fornecedorRepository.existsActiveByCnpj(cnpj)) {
            throw new FieldValidationException("Já existe fornecedor ativo com este CNPJ.");
        }
    }

    public void validateUpdate(Long id, FornecedorDTO dto) {
        validateId(id);
        validateFields(dto);
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && fornecedorRepository.existsActiveByCnpjAndIdNot(cnpj, id)) {
            throw new FieldValidationException("Já existe outro fornecedor ativo com este CNPJ.");
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador do fornecedor é obrigatório.");
        }
    }

    private void validateFields(FornecedorDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados do fornecedor são obrigatórios.");
        }
        ValidationUtils.validateBusinessText(dto.getNomeFornecedor(), "nome do fornecedor", true);
        ValidationUtils.validatePhone(dto.getTelefone(), false);
        ValidationUtils.validateEmail(dto.getEmail(), false);
        ValidationUtils.maxLength(dto.getEndereco(), 255, "endereço");
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && !DocumentoValidationUtils.cnpjValido(cnpj)) {
            throw new FieldValidationException("CNPJ do fornecedor inválido. Informe um CNPJ real ou deixe o campo vazio.");
        }
    }

    private String onlyDigits(String value) {
        if (value == null) {
            return null;
        }
        String digits = value.replaceAll("\\D", "");
        return digits.isEmpty() ? null : digits;
    }
}
