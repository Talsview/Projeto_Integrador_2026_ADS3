package br.com.avcar.oficina.business.pessoa.validation;

import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaFisicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaJuridicaDTO;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaFisicaRepository;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaJuridicaRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import org.springframework.stereotype.Component;

/**
 * Camada de validação do módulo Cliente.
 * Mantém regras de entrada e de unicidade fora da Controller e fora da View Angular.
 */
@Component
public class ClienteValidation {

    private final IPessoaFisicaRepository pessoaFisicaRepository;
    private final IPessoaJuridicaRepository pessoaJuridicaRepository;

    public ClienteValidation(IPessoaFisicaRepository pessoaFisicaRepository,
                             IPessoaJuridicaRepository pessoaJuridicaRepository) {
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
    }

    public void validatePessoaFisicaInsert(ClientePessoaFisicaDTO dto) {
        validatePessoaFisicaFields(dto);

        if (pessoaFisicaRepository.existsByCpfAndAtivoTrue(onlyDigits(dto.getCpf()))) {
            throw new FieldValidationException("Já existe cliente pessoa física ativo com este CPF.");
        }
    }

    public void validatePessoaFisicaUpdate(Long id, ClientePessoaFisicaDTO dto) {
        validateId(id);
        validatePessoaFisicaFields(dto);

        if (pessoaFisicaRepository.existsByCpfAndIdNotAndAtivoTrue(onlyDigits(dto.getCpf()), id)) {
            throw new FieldValidationException("Já existe outro cliente pessoa física ativo com este CPF.");
        }
    }

    public void validatePessoaJuridicaInsert(ClientePessoaJuridicaDTO dto) {
        validatePessoaJuridicaFields(dto);

        if (pessoaJuridicaRepository.existsByCnpjAndAtivoTrue(onlyDigits(dto.getCnpj()))) {
            throw new FieldValidationException("Já existe cliente pessoa jurídica ativo com este CNPJ.");
        }
    }

    public void validatePessoaJuridicaUpdate(Long id, ClientePessoaJuridicaDTO dto) {
        validateId(id);
        validatePessoaJuridicaFields(dto);

        if (pessoaJuridicaRepository.existsByCnpjAndIdNotAndAtivoTrue(onlyDigits(dto.getCnpj()), id)) {
            throw new FieldValidationException("Já existe outro cliente pessoa jurídica ativo com este CNPJ.");
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador do cliente é obrigatório.");
        }
    }

    private void validatePessoaFisicaFields(ClientePessoaFisicaDTO dto) {
        validateNotNull(dto);
        validateRequired(dto.getNome(), "nome");
        validateRequired(dto.getCpf(), "CPF");

        String cpf = onlyDigits(dto.getCpf());
        if (cpf.length() != 11) {
            throw new FieldValidationException("O CPF deve possuir 11 dígitos.");
        }
    }

    private void validatePessoaJuridicaFields(ClientePessoaJuridicaDTO dto) {
        validateNotNull(dto);
        validateRequired(dto.getNome(), "nome");
        validateRequired(dto.getCnpj(), "CNPJ");
        validateRequired(dto.getRazaoSocial(), "razão social");

        String cnpj = onlyDigits(dto.getCnpj());
        if (cnpj.length() != 14) {
            throw new FieldValidationException("O CNPJ deve possuir 14 dígitos.");
        }
    }

    private void validateNotNull(Object dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados do cliente são obrigatórios.");
        }
    }

    private void validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new FieldValidationException("O campo " + fieldName + " é obrigatório.");
        }
    }

    public String onlyDigits(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("\\D", "");
    }
}
