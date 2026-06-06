package br.com.avcar.oficina.core.estrutura.fila;

import br.com.avcar.oficina.core.estrutura.iterator.OficinaIterator;
import java.util.NoSuchElementException;

/**
 * PADRÃO DE PROJETO: ITERATOR.
 *
 * Aplicação: percorre a FilaAtendimento sem permitir que Controller ou Service
 * conheçam os nós internos da estrutura.
 */
public class FilaAtendimentoIterator<T> implements OficinaIterator<T> {

    private final NoFila<T> inicio;
    private NoFila<T> atual;

    FilaAtendimentoIterator(NoFila<T> inicio) {
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
            throw new NoSuchElementException("Não há próximo elemento na fila de atendimento.");
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
