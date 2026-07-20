package br.com.avcar.oficina.business.pessoa.validation;

import br.com.avcar.oficina.core.validation.GenericDtoValidation;

import br.com.avcar.oficina.business.pessoa.dto.FuncaoDTO;
import br.com.avcar.oficina.business.pessoa.repository.IFuncaoRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Valida as regras de entrada e unicidade do cadastro de Função.
 */
@Component
public class FuncaoValidation extends GenericDtoValidation<FuncaoDTO> {

    private final IFuncaoRepository funcaoRepository;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public FuncaoValidation(IFuncaoRepository funcaoRepository) {
        this.funcaoRepository = funcaoRepository;
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateInsert(FuncaoDTO dto) {
        validateFields(dto);
        if (funcaoRepository.existsByNomeFuncaoIgnoreCaseAndAtivoTrue(dto.getNomeFuncao().trim())) {
            throw new FieldValidationException("Já existe função ativa cadastrada com este nome.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateUpdate(Long id, FuncaoDTO dto) {
        validateId(id);
        validateFields(dto);
        if (funcaoRepository.existsByNomeFuncaoIgnoreCaseAndIdNotAndAtivoTrue(dto.getNomeFuncao().trim(), id)) {
            throw new FieldValidationException("Já existe outra função ativa cadastrada com este nome.");
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
            throw new FieldValidationException("O identificador da função é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private void validateFields(FuncaoDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados da função são obrigatórios.");
        }
        ValidationUtils.validateBusinessText(dto.getNomeFuncao(), "nome da função", true);
        ValidationUtils.maxLength(dto.getDescricao(), 255, "descrição da função");
    }
}
