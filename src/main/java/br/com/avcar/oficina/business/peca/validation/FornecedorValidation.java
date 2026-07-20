package br.com.avcar.oficina.business.peca.validation;

import br.com.avcar.oficina.core.validation.GenericDtoValidation;

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
public class FornecedorValidation extends GenericDtoValidation<FornecedorDTO> {

    private final IFornecedorRepository fornecedorRepository;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public FornecedorValidation(IFornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateInsert(FornecedorDTO dto) {
        validateFields(dto);
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && fornecedorRepository.existsActiveByCnpj(cnpj)) {
            throw new FieldValidationException("Já existe fornecedor ativo com este CNPJ.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateUpdate(Long id, FornecedorDTO dto) {
        validateId(id);
        validateFields(dto);
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && fornecedorRepository.existsActiveByCnpjAndIdNot(cnpj, id)) {
            throw new FieldValidationException("Já existe outro fornecedor ativo com este CNPJ.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador do fornecedor é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
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

    /**
     * Função: Valida os dados necessários para a operação only digits.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private String onlyDigits(String value) {
        if (value == null) {
            return null;
        }
        String digits = value.replaceAll("\\D", "");
        return digits.isEmpty() ? null : digits;
    }
}
