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

    /**
     * Função: Soma os valores totais dos serviços lançados na Ordem de Serviço.
     * Uso no sistema: compõe o total financeiro do documento e confirma o custo da mão de obra.
     */
    public BigDecimal somarServicos(List<ItemServicoModel> itens) {
        return somarServicos(itens, 0);
    }

    /**
     * Função: Soma os valores totais das peças aplicadas na Ordem de Serviço.
     * Uso no sistema: compõe o total financeiro do documento e mantém a separação entre peças e
     * serviços.
     */
    public BigDecimal somarPecas(List<ItemPecaModel> itens) {
        return somarPecas(itens, 0);
    }

    /**
     * Função: Soma os valores totais dos serviços lançados na Ordem de Serviço.
     * Uso no sistema: compõe o total financeiro do documento e confirma o custo da mão de obra.
     */
    private BigDecimal somarServicos(List<ItemServicoModel> itens, int indice) {
        if (itens == null || indice >= itens.size()) {
            return BigDecimal.ZERO;
        }
        BigDecimal valorAtual = itens.get(indice).getValorTotal() == null
                ? BigDecimal.ZERO
                : itens.get(indice).getValorTotal();
        return valorAtual.add(somarServicos(itens, indice + 1));
    }

    /**
     * Função: Soma os valores totais das peças aplicadas na Ordem de Serviço.
     * Uso no sistema: compõe o total financeiro do documento e mantém a separação entre peças e
     * serviços.
     */
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
