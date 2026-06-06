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

    ListaLinearIterator(NoLista<T> inicio) {
        this.inicio = inicio;
        this.atual = inicio;
    }

    @Override
    public boolean hasNext() {
        return atual != null;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Não há próximo elemento na lista linear.");
        }
        T valor = atual.getValor();
        atual = atual.getProximo();
        return valor;
    }

    @Override
    public void reset() {
        atual = inicio;
    }
}
