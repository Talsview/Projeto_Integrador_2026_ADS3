package br.com.avcar.oficina.business.veiculo.adapter;

import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import br.com.avcar.oficina.business.veiculo.dto.HistoricoProprietarioDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoResumoDTO;
import br.com.avcar.oficina.business.veiculo.model.HistoricoProprietarioModel;
import br.com.avcar.oficina.business.veiculo.model.ModeloModel;
import br.com.avcar.oficina.business.veiculo.model.VeiculoModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * PADRÃO DE PROJETO: ADAPTER.
 *
 * Aplicação: adapta a estrutura interna formada por VeiculoModel,
 * ModeloModel, MarcaModel e HistoricoProprietarioModel para DTOs de resposta
 * consumidos pela View Angular.
 *
 * Justificativa: o domínio mantém a propriedade do veículo em uma entidade
 * associativa histórica, enquanto a interface precisa receber uma resposta
 * consolidada com marca, modelo, proprietário atual e histórico de proprietários.
 */
@Component
public class VeiculoResponseAdapter {

    /**
     * Função: Adapta dados de veículo no processamento adaptar para detalhe.
     * Padrão aplicado: ADAPTER.
     * Justificativa: separa o formato interno do domínio do formato consumido pelo frontend.
     * Uso no sistema: facilita a exibição de veículos com proprietário atual sem perder a
     * rastreabilidade histórica.
     */
    public VeiculoDTO adaptarParaDetalhe(VeiculoModel veiculo,
                                         HistoricoProprietarioModel proprietarioAtual,
                                         List<HistoricoProprietarioModel> historico) {
        if (veiculo == null) {
            return null;
        }

        VeiculoDTO dto = new VeiculoDTO();
        preencherDadosBase(dto, veiculo, proprietarioAtual);

        List<HistoricoProprietarioDTO> historicoDtos = new ArrayList<>();
        if (historico != null) {
            historico.stream()
                    .sorted(Comparator.comparing(HistoricoProprietarioModel::getDataInicioPosse).reversed())
                    .map(this::adaptarHistorico)
                    .forEach(historicoDtos::add);
        }
        dto.setHistoricoProprietarios(historicoDtos);
        return dto;
    }

