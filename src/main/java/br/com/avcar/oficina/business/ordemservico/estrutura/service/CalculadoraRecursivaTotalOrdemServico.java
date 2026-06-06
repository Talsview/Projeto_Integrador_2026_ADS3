package br.com.avcar.oficina.business.ordemservico.estrutura.service;

import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
import java.math.BigDecimal;
import java.util.List;

/**
 * Função Recursiva aplicada ao sistema.
 *
 * Aplicação: calcular o total da Ordem de Serviço a partir da soma recursiva
 * dos itens de serviço e dos itens de peça.
 *
 * Justificativa acadêmica: demonstra recursividade em uma funcionalidade real
 * da oficina, sem usar reduce, sum ou funções prontas de agregação.
 */
public class CalculadoraRecursivaTotalOrdemServico {

    public BigDecimal somarServicos(List<ItemServicoModel> itens) {
        return somarServicos(itens, 0);
    }

    public BigDecimal somarPecas(List<ItemPecaModel> itens) {
        return somarPecas(itens, 0);
    }

    private BigDecimal somarServicos(List<ItemServicoModel> itens, int indice) {
        if (itens == null || indice >= itens.size()) {
            return BigDecimal.ZERO;
        }
        BigDecimal valorAtual = itens.get(indice).getValorTotal() == null
                ? BigDecimal.ZERO
                : itens.get(indice).getValorTotal();
        return valorAtual.add(somarServicos(itens, indice + 1));
    }

    private BigDecimal somarPecas(List<ItemPecaModel> itens, int indice) {
        if (itens == null || indice >= itens.size()) {
            return BigDecimal.ZERO;
        }
        BigDecimal valorAtual = itens.get(indice).getValorTotal() == null
                ? BigDecimal.ZERO
                : itens.get(indice).getValorTotal();
        return valorAtual.add(somarPecas(itens, indice + 1));
    }
}
