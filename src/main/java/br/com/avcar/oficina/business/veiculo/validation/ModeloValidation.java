package br.com.avcar.oficina.business.veiculo.validation;

import br.com.avcar.oficina.business.veiculo.dto.ModeloDTO;
import br.com.avcar.oficina.business.veiculo.repository.IMarcaRepository;
import br.com.avcar.oficina.business.veiculo.repository.IModeloRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Valida as regras de entrada e unicidade do cadastro de Modelo.
 */
@Component
public class ModeloValidation {

    private final IMarcaRepository marcaRepository;
    private final IModeloRepository modeloRepository;

    public ModeloValidation(IMarcaRepository marcaRepository, IModeloRepository modeloRepository) {
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
    }

    public void validateInsert(ModeloDTO dto) {
        validateFields(dto);
        validateMarca(dto.getMarcaId());
        if (modeloRepository.existsActiveByMarcaAndNome(dto.getMarcaId(), dto.getNomeModelo().trim())) {
            throw new FieldValidationException("Já existe modelo ativo com este nome para a marca informada.");
        }
    }

    public void validateUpdate(Long id, ModeloDTO dto) {
        validateId(id);
        validateFields(dto);
        validateMarca(dto.getMarcaId());
        if (modeloRepository.existsActiveByMarcaAndNomeAndIdNot(dto.getMarcaId(), dto.getNomeModelo().trim(), id)) {
            throw new FieldValidationException("Já existe outro modelo ativo com este nome para a marca informada.");
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador do modelo é obrigatório.");
        }
    }

    public void validateMarca(Long marcaId) {
        if (marcaId == null || marcaId <= 0) {
            throw new FieldValidationException("A marca do modelo é obrigatória.");
        }
        if (marcaRepository.findByIdAndAtivoTrue(marcaId).isEmpty()) {
            throw new FieldValidationException("A marca informada não foi localizada ou está inativa.");
        }
    }

    private void validateFields(ModeloDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados do modelo são obrigatórios.");
        }
        ValidationUtils.validateBusinessText(dto.getNomeModelo(), "nome do modelo", true);
    }
}
