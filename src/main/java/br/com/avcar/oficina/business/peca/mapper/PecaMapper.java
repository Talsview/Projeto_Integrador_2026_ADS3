package br.com.avcar.oficina.business.peca.mapper;

import br.com.avcar.oficina.core.mapper.IGenericMapper;

import br.com.avcar.oficina.business.peca.dto.PecaDTO;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
import br.com.avcar.oficina.business.peca.model.PecaModel;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre PecaModel e PecaDTO.
 */
@Component
public class PecaMapper implements IGenericMapper<PecaModel, PecaDTO> {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public PecaModel toModel(PecaDTO dto, FornecedorModel fornecedorPadrao) {
        if (dto == null) {
            return null;
        }
        PecaModel model = new PecaModel();
        atualizarModel(model, dto, fornecedorPadrao);
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public PecaDTO toDto(PecaModel model) {
        if (model == null) {
            return null;
        }
        PecaDTO dto = new PecaDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setNomePeca(model.getNomePeca());
        dto.setCodigoNacional(model.getCodigoNacional());
        dto.setMarcaPeca(model.getMarcaPeca());
        dto.setModeloAplicavel(model.getModeloAplicavel());
        dto.setAnoVeiculo(model.getAnoVeiculo());
        dto.setAnoModelo(model.getAnoModelo());
        if (model.getFornecedorPadrao() != null) {
            dto.setIdFornecedorPadrao(model.getFornecedorPadrao().getId());
            dto.setNomeFornecedorPadrao(model.getFornecedorPadrao().getNomeFornecedor());
        }
        dto.setValorUnitarioPadrao(model.getValorUnitarioPadrao());
        dto.setPrazoGarantiaDias(model.getPrazoGarantiaDias());
        dto.setDescricao(model.getDescricao());
        return dto;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarModel(PecaModel model, PecaDTO dto, FornecedorModel fornecedorPadrao) {
        model.setNomePeca(normalize(dto.getNomePeca()));
        model.setCodigoNacional(normalizeUpper(dto.getCodigoNacional()));
        model.setMarcaPeca(normalize(dto.getMarcaPeca()));
        model.setModeloAplicavel(normalize(dto.getModeloAplicavel()));
        model.setAnoVeiculo(dto.getAnoVeiculo());
        model.setAnoModelo(dto.getAnoModelo());
        model.setFornecedorPadrao(fornecedorPadrao);
        model.setValorUnitarioPadrao(dto.getValorUnitarioPadrao() == null ? BigDecimal.ZERO : dto.getValorUnitarioPadrao());
        model.setPrazoGarantiaDias(dto.getPrazoGarantiaDias() == null ? 90 : dto.getPrazoGarantiaDias());
        model.setDescricao(normalize(dto.getDescricao()));
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação normalize.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação normalize upper.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private String normalizeUpper(String value) {
        String normalized = normalize(value);
        return normalized == null ? null : normalized.toUpperCase();
    }
}
