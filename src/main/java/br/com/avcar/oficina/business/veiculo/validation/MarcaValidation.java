package br.com.avcar.oficina.business.veiculo.validation;

import br.com.avcar.oficina.business.veiculo.dto.MarcaDTO;
import br.com.avcar.oficina.business.veiculo.repository.IMarcaRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Valida as regras de entrada e unicidade do cadastro de Marca.
 */
@Component
public class MarcaValidation {

    private final IMarcaRepository marcaRepository;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public MarcaValidation(IMarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateInsert(MarcaDTO dto) {
        validateFields(dto);
        if (marcaRepository.existsByNomeMarcaIgnoreCaseAndAtivoTrue(dto.getNomeMarca().trim())) {
            throw new FieldValidationException("Já existe marca ativa cadastrada com este nome.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateUpdate(Long id, MarcaDTO dto) {
        validateId(id);
        validateFields(dto);
        if (marcaRepository.existsByNomeMarcaIgnoreCaseAndIdNotAndAtivoTrue(dto.getNomeMarca().trim(), id)) {
            throw new FieldValidationException("Já existe outra marca ativa cadastrada com este nome.");
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
            throw new FieldValidationException("O identificador da marca é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private void validateFields(MarcaDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados da marca são obrigatórios.");
        }
        ValidationUtils.validateBusinessText(dto.getNomeMarca(), "nome da marca", true);
    }
}
