package br.com.avcar.oficina.business.ordemservico.estrutura.ordenacao;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.core.estrutura.ordenacao.OrdenadorTemplate;
import java.math.BigDecimal;

/**
 * TEMPLATE METHOD: ordena manualmente as Ordens de Serviço por maior valor total.
 */
/**
 * PADRÃO DE PROJETO: TEMPLATE METHOD.
 *
 * Função no sistema: reutiliza o mesmo algoritmo manual para ordenar ordens de serviço por data, valor ou prioridade.
 * Justificativa funcional: este código participa de uma funcionalidade real do sistema e
 * evita que o padrão seja usado apenas como exemplo sem utilidade prática.
 */
public class OrdenadorOrdemServicoPorValorTotal extends OrdenadorTemplate<OrdemServicoResumoDTO> {

    @Override
    /**
     * Função: Define o critério específico usado para comparar duas Ordens de Serviço durante a
     * ordenação.
     * Padrão aplicado: TEMPLATE METHOD.
     * Justificativa: permite ordenar por data, valor ou prioridade sem duplicar o algoritmo de
     * ordenação.
     * Uso no sistema: atende à ordenação manual exigida no projeto e organiza diferentes critérios de
     * consulta de OS.
     */
    protected int comparar(OrdemServicoResumoDTO anterior, OrdemServicoResumoDTO atual) {
        BigDecimal valorAnterior = anterior.getValorTotal() == null ? BigDecimal.ZERO : anterior.getValorTotal();
        BigDecimal valorAtual = atual.getValorTotal() == null ? BigDecimal.ZERO : atual.getValorTotal();
        return valorAtual.compareTo(valorAnterior);
    }
}
