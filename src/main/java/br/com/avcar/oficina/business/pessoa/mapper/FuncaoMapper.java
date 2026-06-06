package br.com.avcar.oficina.business.pessoa.mapper;

import br.com.avcar.oficina.business.pessoa.dto.FuncaoDTO;
import br.com.avcar.oficina.business.pessoa.model.FuncaoModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre FuncaoModel e FuncaoDTO.
 */
@Component
public class FuncaoMapper {

    public FuncaoModel toModel(FuncaoDTO dto) {
        if (dto == null) {
            return null;
        }
        FuncaoModel model = new FuncaoModel();
        model.setNomeFuncao(normalize(dto.getNomeFuncao()));
        model.setDescricao(dto.getDescricao());
        return model;
    }

    public FuncaoDTO toDto(FuncaoModel model) {
        if (model == null) {
            return null;
        }
        FuncaoDTO dto = new FuncaoDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setNomeFuncao(model.getNomeFuncao());
        dto.setDescricao(model.getDescricao());
        return dto;
    }

    public void atualizarModel(FuncaoModel model, FuncaoDTO dto) {
        model.setNomeFuncao(normalize(dto.getNomeFuncao()));
        model.setDescricao(dto.getDescricao());
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}
