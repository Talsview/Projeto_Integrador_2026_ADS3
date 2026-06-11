package br.com.avcar.oficina.business.peca.mapper;

import br.com.avcar.oficina.business.peca.dto.ItemPecaDTO;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
import br.com.avcar.oficina.business.peca.model.PecaModel;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão da entidade associativa ItemPeca.
 */
@Component
public class ItemPecaMapper {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public ItemPecaModel toModel(ItemPecaDTO dto, PecaModel peca, FornecedorModel fornecedor) {
        if (dto == null) {
            return null;
        }
        ItemPecaModel model = new ItemPecaModel();
        atualizarModel(model, dto, peca, fornecedor);
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public ItemPecaDTO toDto(ItemPecaModel model) {
        if (model == null) {
            return null;
        }
        ItemPecaDTO dto = new ItemPecaDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setIdOrdemServico(model.getIdOrdemServico());
        dto.setQuantidade(model.getQuantidade());
        dto.setValorUnitario(model.getValorUnitario());
        dto.setValorTotal(model.getValorTotal());
        dto.setObservacao(model.getObservacao());

        if (model.getPeca() != null) {
            dto.setIdPeca(model.getPeca().getId());
            dto.setNomePeca(model.getPeca().getNomePeca());
            dto.setCodigoNacional(model.getPeca().getCodigoNacional());
        }
        if (model.getFornecedor() != null) {
            dto.setIdFornecedor(model.getFornecedor().getId());
            dto.setNomeFornecedor(model.getFornecedor().getNomeFornecedor());
        }
        return dto;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarModel(ItemPecaModel model, ItemPecaDTO dto, PecaModel peca, FornecedorModel fornecedor) {
        model.setIdOrdemServico(dto.getIdOrdemServico());
        model.setPeca(peca);
        model.setFornecedor(fornecedor);
        model.setQuantidade(defaultIfNull(dto.getQuantidade(), BigDecimal.ONE));
        model.setValorUnitario(defaultIfNull(dto.getValorUnitario(), BigDecimal.ZERO));
        model.setValorTotal(model.getQuantidade().multiply(model.getValorUnitario()));
        model.setObservacao(normalize(dto.getObservacao()));
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação default if null.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private BigDecimal defaultIfNull(BigDecimal value, BigDecimal defaultValue) {
        return value == null ? defaultValue : value;
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
}
