package br.com.avcar.oficina.business.servico.mapper;

import br.com.avcar.oficina.core.mapper.IGenericMapper;

import br.com.avcar.oficina.business.servico.dto.ServicoDTO;
import br.com.avcar.oficina.business.servico.enums.TipoServico;
import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import br.com.avcar.oficina.business.servico.model.ServicoInternoModel;
import br.com.avcar.oficina.business.servico.model.ServicoModel;
import br.com.avcar.oficina.business.servico.model.ServicoTerceirizadoModel;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre Serviço e DTO.
 */
@Component
public class ServicoMapper implements IGenericMapper<ServicoModel, ServicoDTO> {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public ServicoModel toServicoModel(ServicoDTO dto) {
        if (dto == null) {
            return null;
        }
        ServicoModel model = new ServicoModel();
        atualizarServicoModel(model, dto);
        return model;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarServicoModel(ServicoModel model, ServicoDTO dto) {
        model.setNomeServico(normalize(dto.getNomeServico()));
        model.setDescricao(normalize(dto.getDescricao()));
        model.setPrazoGarantiaDias(dto.getPrazoGarantiaDias() == null ? 90 : dto.getPrazoGarantiaDias());
        model.setValorBase(dto.getValorBase() == null ? BigDecimal.ZERO : dto.getValorBase());
    }

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public ServicoInternoModel toServicoInternoModel(ServicoModel servico, ServicoDTO dto) {
        ServicoInternoModel model = new ServicoInternoModel();
        model.setServico(servico);
        model.setObservacaoInterna(normalize(dto.getObservacaoInterna()));
        return model;
    }

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public ServicoTerceirizadoModel toServicoTerceirizadoModel(ServicoModel servico, ServicoDTO dto, EmpresaTerceirizadaModel empresaPadrao) {
        ServicoTerceirizadoModel model = new ServicoTerceirizadoModel();
        model.setServico(servico);
        model.setEmpresaTerceirizadaPadrao(empresaPadrao);
        model.setObservacaoTerceirizacao(normalize(dto.getObservacaoTerceirizacao()));
        return model;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarServicoInternoModel(ServicoInternoModel model, ServicoModel servico, ServicoDTO dto) {
        model.setServico(servico);
        model.setObservacaoInterna(normalize(dto.getObservacaoInterna()));
        model.setAtivo(Boolean.TRUE);
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarServicoTerceirizadoModel(ServicoTerceirizadoModel model, ServicoModel servico, ServicoDTO dto, EmpresaTerceirizadaModel empresaPadrao) {
        model.setServico(servico);
        model.setEmpresaTerceirizadaPadrao(empresaPadrao);
        model.setObservacaoTerceirizacao(normalize(dto.getObservacaoTerceirizacao()));
        model.setAtivo(Boolean.TRUE);
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public ServicoDTO toDto(ServicoModel servico,
                            ServicoInternoModel servicoInterno,
                            ServicoTerceirizadoModel servicoTerceirizado) {
        if (servico == null) {
            return null;
        }
        ServicoDTO dto = new ServicoDTO();
        dto.setId(servico.getId());
        dto.setAtivo(servico.getAtivo());
        dto.setDataHoraCriacao(servico.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(servico.getDataHoraAtualizacao());
        dto.setNomeServico(servico.getNomeServico());
        dto.setDescricao(servico.getDescricao());
        dto.setPrazoGarantiaDias(servico.getPrazoGarantiaDias());
        dto.setValorBase(servico.getValorBase());

        if (servicoInterno != null) {
            dto.setTipoServico(TipoServico.INTERNO);
            dto.setObservacaoInterna(servicoInterno.getObservacaoInterna());
        }

        if (servicoTerceirizado != null) {
            dto.setTipoServico(TipoServico.TERCEIRIZADO);
            dto.setObservacaoTerceirizacao(servicoTerceirizado.getObservacaoTerceirizacao());
            if (servicoTerceirizado.getEmpresaTerceirizadaPadrao() != null) {
                dto.setIdEmpresaTerceirizadaPadrao(servicoTerceirizado.getEmpresaTerceirizadaPadrao().getId());
                dto.setNomeEmpresaTerceirizadaPadrao(servicoTerceirizado.getEmpresaTerceirizadaPadrao().getNomeEmpresa());
            }
        }

        return dto;
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
