package br.com.avcar.oficina.business.ordemservico.mapper;

import br.com.avcar.oficina.business.ordemservico.dto.ItemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.model.ExecucaoServicoTerceirizadoModel;
import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.pessoa.model.ColaboradorModel;
import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import br.com.avcar.oficina.business.servico.model.ServicoModel;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Mapper de ItemServico e de sua execução terceirizada opcional.
 */
@Component
public class ItemServicoMapper {

    public ItemServicoModel toModel(ItemServicoDTO dto,
                                    OrdemServicoModel ordemServico,
                                    ServicoModel servico,
                                    ColaboradorModel colaborador) {
        ItemServicoModel model = new ItemServicoModel();
        model.setOrdemServico(ordemServico);
        model.setServico(servico);
        model.setColaborador(colaborador);
        atualizarModel(model, dto, ordemServico, servico, colaborador);
        return model;
    }

    public void atualizarModel(ItemServicoModel model,
                               ItemServicoDTO dto,
                               OrdemServicoModel ordemServico,
                               ServicoModel servico,
                               ColaboradorModel colaborador) {
        model.setOrdemServico(ordemServico);
        model.setServico(servico);
        model.setColaborador(colaborador);
        model.setDescricaoExecucao(normalize(dto.getDescricaoExecucao()));
        model.setQuantidade(dto.getQuantidade() == null ? BigDecimal.ONE : dto.getQuantidade());
        model.setValorUnitario(dto.getValorUnitario() == null ? BigDecimal.ZERO : dto.getValorUnitario());
        model.setValorTotal(model.getQuantidade().multiply(model.getValorUnitario()));
        model.setDataInicio(dto.getDataInicio());
        model.setDataFim(dto.getDataFim());
    }

    public ExecucaoServicoTerceirizadoModel toExecucaoTerceirizada(ItemServicoModel itemServico,
                                                                    EmpresaTerceirizadaModel empresa,
                                                                    ItemServicoDTO dto) {
        ExecucaoServicoTerceirizadoModel model = new ExecucaoServicoTerceirizadoModel();
        atualizarExecucaoTerceirizada(model, itemServico, empresa, dto);
        return model;
    }

    public void atualizarExecucaoTerceirizada(ExecucaoServicoTerceirizadoModel model,
                                               ItemServicoModel itemServico,
                                               EmpresaTerceirizadaModel empresa,
                                               ItemServicoDTO dto) {
        model.setItemServico(itemServico);
        model.setEmpresaTerceirizada(empresa);
        model.setDataEnvio(dto.getDataEnvioTerceirizacao());
        model.setDataRetorno(dto.getDataRetornoTerceirizacao());
        model.setValorCobrado(dto.getValorCobradoTerceirizacao() == null ? BigDecimal.ZERO : dto.getValorCobradoTerceirizacao());
        model.setObservacao(normalize(dto.getObservacaoTerceirizacao()));
        model.setAtivo(Boolean.TRUE);
    }

    public ItemServicoDTO toDto(ItemServicoModel itemServico, ExecucaoServicoTerceirizadoModel execucao) {
        if (itemServico == null) {
            return null;
        }
        ItemServicoDTO dto = new ItemServicoDTO();
        dto.setId(itemServico.getId());
        dto.setAtivo(itemServico.getAtivo());
        dto.setDescricaoExecucao(itemServico.getDescricaoExecucao());
        dto.setQuantidade(itemServico.getQuantidade());
        dto.setValorUnitario(itemServico.getValorUnitario());
        dto.setValorTotal(itemServico.getValorTotal());
        dto.setDataInicio(itemServico.getDataInicio());
        dto.setDataFim(itemServico.getDataFim());

        if (itemServico.getOrdemServico() != null) {
            dto.setIdOrdemServico(itemServico.getOrdemServico().getId());
            dto.setNumeroOs(itemServico.getOrdemServico().getNumeroOs());
        }
        if (itemServico.getServico() != null) {
            dto.setIdServico(itemServico.getServico().getId());
            dto.setNomeServico(itemServico.getServico().getNomeServico());
        }
        if (itemServico.getColaborador() != null) {
            dto.setIdColaborador(itemServico.getColaborador().getId());
            if (itemServico.getColaborador().getPessoa() != null) {
                dto.setNomeColaborador(itemServico.getColaborador().getPessoa().getNome());
            }
        }
        if (execucao != null && Boolean.TRUE.equals(execucao.getAtivo())) {
            dto.setIdExecucaoServicoTerceirizado(execucao.getId());
            dto.setDataEnvioTerceirizacao(execucao.getDataEnvio());
            dto.setDataRetornoTerceirizacao(execucao.getDataRetorno());
            dto.setValorCobradoTerceirizacao(execucao.getValorCobrado());
            dto.setObservacaoTerceirizacao(execucao.getObservacao());
            if (execucao.getEmpresaTerceirizada() != null) {
                dto.setIdEmpresaTerceirizada(execucao.getEmpresaTerceirizada().getId());
                dto.setNomeEmpresaTerceirizada(execucao.getEmpresaTerceirizada().getNomeEmpresa());
            }
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
