package br.com.avcar.oficina.business.ordemservico.estrutura.ordenacao;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.business.ordemservico.enums.PrioridadeOrdemServico;
import br.com.avcar.oficina.core.estrutura.ordenacao.OrdenadorTemplate;
import java.time.LocalDateTime;

/**
 * TEMPLATE METHOD: ordena manualmente por prioridade operacional da oficina.
 */
public class OrdenadorOrdemServicoPorPrioridade extends OrdenadorTemplate<OrdemServicoResumoDTO> {

    @Override
    protected int comparar(OrdemServicoResumoDTO anterior, OrdemServicoResumoDTO atual) {
        int comparacaoPrioridade = Integer.compare(peso(anterior.getPrioridade()), peso(atual.getPrioridade()));
        if (comparacaoPrioridade != 0) {
            return comparacaoPrioridade;
        }
        LocalDateTime dataAnterior = anterior.getDataAbertura() == null ? LocalDateTime.MAX : anterior.getDataAbertura();
        LocalDateTime dataAtual = atual.getDataAbertura() == null ? LocalDateTime.MAX : atual.getDataAbertura();
        return dataAnterior.compareTo(dataAtual);
    }

    private int peso(PrioridadeOrdemServico prioridade) {
        if (prioridade == null) {
            return 2;
        }
        return switch (prioridade) {
            case URGENTE -> 0;
            case ALTA -> 1;
            case NORMAL -> 2;
            case BAIXA -> 3;
        };
    }
}
