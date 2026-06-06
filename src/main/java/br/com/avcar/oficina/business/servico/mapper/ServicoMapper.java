package br.com.avcar.oficina.business.servico.mapper;

import br.com.avcar.oficina.business.servico.dto.ServicoDTO;
import br.com.avcar.oficina.business.servico.enums.TipoServico;
import br.com.avcar.oficina.business.servico.model.ServicoInternoModel;
import br.com.avcar.oficina.business.servico.model.ServicoModel;
import br.com.avcar.oficina.business.servico.model.ServicoTerceirizadoModel;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre Serviço e DTO.
 */
@Component
public class ServicoMapper {

    public ServicoModel toServicoModel(ServicoDTO dto) {
        if (dto == null) {
            return null;
        }
        ServicoModel model = new ServicoModel();
        atualizarServicoModel(model, dto);
        return model;
    }

    public void atualizarServicoModel(ServicoModel model, ServicoDTO dto) {
        model.setNomeServico(normalize(dto.getNomeServico()));
        model.setDescricao(normalize(dto.getDescricao()));
        model.setPrazoGarantiaDias(dto.getPrazoGarantiaDias() == null ? 90 : dto.getPrazoGarantiaDias());
        model.setValorBase(dto.getValorBase() == null ? BigDecimal.ZERO : dto.getValorBase());
    }

    public ServicoInternoModel toServicoInternoModel(ServicoModel servico, ServicoDTO dto) {
        ServicoInternoModel model = new ServicoInternoModel();
        model.setServico(servico);
        model.setObservacaoInterna(normalize(dto.getObservacaoInterna()));
        return model;
    }

    public ServicoTerceirizadoModel toServicoTerceirizadoModel(ServicoModel servico, ServicoDTO dto) {
        ServicoTerceirizadoModel model = new ServicoTerceirizadoModel();
        model.setServico(servico);
        model.setObservacaoTerceirizacao(normalize(dto.getObservacaoTerceirizacao()));
        return model;
    }

    public void atualizarServicoInternoModel(ServicoInternoModel model, ServicoModel servico, ServicoDTO dto) {
        model.setServico(servico);
        model.setObservacaoInterna(normalize(dto.getObservacaoInterna()));
        model.setAtivo(Boolean.TRUE);
    }

    public void atualizarServicoTerceirizadoModel(ServicoTerceirizadoModel model, ServicoModel servico, ServicoDTO dto) {
        model.setServico(servico);
        model.setObservacaoTerceirizacao(normalize(dto.getObservacaoTerceirizacao()));
        model.setAtivo(Boolean.TRUE);
    }

    public ServicoDTO toDto(ServicoModel servico,
                            ServicoInternoModel servicoInterno,
                            ServicoTerceirizadoModel servicoTerceirizado) {
        if (servico == null) {
            return null;
        }
        ServicoDTO dto = new ServicoDTO();
        dto.setId(servico.getId());
        dto.setAtivo(servico.getAtivo());
        dto.setNomeServico(servico.getNomeServico());
        dto.setDescricao(servico.getDescricao());
        dto.setPrazoGarantiaDias(servico.getPrazoGarantiaDias());
        dto.setValorBase(servico.getValorBase());

        if (servicoInterno != null && Boolean.TRUE.equals(servicoInterno.getAtivo())) {
            dto.setTipoServico(TipoServico.INTERNO);
            dto.setObservacaoInterna(servicoInterno.getObservacaoInterna());
        }

        if (servicoTerceirizado != null && Boolean.TRUE.equals(servicoTerceirizado.getAtivo())) {
            dto.setTipoServico(TipoServico.TERCEIRIZADO);
            dto.setObservacaoTerceirizacao(servicoTerceirizado.getObservacaoTerceirizacao());
        }

        return dto;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
