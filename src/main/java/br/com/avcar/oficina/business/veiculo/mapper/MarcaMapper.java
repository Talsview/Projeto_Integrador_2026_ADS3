package br.com.avcar.oficina.business.veiculo.mapper;

import br.com.avcar.oficina.core.mapper.IGenericMapper;

import br.com.avcar.oficina.business.veiculo.dto.MarcaDTO;
import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre MarcaModel e MarcaDTO.
 */
@Component
public class MarcaMapper implements IGenericMapper<MarcaModel, MarcaDTO> {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public MarcaModel toModel(MarcaDTO dto) {
        if (dto == null) {
            return null;
        }
        MarcaModel model = new MarcaModel();
        model.setNomeMarca(normalize(dto.getNomeMarca()));
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public MarcaDTO toDto(MarcaModel model) {
        if (model == null) {
            return null;
        }
        MarcaDTO dto = new MarcaDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setNomeMarca(model.getNomeMarca());
        return dto;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarModel(MarcaModel model, MarcaDTO dto) {
        model.setNomeMarca(normalize(dto.getNomeMarca()));
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação normalize.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
