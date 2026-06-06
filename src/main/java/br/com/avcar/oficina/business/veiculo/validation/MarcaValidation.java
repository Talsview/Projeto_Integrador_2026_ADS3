package br.com.avcar.oficina.business.veiculo.validation;

import br.com.avcar.oficina.business.veiculo.dto.MarcaDTO;
import br.com.avcar.oficina.business.veiculo.repository.IMarcaRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import org.springframework.stereotype.Component;

/**
 * Valida as regras de entrada e unicidade do cadastro de Marca.
 */
@Component
public class MarcaValidation {

    private final IMarcaRepository marcaRepository;

    public MarcaValidation(IMarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public void validateInsert(MarcaDTO dto) {
        validateFields(dto);
        if (marcaRepository.existsByNomeMarcaIgnoreCaseAndAtivoTrue(dto.getNomeMarca().trim())) {
            throw new FieldValidationException("Já existe marca ativa cadastrada com este nome.");
        }
    }

    public void validateUpdate(Long id, MarcaDTO dto) {
        validateId(id);
        validateFields(dto);
        if (marcaRepository.existsByNomeMarcaIgnoreCaseAndIdNotAndAtivoTrue(dto.getNomeMarca().trim(), id)) {
            throw new FieldValidationException("Já existe outra marca ativa cadastrada com este nome.");
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador da marca é obrigatório.");
        }
    }

    private void validateFields(MarcaDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados da marca são obrigatórios.");
        }
        if (dto.getNomeMarca() == null || dto.getNomeMarca().trim().isEmpty()) {
            throw new FieldValidationException("O nome da marca é obrigatório.");
        }
    }
}
