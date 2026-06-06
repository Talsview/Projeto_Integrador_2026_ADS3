package br.com.avcar.oficina.business.ordemservico.estrutura.ordenacao;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.core.estrutura.ordenacao.OrdenadorTemplate;
import java.math.BigDecimal;

/**
 * TEMPLATE METHOD: ordena manualmente as Ordens de Serviço por maior valor total.
 */
public class OrdenadorOrdemServicoPorValorTotal extends OrdenadorTemplate<OrdemServicoResumoDTO> {

    @Override
    protected int comparar(OrdemServicoResumoDTO anterior, OrdemServicoResumoDTO atual) {
        BigDecimal valorAnterior = anterior.getValorTotal() == null ? BigDecimal.ZERO : anterior.getValorTotal();
        BigDecimal valorAtual = atual.getValorTotal() == null ? BigDecimal.ZERO : atual.getValorTotal();
        return valorAtual.compareTo(valorAnterior);
    }
}
