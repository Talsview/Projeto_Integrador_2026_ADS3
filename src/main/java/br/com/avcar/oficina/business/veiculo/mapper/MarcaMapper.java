package br.com.avcar.oficina.business.veiculo.mapper;

import br.com.avcar.oficina.business.veiculo.dto.MarcaDTO;
import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre MarcaModel e MarcaDTO.
 */
@Component
public class MarcaMapper {

    public MarcaModel toModel(MarcaDTO dto) {
        if (dto == null) {
            return null;
        }
        MarcaModel model = new MarcaModel();
        model.setNomeMarca(normalize(dto.getNomeMarca()));
        return model;
    }

    public MarcaDTO toDto(MarcaModel model) {
        if (model == null) {
            return null;
        }
        MarcaDTO dto = new MarcaDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setNomeMarca(model.getNomeMarca());
        return dto;
    }

    public void atualizarModel(MarcaModel model, MarcaDTO dto) {
        model.setNomeMarca(normalize(dto.getNomeMarca()));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
