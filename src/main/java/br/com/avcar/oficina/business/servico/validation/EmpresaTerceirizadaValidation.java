package br.com.avcar.oficina.business.servico.validation;

import br.com.avcar.oficina.core.validation.GenericDtoValidation;

import br.com.avcar.oficina.business.servico.dto.EmpresaTerceirizadaDTO;
import br.com.avcar.oficina.business.servico.repository.IEmpresaTerceirizadaRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.validation.DocumentoValidationUtils;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Valida regras de entrada do cadastro de Empresa Terceirizada.
 */
@Component
public class EmpresaTerceirizadaValidation extends GenericDtoValidation<EmpresaTerceirizadaDTO> {

    private final IEmpresaTerceirizadaRepository empresaRepository;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public EmpresaTerceirizadaValidation(IEmpresaTerceirizadaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateInsert(EmpresaTerceirizadaDTO dto) {
        validateFields(dto);
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && empresaRepository.existsActiveByCnpj(cnpj)) {
            throw new FieldValidationException("Já existe empresa terceirizada ativa com este CNPJ.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateUpdate(Long id, EmpresaTerceirizadaDTO dto) {
        validateId(id);
        validateFields(dto);
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && empresaRepository.existsActiveByCnpjAndIdNot(cnpj, id)) {
            throw new FieldValidationException("Já existe outra empresa terceirizada ativa com este CNPJ.");
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
            throw new FieldValidationException("O identificador da empresa terceirizada é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private void validateFields(EmpresaTerceirizadaDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados da empresa terceirizada são obrigatórios.");
        }
        ValidationUtils.validateBusinessText(dto.getNomeEmpresa(), "nome da empresa terceirizada", true);
        ValidationUtils.validatePhone(dto.getTelefone(), false);
        ValidationUtils.validateEmail(dto.getEmail(), false);
        ValidationUtils.maxLength(dto.getEndereco(), 255, "endereço");
        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj != null && !DocumentoValidationUtils.cnpjValido(cnpj)) {
            throw new FieldValidationException("CNPJ da empresa terceirizada inválido. Informe um CNPJ real ou deixe o campo vazio.");
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
