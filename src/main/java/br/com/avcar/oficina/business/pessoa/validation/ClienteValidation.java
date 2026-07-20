package br.com.avcar.oficina.business.pessoa.validation;

import br.com.avcar.oficina.core.validation.GenericDtoValidation;

import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaFisicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaJuridicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClienteResumoDTO;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaFisicaRepository;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaJuridicaRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.validation.DocumentoValidationUtils;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Camada de validação do módulo Cliente.
 * Mantém regras de entrada e de unicidade fora da Controller e fora da View Angular.
 */
@Component
public class ClienteValidation extends GenericDtoValidation<ClienteResumoDTO> {

    private final IPessoaFisicaRepository pessoaFisicaRepository;
    private final IPessoaJuridicaRepository pessoaJuridicaRepository;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ClienteValidation(IPessoaFisicaRepository pessoaFisicaRepository,
                             IPessoaJuridicaRepository pessoaJuridicaRepository) {
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validatePessoaFisicaInsert(ClientePessoaFisicaDTO dto) {
        validatePessoaFisicaFields(dto);

        if (pessoaFisicaRepository.existsByCpfAndAtivoTrue(DocumentoValidationUtils.somenteDigitos(dto.getCpf()))) {
            throw new FieldValidationException("Já existe cliente pessoa física ativo com este CPF.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validatePessoaFisicaUpdate(Long id, ClientePessoaFisicaDTO dto) {
        validateId(id);
        validatePessoaFisicaFields(dto);

        if (pessoaFisicaRepository.existsByCpfAndIdNotAndAtivoTrue(DocumentoValidationUtils.somenteDigitos(dto.getCpf()), id)) {
            throw new FieldValidationException("Já existe outro cliente pessoa física ativo com este CPF.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validatePessoaJuridicaInsert(ClientePessoaJuridicaDTO dto) {
        validatePessoaJuridicaFields(dto);

        if (pessoaJuridicaRepository.existsByCnpjAndAtivoTrue(DocumentoValidationUtils.somenteDigitos(dto.getCnpj()))) {
            throw new FieldValidationException("Já existe cliente pessoa jurídica ativo com este CNPJ.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validatePessoaJuridicaUpdate(Long id, ClientePessoaJuridicaDTO dto) {
        validateId(id);
        validatePessoaJuridicaFields(dto);

        if (pessoaJuridicaRepository.existsByCnpjAndIdNotAndAtivoTrue(DocumentoValidationUtils.somenteDigitos(dto.getCnpj()), id)) {
            throw new FieldValidationException("Já existe outro cliente pessoa jurídica ativo com este CNPJ.");
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
            throw new FieldValidationException("O identificador do cliente é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private void validatePessoaFisicaFields(ClientePessoaFisicaDTO dto) {
        validateNotNull(dto);
        ValidationUtils.validatePersonName(dto.getNome(), "nome");
        ValidationUtils.validatePhone(dto.getTelefone(), false);
        ValidationUtils.validateEmail(dto.getEmail(), false);
        ValidationUtils.maxLength(dto.getEndereco(), 255, "endereço");
        ValidationUtils.maxLength(dto.getRg(), 30, "RG");
        ValidationUtils.notFuture(dto.getDataNascimento(), "data de nascimento");
        validateRequired(dto.getCpf(), "CPF");

        String cpf = DocumentoValidationUtils.somenteDigitos(dto.getCpf());
        if (cpf.length() != 11) {
            throw new FieldValidationException("O CPF deve possuir 11 dígitos.");
        }
        if (!DocumentoValidationUtils.cpfValido(cpf)) {
            throw new FieldValidationException("CPF inválido. Informe um CPF real, com dígitos verificadores válidos.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private void validatePessoaJuridicaFields(ClientePessoaJuridicaDTO dto) {
        validateNotNull(dto);
        ValidationUtils.validateBusinessText(dto.getNome(), "nome", true);
        ValidationUtils.validatePhone(dto.getTelefone(), false);
        ValidationUtils.validateEmail(dto.getEmail(), false);
        ValidationUtils.maxLength(dto.getEndereco(), 255, "endereço");
        validateRequired(dto.getCnpj(), "CNPJ");
        ValidationUtils.validateBusinessText(dto.getRazaoSocial(), "razão social", true);
        ValidationUtils.validateBusinessText(dto.getNomeFantasia(), "nome fantasia", false);
        ValidationUtils.maxLength(dto.getInscricaoEstadual(), 40, "inscrição estadual");

        String cnpj = DocumentoValidationUtils.somenteDigitos(dto.getCnpj());
        if (cnpj.length() != 14) {
            throw new FieldValidationException("O CNPJ deve possuir 14 dígitos.");
        }
        if (!DocumentoValidationUtils.cnpjValido(cnpj)) {
            throw new FieldValidationException("CNPJ inválido. Informe um CNPJ real, com dígitos verificadores válidos.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private void validateNotNull(Object dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados do cliente são obrigatórios.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private void validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new FieldValidationException("O campo " + fieldName + " é obrigatório.");
        }
    }

    /**
     * Função: Valida os dados necessários para a operação only digits.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public String onlyDigits(String value) {
        return DocumentoValidationUtils.somenteDigitos(value);
    }
}
