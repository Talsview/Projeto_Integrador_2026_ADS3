package br.com.avcar.oficina.business.servico.validation;

import br.com.avcar.oficina.business.servico.dto.EmpresaTerceirizadaDTO;
import br.com.avcar.oficina.business.servico.repository.IEmpresaTerceirizadaRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import org.springframework.stereotype.Component;

/**
 * Valida regras de entrada do cadastro de Empresa Terceirizada.
 */
@Component
public class EmpresaTerceirizadaValidation {

    private final IEmpresaTerceirizadaRepository empresaRepository;

    public EmpresaTerceirizadaValidation(IEmpresaTerceirizadaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public void validateInsert(EmpresaTerceirizadaDTO dto) {
        validateFields(dto);
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && empresaRepository.existsActiveByCnpj(cnpj)) {
            throw new FieldValidationException("Já existe empresa terceirizada ativa com este CNPJ.");
        }
    }

    public void validateUpdate(Long id, EmpresaTerceirizadaDTO dto) {
        validateId(id);
        validateFields(dto);
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && empresaRepository.existsActiveByCnpjAndIdNot(cnpj, id)) {
            throw new FieldValidationException("Já existe outra empresa terceirizada ativa com este CNPJ.");
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador da empresa terceirizada é obrigatório.");
        }
    }

    private void validateFields(EmpresaTerceirizadaDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados da empresa terceirizada são obrigatórios.");
        }
        if (dto.getNomeEmpresa() == null || dto.getNomeEmpresa().trim().isEmpty()) {
            throw new FieldValidationException("O nome da empresa terceirizada é obrigatório.");
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
