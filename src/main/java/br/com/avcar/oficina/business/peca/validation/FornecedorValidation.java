package br.com.avcar.oficina.business.peca.validation;

import br.com.avcar.oficina.business.peca.dto.FornecedorDTO;
import br.com.avcar.oficina.business.peca.repository.IFornecedorRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
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
        if (dto.getNomeFornecedor() == null || dto.getNomeFornecedor().trim().isEmpty()) {
            throw new FieldValidationException("O nome do fornecedor é obrigatório.");
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
