package br.com.avcar.oficina.business.veiculo.mapper;

import br.com.avcar.oficina.core.mapper.IGenericMapper;

import br.com.avcar.oficina.business.veiculo.dto.ModeloDTO;
import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import br.com.avcar.oficina.business.veiculo.model.ModeloModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre ModeloModel e ModeloDTO.
 */
@Component
public class ModeloMapper implements IGenericMapper<ModeloModel, ModeloDTO> {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public ModeloModel toModel(ModeloDTO dto, MarcaModel marca) {
        if (dto == null) {
            return null;
        }
        ModeloModel model = new ModeloModel();
        model.setMarca(marca);
        model.setNomeModelo(normalize(dto.getNomeModelo()));
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public ModeloDTO toDto(ModeloModel model) {
        if (model == null) {
            return null;
        }
        ModeloDTO dto = new ModeloDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setNomeModelo(model.getNomeModelo());
        if (model.getMarca() != null) {
            dto.setMarcaId(model.getMarca().getId());
            dto.setNomeMarca(model.getMarca().getNomeMarca());
        }
        return dto;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarModel(ModeloModel model, ModeloDTO dto, MarcaModel marca) {
        model.setMarca(marca);
        model.setNomeModelo(normalize(dto.getNomeModelo()));
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação normalize.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
