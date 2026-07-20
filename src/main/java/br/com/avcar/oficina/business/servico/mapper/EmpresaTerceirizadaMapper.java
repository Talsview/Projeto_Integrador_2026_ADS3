package br.com.avcar.oficina.business.servico.mapper;

import br.com.avcar.oficina.core.mapper.IGenericMapper;

import br.com.avcar.oficina.business.servico.dto.EmpresaTerceirizadaDTO;
import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre EmpresaTerceirizadaModel e DTO.
 */
@Component
public class EmpresaTerceirizadaMapper implements IGenericMapper<EmpresaTerceirizadaModel, EmpresaTerceirizadaDTO> {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public EmpresaTerceirizadaModel toModel(EmpresaTerceirizadaDTO dto) {
        if (dto == null) {
            return null;
        }
        EmpresaTerceirizadaModel model = new EmpresaTerceirizadaModel();
        atualizarModel(model, dto);
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public EmpresaTerceirizadaDTO toDto(EmpresaTerceirizadaModel model) {
        if (model == null) {
            return null;
        }
        EmpresaTerceirizadaDTO dto = new EmpresaTerceirizadaDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setNomeEmpresa(model.getNomeEmpresa());
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
    public void atualizarModel(EmpresaTerceirizadaModel model, EmpresaTerceirizadaDTO dto) {
        model.setNomeEmpresa(normalize(dto.getNomeEmpresa()));
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
