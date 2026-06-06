package br.com.avcar.oficina.business.peca.mapper;

import br.com.avcar.oficina.business.peca.dto.PecaDTO;
import br.com.avcar.oficina.business.peca.model.PecaModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre PecaModel e PecaDTO.
 */
@Component
public class PecaMapper {

    public PecaModel toModel(PecaDTO dto) {
        if (dto == null) {
            return null;
        }
        PecaModel model = new PecaModel();
        atualizarModel(model, dto);
        return model;
    }

    public PecaDTO toDto(PecaModel model) {
        if (model == null) {
            return null;
        }
        PecaDTO dto = new PecaDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setNomePeca(model.getNomePeca());
        dto.setCodigoNacional(model.getCodigoNacional());
        dto.setMarcaPeca(model.getMarcaPeca());
        dto.setModeloAplicavel(model.getModeloAplicavel());
        dto.setAnoVeiculo(model.getAnoVeiculo());
        dto.setAnoModelo(model.getAnoModelo());
        dto.setPrazoGarantiaDias(model.getPrazoGarantiaDias());
        dto.setDescricao(model.getDescricao());
        return dto;
    }

    public void atualizarModel(PecaModel model, PecaDTO dto) {
        model.setNomePeca(normalize(dto.getNomePeca()));
        model.setCodigoNacional(normalizeUpper(dto.getCodigoNacional()));
        model.setMarcaPeca(normalize(dto.getMarcaPeca()));
        model.setModeloAplicavel(normalize(dto.getModeloAplicavel()));
        model.setAnoVeiculo(dto.getAnoVeiculo());
        model.setAnoModelo(dto.getAnoModelo());
        model.setPrazoGarantiaDias(dto.getPrazoGarantiaDias() == null ? 90 : dto.getPrazoGarantiaDias());
        model.setDescricao(normalize(dto.getDescricao()));
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeUpper(String value) {
        String normalized = normalize(value);
        return normalized == null ? null : normalized.toUpperCase();
    }
}