    /**
     * Função: Adapta dados de veículo no processamento adaptar para resumo.
     * Padrão aplicado: ADAPTER.
     * Justificativa: separa o formato interno do domínio do formato consumido pelo frontend.
     * Uso no sistema: facilita a exibição de veículos com proprietário atual sem perder a
     * rastreabilidade histórica.
     */
    public VeiculoResumoDTO adaptarParaResumo(VeiculoModel veiculo, HistoricoProprietarioModel proprietarioAtual) {
        if (veiculo == null) {
            return null;
        }

        VeiculoResumoDTO dto = new VeiculoResumoDTO();
        dto.setId(veiculo.getId());
        dto.setAtivo(veiculo.getAtivo());
        dto.setDataHoraCriacao(veiculo.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(veiculo.getDataHoraAtualizacao());
        dto.setPlaca(veiculo.getPlaca());
        dto.setChassi(veiculo.getChassi());
        dto.setCor(veiculo.getCor());
        dto.setAnoVeiculo(veiculo.getAnoVeiculo());
        dto.setAnoModelo(veiculo.getAnoModelo());
        dto.setQuilometragemAtual(veiculo.getQuilometragemAtual());

        ModeloModel modelo = veiculo.getModelo();
        if (modelo != null) {
            dto.setModeloId(modelo.getId());
            dto.setNomeModelo(modelo.getNomeModelo());
            if (modelo.getMarca() != null) {
                dto.setMarcaId(modelo.getMarca().getId());
                dto.setNomeMarca(modelo.getMarca().getNomeMarca());
            }
        }

        if (proprietarioAtual != null && proprietarioAtual.getCliente() != null) {
            dto.setProprietarioAtualId(proprietarioAtual.getCliente().getId());
            if (proprietarioAtual.getCliente().getPessoa() != null) {
                dto.setNomeProprietarioAtual(proprietarioAtual.getCliente().getPessoa().getNome());
            }
        }
        return dto;
    }

    /**
     * Função: Adapta dados de veículo no processamento adaptar historico.
     * Padrão aplicado: ADAPTER.
     * Justificativa: separa o formato interno do domínio do formato consumido pelo frontend.
     * Uso no sistema: facilita a exibição de veículos com proprietário atual sem perder a
     * rastreabilidade histórica.
     */
    public HistoricoProprietarioDTO adaptarHistorico(HistoricoProprietarioModel historico) {
        if (historico == null) {
            return null;
        }

        HistoricoProprietarioDTO dto = new HistoricoProprietarioDTO();
        dto.setId(historico.getId());
        dto.setAtivo(historico.getAtivo());
        dto.setDataHoraCriacao(historico.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(historico.getDataHoraAtualizacao());
        dto.setDataInicioPosse(historico.getDataInicioPosse());
        dto.setDataFimPosse(historico.getDataFimPosse());
        dto.setProprietarioAtual(historico.getProprietarioAtual());
        dto.setObservacao(historico.getObservacao());

        if (historico.getCliente() != null) {
            dto.setClienteId(historico.getCliente().getId());
            PessoaModel pessoa = historico.getCliente().getPessoa();
            if (pessoa != null) {
                dto.setNomeCliente(pessoa.getNome());
            }
        }

        if (historico.getVeiculo() != null) {
            VeiculoModel veiculo = historico.getVeiculo();
            dto.setVeiculoId(veiculo.getId());
            dto.setPlacaVeiculo(veiculo.getPlaca());
            dto.setAnoVeiculo(veiculo.getAnoVeiculo());
            dto.setAnoModelo(veiculo.getAnoModelo());
            dto.setChassiVeiculo(veiculo.getChassi());
            dto.setCorVeiculo(veiculo.getCor());
            dto.setQuilometragemAtual(veiculo.getQuilometragemAtual());

            ModeloModel modelo = veiculo.getModelo();
            if (modelo != null) {
                dto.setNomeModeloVeiculo(modelo.getNomeModelo());
                if (modelo.getMarca() != null) {
                    dto.setNomeMarcaVeiculo(modelo.getMarca().getNomeMarca());
                }
            }
        }
        return dto;
    }

    /**
     * Função: Adapta dados de veículo no processamento preencher dados base.
     * Padrão aplicado: ADAPTER.
     * Justificativa: separa o formato interno do domínio do formato consumido pelo frontend.
     * Uso no sistema: facilita a exibição de veículos com proprietário atual sem perder a
     * rastreabilidade histórica.
     */
    private void preencherDadosBase(VeiculoDTO dto, VeiculoModel veiculo, HistoricoProprietarioModel proprietarioAtual) {
        dto.setId(veiculo.getId());
        dto.setAtivo(veiculo.getAtivo());
        dto.setDataHoraCriacao(veiculo.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(veiculo.getDataHoraAtualizacao());
        dto.setPlaca(veiculo.getPlaca());
        dto.setChassi(veiculo.getChassi());
        dto.setCor(veiculo.getCor());
        dto.setAnoVeiculo(veiculo.getAnoVeiculo());
        dto.setAnoModelo(veiculo.getAnoModelo());
        dto.setQuilometragemAtual(veiculo.getQuilometragemAtual());
        dto.setObservacao(veiculo.getObservacao());

        ModeloModel modelo = veiculo.getModelo();
        if (modelo != null) {
            dto.setModeloId(modelo.getId());
            dto.setNomeModelo(modelo.getNomeModelo());
            if (modelo.getMarca() != null) {
                dto.setMarcaId(modelo.getMarca().getId());
                dto.setNomeMarca(modelo.getMarca().getNomeMarca());
            }
        }

        if (proprietarioAtual != null && proprietarioAtual.getCliente() != null) {
            dto.setProprietarioAtualId(proprietarioAtual.getCliente().getId());
            dto.setDataInicioPosse(proprietarioAtual.getDataInicioPosse());
            dto.setObservacaoPosse(proprietarioAtual.getObservacao());
            if (proprietarioAtual.getCliente().getPessoa() != null) {
                dto.setNomeProprietarioAtual(proprietarioAtual.getCliente().getPessoa().getNome());
            }
        }
    }
}
