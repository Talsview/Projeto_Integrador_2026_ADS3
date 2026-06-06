package br.com.avcar.oficina.business.veiculo.mapper;

import br.com.avcar.oficina.business.veiculo.dto.ModeloDTO;
import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import br.com.avcar.oficina.business.veiculo.model.ModeloModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre ModeloModel e ModeloDTO.
 */
@Component
public class ModeloMapper {

    public ModeloModel toModel(ModeloDTO dto, MarcaModel marca) {
        if (dto == null) {
            return null;
        }
        ModeloModel model = new ModeloModel();
        model.setMarca(marca);
        model.setNomeModelo(normalize(dto.getNomeModelo()));
        return model;
    }

    public ModeloDTO toDto(ModeloModel model) {
        if (model == null) {
            return null;
        }
        ModeloDTO dto = new ModeloDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setNomeModelo(model.getNomeModelo());
        if (model.getMarca() != null) {
            dto.setMarcaId(model.getMarca().getId());
            dto.setNomeMarca(model.getMarca().getNomeMarca());
        }
        return dto;
    }

    public void atualizarModel(ModeloModel model, ModeloDTO dto, MarcaModel marca) {
        model.setMarca(marca);
        model.setNomeModelo(normalize(dto.getNomeModelo()));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
