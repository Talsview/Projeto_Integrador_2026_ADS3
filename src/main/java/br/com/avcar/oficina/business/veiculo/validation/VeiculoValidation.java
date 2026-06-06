package br.com.avcar.oficina.business.veiculo.validation;

import br.com.avcar.oficina.business.pessoa.repository.IClienteRepository;
import br.com.avcar.oficina.business.veiculo.dto.TransferenciaProprietarioDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoDTO;
import br.com.avcar.oficina.business.veiculo.mapper.VeiculoMapper;
import br.com.avcar.oficina.business.veiculo.model.HistoricoProprietarioModel;
import br.com.avcar.oficina.business.veiculo.repository.IModeloRepository;
import br.com.avcar.oficina.business.veiculo.repository.IVeiculoRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * Valida as regras de entrada do cadastro de Veículo e transferência de proprietário.
 */
@Component
public class VeiculoValidation {

    private final IModeloRepository modeloRepository;
    private final IClienteRepository clienteRepository;
    private final IVeiculoRepository veiculoRepository;
    private final VeiculoMapper veiculoMapper;

    public VeiculoValidation(IModeloRepository modeloRepository,
                             IClienteRepository clienteRepository,
                             IVeiculoRepository veiculoRepository,
                             VeiculoMapper veiculoMapper) {
        this.modeloRepository = modeloRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = veiculoMapper;
    }

    public void validateInsert(VeiculoDTO dto) {
        validateFields(dto, true);
        validateModelo(dto.getModeloId());
        validateCliente(dto.getProprietarioAtualId(), "O proprietário atual do veículo é obrigatório.");

        String placa = veiculoMapper.normalizePlaca(dto.getPlaca());
        if (veiculoRepository.existsByPlacaAndAtivoTrue(placa)) {
            throw new FieldValidationException("Já existe veículo ativo cadastrado com esta placa.");
        }

        String chassi = veiculoMapper.normalizeNullable(dto.getChassi());
        if (chassi != null && veiculoRepository.existsByChassiAndAtivoTrue(chassi)) {
            throw new FieldValidationException("Já existe veículo ativo cadastrado com este chassi.");
        }
    }

    public void validateUpdate(Long id, VeiculoDTO dto) {
        validateId(id);
        validateFields(dto, false);
        validateModelo(dto.getModeloId());

        String placa = veiculoMapper.normalizePlaca(dto.getPlaca());
        if (veiculoRepository.existsByPlacaAndIdNotAndAtivoTrue(placa, id)) {
            throw new FieldValidationException("Já existe outro veículo ativo cadastrado com esta placa.");
        }

        String chassi = veiculoMapper.normalizeNullable(dto.getChassi());
        if (chassi != null && veiculoRepository.existsByChassiAndIdNotAndAtivoTrue(chassi, id)) {
            throw new FieldValidationException("Já existe outro veículo ativo cadastrado com este chassi.");
        }
    }

    public void validateTransferencia(Long veiculoId,
                                      TransferenciaProprietarioDTO dto,
                                      HistoricoProprietarioModel proprietarioAtual) {
        validateId(veiculoId);
        if (dto == null) {
            throw new FieldValidationException("Os dados da transferência de proprietário são obrigatórios.");
        }
        validateCliente(dto.getNovoClienteId(), "O novo proprietário do veículo é obrigatório.");

        LocalDate dataInicio = dto.getDataInicioPosse() == null ? LocalDate.now() : dto.getDataInicioPosse();
        if (proprietarioAtual != null) {
            if (proprietarioAtual.getCliente().getId().equals(dto.getNovoClienteId())) {
                throw new FieldValidationException("O novo proprietário informado já é o proprietário atual do veículo.");
            }
            if (dataInicio.isBefore(proprietarioAtual.getDataInicioPosse())) {
                throw new FieldValidationException("A data da nova posse não pode ser anterior ao início da posse atual.");
            }
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador do veículo é obrigatório.");
        }
    }

    private void validateFields(VeiculoDTO dto, boolean validarProprietario) {
        if (dto == null) {
            throw new FieldValidationException("Os dados do veículo são obrigatórios.");
        }
        if (dto.getModeloId() == null || dto.getModeloId() <= 0) {
            throw new FieldValidationException("O modelo do veículo é obrigatório.");
        }
        if (dto.getPlaca() == null || dto.getPlaca().trim().isEmpty()) {
            throw new FieldValidationException("A placa do veículo é obrigatória.");
        }
        String placa = veiculoMapper.normalizePlaca(dto.getPlaca());
        if (placa.length() < 7 || placa.length() > 8) {
            throw new FieldValidationException("A placa do veículo deve possuir formato válido.");
        }
        if (dto.getAnoVeiculo() == null || dto.getAnoVeiculo() < 1900) {
            throw new FieldValidationException("O ano de fabricação do veículo é obrigatório e deve ser válido.");
        }
        if (dto.getAnoModelo() == null || dto.getAnoModelo() < 1900) {
            throw new FieldValidationException("O ano do modelo do veículo é obrigatório e deve ser válido.");
        }
        if (dto.getQuilometragemAtual() != null && dto.getQuilometragemAtual() < 0) {
            throw new FieldValidationException("A quilometragem do veículo não pode ser negativa.");
        }
        if (validarProprietario && (dto.getProprietarioAtualId() == null || dto.getProprietarioAtualId() <= 0)) {
            throw new FieldValidationException("O proprietário atual do veículo é obrigatório.");
        }
    }

    private void validateModelo(Long modeloId) {
        if (modeloRepository.findByIdAndAtivoTrue(modeloId).isEmpty()) {
            throw new FieldValidationException("O modelo informado não foi localizado ou está inativo.");
        }
    }

    private void validateCliente(Long clienteId, String mensagemObrigatorio) {
        if (clienteId == null || clienteId <= 0) {
            throw new FieldValidationException(mensagemObrigatorio);
        }
        if (clienteRepository.findByIdAndAtivoTrue(clienteId).isEmpty()) {
            throw new FieldValidationException("O cliente informado não foi localizado ou está inativo.");
        }
    }
}
