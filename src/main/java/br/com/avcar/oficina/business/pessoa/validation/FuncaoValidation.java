package br.com.avcar.oficina.business.pessoa.validation;

import br.com.avcar.oficina.business.pessoa.dto.FuncaoDTO;
import br.com.avcar.oficina.business.pessoa.repository.IFuncaoRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import org.springframework.stereotype.Component;

/**
 * Valida as regras de entrada e unicidade do cadastro de Função.
 */
@Component
public class FuncaoValidation {

    private final IFuncaoRepository funcaoRepository;

    public FuncaoValidation(IFuncaoRepository funcaoRepository) {
        this.funcaoRepository = funcaoRepository;
    }

    public void validateInsert(FuncaoDTO dto) {
        validateFields(dto);
        if (funcaoRepository.existsByNomeFuncaoIgnoreCaseAndAtivoTrue(dto.getNomeFuncao().trim())) {
            throw new FieldValidationException("Já existe função ativa cadastrada com este nome.");
        }
    }

    public void validateUpdate(Long id, FuncaoDTO dto) {
        validateId(id);
        validateFields(dto);
        if (funcaoRepository.existsByNomeFuncaoIgnoreCaseAndIdNotAndAtivoTrue(dto.getNomeFuncao().trim(), id)) {
            throw new FieldValidationException("Já existe outra função ativa cadastrada com este nome.");
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador da função é obrigatório.");
        }
    }

    private void validateFields(FuncaoDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados da função são obrigatórios.");
        }
        if (dto.getNomeFuncao() == null || dto.getNomeFuncao().trim().isEmpty()) {
            throw new FieldValidationException("O nome da função é obrigatório.");
        }
    }
}
