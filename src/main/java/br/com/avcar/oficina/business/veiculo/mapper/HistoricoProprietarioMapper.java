package br.com.avcar.oficina.business.veiculo.mapper;

import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.veiculo.dto.TransferenciaProprietarioDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoDTO;
import br.com.avcar.oficina.business.veiculo.model.HistoricoProprietarioModel;
import br.com.avcar.oficina.business.veiculo.model.VeiculoModel;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável por criar registros de histórico de proprietário.
 */
@Component
public class HistoricoProprietarioMapper {

    public HistoricoProprietarioModel criarHistoricoInicial(VeiculoModel veiculo, ClienteModel cliente, VeiculoDTO dto) {
        HistoricoProprietarioModel historico = new HistoricoProprietarioModel();
        historico.setVeiculo(veiculo);
        historico.setCliente(cliente);
        historico.setDataInicioPosse(dto.getDataInicioPosse() == null ? LocalDate.now() : dto.getDataInicioPosse());
        historico.setProprietarioAtual(Boolean.TRUE);
        historico.setObservacao(dto.getObservacaoPosse());
        return historico;
    }

    public HistoricoProprietarioModel criarNovoHistorico(VeiculoModel veiculo,
                                                         ClienteModel novoCliente,
                                                         TransferenciaProprietarioDTO dto) {
        HistoricoProprietarioModel historico = new HistoricoProprietarioModel();
        historico.setVeiculo(veiculo);
        historico.setCliente(novoCliente);
        historico.setDataInicioPosse(dto.getDataInicioPosse() == null ? LocalDate.now() : dto.getDataInicioPosse());
        historico.setProprietarioAtual(Boolean.TRUE);
        historico.setObservacao(dto.getObservacao());
        return historico;
    }
}
