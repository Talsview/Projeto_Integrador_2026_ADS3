package br.com.avcar.oficina.business.servico.mapper;

import br.com.avcar.oficina.business.servico.dto.EmpresaTerceirizadaDTO;
import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre EmpresaTerceirizadaModel e DTO.
 */
@Component
public class EmpresaTerceirizadaMapper {

    public EmpresaTerceirizadaModel toModel(EmpresaTerceirizadaDTO dto) {
        if (dto == null) {
            return null;
        }
        EmpresaTerceirizadaModel model = new EmpresaTerceirizadaModel();
        atualizarModel(model, dto);
        return model;
    }

    public EmpresaTerceirizadaDTO toDto(EmpresaTerceirizadaModel model) {
        if (model == null) {
            return null;
        }
        EmpresaTerceirizadaDTO dto = new EmpresaTerceirizadaDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setNomeEmpresa(model.getNomeEmpresa());
        dto.setCnpj(model.getCnpj());
        dto.setTelefone(model.getTelefone());
        dto.setEmail(model.getEmail());
        dto.setEndereco(model.getEndereco());
        return dto;
    }

    public void atualizarModel(EmpresaTerceirizadaModel model, EmpresaTerceirizadaDTO dto) {
        model.setNomeEmpresa(normalize(dto.getNomeEmpresa()));
        model.setCnpj(onlyDigits(dto.getCnpj()));
        model.setTelefone(normalize(dto.getTelefone()));
        model.setEmail(normalize(dto.getEmail()));
        model.setEndereco(normalize(dto.getEndereco()));
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String onlyDigits(String value) {
        if (value == null) {
            return null;
        }
        String digits = value.replaceAll("\\D", "");
        return digits.isEmpty() ? null : digits;
    }
}
