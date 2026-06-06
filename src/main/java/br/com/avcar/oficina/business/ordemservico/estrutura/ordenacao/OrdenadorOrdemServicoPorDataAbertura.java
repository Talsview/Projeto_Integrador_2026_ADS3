package br.com.avcar.oficina.business.ordemservico.estrutura.ordenacao;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.core.estrutura.ordenacao.OrdenadorTemplate;
import java.time.LocalDateTime;

/**
 * TEMPLATE METHOD: altera apenas o critério de comparação do algoritmo manual.
 */
public class OrdenadorOrdemServicoPorDataAbertura extends OrdenadorTemplate<OrdemServicoResumoDTO> {

    @Override
    protected int comparar(OrdemServicoResumoDTO anterior, OrdemServicoResumoDTO atual) {
        LocalDateTime dataAnterior = anterior.getDataAbertura() == null ? LocalDateTime.MAX : anterior.getDataAbertura();
        LocalDateTime dataAtual = atual.getDataAbertura() == null ? LocalDateTime.MAX : atual.getDataAbertura();
        return dataAnterior.compareTo(dataAtual);
    }
}
