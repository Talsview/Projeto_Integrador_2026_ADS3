package br.com.avcar.oficina.business.garantia.mapper;

import br.com.avcar.oficina.core.mapper.IGenericMapper;

import br.com.avcar.oficina.business.garantia.dto.GarantiaServicoDTO;
import br.com.avcar.oficina.business.garantia.enums.StatusGarantia;
import br.com.avcar.oficina.business.garantia.model.GarantiaServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.ExecucaoServicoTerceirizadoModel;
import br.com.avcar.oficina.business.ordemservico.repository.IExecucaoServicoTerceirizadoRepository;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre GarantiaServicoModel e GarantiaServicoDTO.
 */
@Component
public class GarantiaServicoMapper implements IGenericMapper<GarantiaServicoModel, GarantiaServicoDTO> {

    private final IExecucaoServicoTerceirizadoRepository execucaoRepository;

    /**
     * Função: recebe o repositório de execução terceirizada utilizado para complementar
     * a garantia de serviço com a empresa que realmente executou o trabalho externo.
     * Uso no sistema: permite que a tela de garantias mostre colaborador para serviço
     * interno e empresa terceirizada para serviço externo, preservando rastreabilidade.
     */
    public GarantiaServicoMapper(IExecucaoServicoTerceirizadoRepository execucaoRepository) {
        this.execucaoRepository = execucaoRepository;
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação criar aguardando finalizacao.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public GarantiaServicoModel criarAguardandoFinalizacao(ItemServicoModel itemServico) {
        GarantiaServicoModel model = new GarantiaServicoModel();
        model.setItemServico(itemServico);
        model.setPrazoDias(resolverPrazoDias(itemServico));
        model.setStatusGarantia(StatusGarantia.AGUARDANDO_FINALIZACAO_OS);
        model.setObservacao("Garantia criada automaticamente para serviço da OS. A contagem inicia após finalização.");
        return model;
    }

    /**
     * Função: Converte a entidade de auditoria de notificação em DTO de resposta para a API.
     * Uso no sistema: permite consultar notificações auditadas sem expor diretamente o modelo do
     * banco.
     */
    public GarantiaServicoDTO toDto(GarantiaServicoModel model) {
        if (model == null) {
            return null;
        }
        GarantiaServicoDTO dto = new GarantiaServicoDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setDataHoraCriacao(model.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(model.getDataHoraAtualizacao());
        dto.setPrazoDias(model.getPrazoDias());
        dto.setDataInicio(model.getDataInicio());
        dto.setDataFim(model.getDataFim());
        dto.setStatusGarantia(model.getStatusGarantia());
        dto.setDataAcionamento(model.getDataAcionamento());
        dto.setMotivoAcionamento(model.getMotivoAcionamento());
        dto.setDescricaoDefeito(model.getDescricaoDefeito());
        dto.setResponsavelAnalise(model.getResponsavelAnalise());
        dto.setDataEncerramento(model.getDataEncerramento());
        dto.setSolucaoAplicada(model.getSolucaoAplicada());
        dto.setCustoAssumidoPor(model.getCustoAssumidoPor());
        dto.setAtendimentoRealizado(model.getAtendimentoRealizado());
        dto.setObservacao(model.getObservacao());

        if (model.getItemServico() != null) {
            ItemServicoModel item = model.getItemServico();
            dto.setIdItemServico(item.getId());
            if (item.getOrdemServico() != null) {
                dto.setIdOrdemServico(item.getOrdemServico().getId());
            }
            if (item.getServico() != null) {
                dto.setIdServico(item.getServico().getId());
                dto.setNomeServico(item.getServico().getNomeServico());
            }
            if (item.getColaborador() != null) {
                dto.setTipoExecucaoServico("INTERNO");
                dto.setIdColaborador(item.getColaborador().getId());
                if (item.getColaborador().getPessoa() != null) {
                    dto.setNomeColaboradorResponsavel(item.getColaborador().getPessoa().getNome());
                }
            }

            execucaoRepository.findByItemServicoIdAndAtivoTrue(item.getId()).ifPresent(execucao -> preencherExecucaoTerceirizada(dto, execucao));

            if (dto.getTipoExecucaoServico() == null) {
                dto.setTipoExecucaoServico(item.getColaborador() == null ? "TERCEIRIZADO" : "INTERNO");
            }
        }
        return dto;
    }

    /**
     * Função: preenche no DTO os dados da empresa terceirizada que executou o serviço.
     * Uso no sistema: quando a garantia é de serviço terceirizado, o responsável exibido
     * não é um colaborador interno, mas a empresa externa registrada na OS.
     */
    private void preencherExecucaoTerceirizada(GarantiaServicoDTO dto, ExecucaoServicoTerceirizadoModel execucao) {
        dto.setTipoExecucaoServico("TERCEIRIZADO");
        dto.setIdEmpresaTerceirizada(execucao.getEmpresaTerceirizada() != null ? execucao.getEmpresaTerceirizada().getId() : null);
        dto.setNomeEmpresaTerceirizada(execucao.getEmpresaTerceirizada() != null ? execucao.getEmpresaTerceirizada().getNomeEmpresa() : null);
        dto.setDataEnvioTerceirizacao(execucao.getDataEnvio());
        dto.setDataRetornoTerceirizacao(execucao.getDataRetorno());
        dto.setValorCobradoTerceirizacao(execucao.getValorCobrado());
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação resolver prazo dias.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public Integer resolverPrazoDias(ItemServicoModel itemServico) {
        if (itemServico != null
                && itemServico.getServico() != null
                && itemServico.getServico().getPrazoGarantiaDias() != null) {
            return itemServico.getServico().getPrazoGarantiaDias();
        }
        return 90;
    }
}
