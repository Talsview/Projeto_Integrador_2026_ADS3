package br.com.avcar.oficina.core.estrutura.fila;

import br.com.avcar.oficina.core.estrutura.iterator.OficinaIterator;

/**
 * Estrutura de Dados Linear: Fila.
 *
 * Aplicação no sistema: gerenciar Ordens de Serviço aguardando atendimento,
 * respeitando a ordem de chegada dos veículos à oficina.
 *
 * Justificativa: a fila é adequada para este cenário porque o primeiro veículo
 * que entra aguardando atendimento deve ser o primeiro a aparecer na gestão
 * operacional da oficina.
 */
public class FilaAtendimento<T> {

    private NoFila<T> inicio;
    private NoFila<T> fim;
    private int tamanho;

    public void enfileirar(T valor) {
        NoFila<T> novo = new NoFila<>(valor);
        if (estaVazia()) {
            inicio = novo;
            fim = novo;
        } else {
            fim.setProximo(novo);
            fim = novo;
        }
        tamanho++;
    }

    public T desenfileirar() {
        if (estaVazia()) {
            return null;
        }
        T valor = inicio.getValor();
        inicio = inicio.getProximo();
        if (inicio == null) {
            fim = null;
        }
        tamanho--;
        return valor;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int tamanho() {
        return tamanho;
    }

    public OficinaIterator<T> iterator() {
        return new FilaAtendimentoIterator<>(inicio);
    }
}
