package br.com.avcar.oficina.business.peca.mapper;

import br.com.avcar.oficina.business.peca.dto.FornecedorDTO;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre FornecedorModel e FornecedorDTO.
 */
@Component
public class FornecedorMapper {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public FornecedorModel toModel(FornecedorDTO dto) {
        if (dto == null) {
            return null;
        }
        FornecedorModel model = new FornecedorModel();
        atualizarModel(model, dto);
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public FornecedorDTO toDto(FornecedorModel model) {
        if (model == null) {
            return null;
        }
        FornecedorDTO dto = new FornecedorDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setNomeFornecedor(model.getNomeFornecedor());
        dto.setCnpj(model.getCnpj());
        dto.setTelefone(model.getTelefone());
        dto.setEmail(model.getEmail());
        dto.setEndereco(model.getEndereco());
        return dto;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarModel(FornecedorModel model, FornecedorDTO dto) {
        model.setNomeFornecedor(normalize(dto.getNomeFornecedor()));
        model.setCnpj(onlyDigits(dto.getCnpj()));
        model.setTelefone(normalize(dto.getTelefone()));
        model.setEmail(normalize(dto.getEmail()));
        model.setEndereco(normalize(dto.getEndereco()));
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
     * Função: Mapeia dados entre camadas durante a operação only digits.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    private String onlyDigits(String value) {
        if (value == null) {
            return null;
        }
        String digits = value.replaceAll("\\D", "");
        return digits.isEmpty() ? null : digits;
    }
}
