package br.com.avcar.oficina.business.pessoa.mapper;

import br.com.avcar.oficina.business.pessoa.dto.FuncaoDTO;
import br.com.avcar.oficina.business.pessoa.model.FuncaoModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre FuncaoModel e FuncaoDTO.
 */
@Component
public class FuncaoMapper {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public FuncaoModel toModel(FuncaoDTO dto) {
        if (dto == null) {
            return null;
        }
        FuncaoModel model = new FuncaoModel();
        model.setNomeFuncao(normalize(dto.getNomeFuncao()));
        model.setDescricao(dto.getDescricao());
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public FuncaoDTO toDto(FuncaoModel model) {
        if (model == null) {
            return null;
        }
        FuncaoDTO dto = new FuncaoDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setNomeFuncao(model.getNomeFuncao());
        dto.setDescricao(model.getDescricao());
        return dto;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarModel(FuncaoModel model, FuncaoDTO dto) {
        model.setNomeFuncao(normalize(dto.getNomeFuncao()));
        model.setDescricao(dto.getDescricao());
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação normalize.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}
