package br.com.avcar.oficina.core.estrutura.lista;

import br.com.avcar.oficina.core.estrutura.iterator.OficinaIterator;
import java.util.NoSuchElementException;

/**
 * PADRÃO DE PROJETO: ITERATOR.
 *
 * Aplicação: percorre uma lista linear encadeada utilizada em pesquisas
 * manuais, sem expor a estrutura interna da lista.
 */
public class ListaLinearIterator<T> implements OficinaIterator<T> {

    private final NoLista<T> inicio;
    private NoLista<T> atual;

    /**
     * Função: Cria o objeto responsável por percorrer a estrutura linear de forma controlada.
     * Padrão aplicado: ITERATOR.
     * Justificativa: mantém a estrutura protegida e fornece uma forma uniforme de leitura dos
     * elementos.
     * Uso no sistema: apoia a fila de atendimento e as estruturas lineares exigidas na disciplina de
     * Estrutura de Dados.
     */
    ListaLinearIterator(NoLista<T> inicio) {
        this.inicio = inicio;
        this.atual = inicio;
    }

    @Override
    /**
     * Função: Informa se ainda existe próximo elemento a ser percorrido na estrutura linear.
     * Padrão aplicado: ITERATOR.
     * Justificativa: permite percorrer fila ou lista sem expor nós, índices internos ou detalhes da
     * implementação.
     * Uso no sistema: apoia a fila de atendimento e as estruturas lineares exigidas na disciplina de
     * Estrutura de Dados.
     */
    public boolean hasNext() {
        return atual != null;
    }

    @Override
    /**
     * Função: Retorna o próximo elemento da estrutura e avança o cursor de leitura.
     * Padrão aplicado: ITERATOR.
     * Justificativa: padroniza a travessia da fila/lista usada nas funcionalidades acadêmicas do
     * sistema.
     * Uso no sistema: apoia a fila de atendimento e as estruturas lineares exigidas na disciplina de
     * Estrutura de Dados.
     */
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Não há próximo elemento na lista linear.");
        }
        T valor = atual.getValor();
        atual = atual.getProximo();
        return valor;
    }

    @Override
    /**
     * Função: Participa da navegação sequencial da estrutura no passo reset.
     * Padrão aplicado: ITERATOR.
     * Justificativa: evita que outras classes acessem diretamente a organização interna da fila ou
     * lista.
     * Uso no sistema: apoia a fila de atendimento e as estruturas lineares exigidas na disciplina de
     * Estrutura de Dados.
     */
    public void reset() {
        atual = inicio;
    }
}
