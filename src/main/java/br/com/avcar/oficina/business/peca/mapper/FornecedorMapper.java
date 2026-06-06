package br.com.avcar.oficina.business.peca.mapper;

import br.com.avcar.oficina.business.peca.dto.FornecedorDTO;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre FornecedorModel e FornecedorDTO.
 */
@Component
public class FornecedorMapper {

    public FornecedorModel toModel(FornecedorDTO dto) {
        if (dto == null) {
            return null;
        }
        FornecedorModel model = new FornecedorModel();
        atualizarModel(model, dto);
        return model;
    }

    public FornecedorDTO toDto(FornecedorModel model) {
        if (model == null) {
            return null;
        }
        FornecedorDTO dto = new FornecedorDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setNomeFornecedor(model.getNomeFornecedor());
        dto.setCnpj(model.getCnpj());
        dto.setTelefone(model.getTelefone());
        dto.setEmail(model.getEmail());
        dto.setEndereco(model.getEndereco());
        return dto;
    }

    public void atualizarModel(FornecedorModel model, FornecedorDTO dto) {
        model.setNomeFornecedor(normalize(dto.getNomeFornecedor()));
        model.setCnpj(onlyDigits(dto.getCnpj()));
        model.setTelefone(normalize(dto.getTelefone()));
        model.setEmail(normalize(dto.getEmail()));
        model.setEndereco(normalize(dto.getEndereco()));
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String onlyDigits(String value) {
        if (value == null) {
            return null;
        }
        String digits = value.replaceAll("\\D", "");
        return digits.isEmpty() ? null : digits;
    }
}
