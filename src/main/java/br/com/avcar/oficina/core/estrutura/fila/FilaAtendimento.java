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
/**
 * PADRÃO DE PROJETO: ITERATOR / FILA.
 *
 * Função no sistema: aplica estrutura linear FIFO para representar a fila de atendimento da oficina.
 * Justificativa funcional: este código participa de uma funcionalidade real do sistema e
 * evita que o padrão seja usado apenas como exemplo sem utilidade prática.
 */
public class FilaAtendimento<T> {

    private NoFila<T> inicio;
    private NoFila<T> fim;
    private int tamanho;

    /**
     * Função: Insere um novo elemento no final da fila de atendimento.
     * Uso no sistema: representa a entrada de OS em execução aguardando atendimento.
     */
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

    /**
     * Função: Remove e retorna o primeiro elemento da fila.
     * Uso no sistema: representa o avanço do atendimento seguindo a ordem de chegada.
     */
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

    /**
     * Função: Verifica se a estrutura não possui elementos.
     * Uso no sistema: permite tratar telas e operações quando não há registros na fila ou lista.
     */
    public boolean estaVazia() {
        return tamanho == 0;
    }

    /**
     * Função: Retorna a quantidade atual de elementos armazenados na estrutura.
     * Uso no sistema: exibe ou valida o volume de itens na fila/lista sem acessar detalhes internos.
     */
    public int tamanho() {
        return tamanho;
    }

    /**
     * Função: Cria o iterador usado para percorrer a estrutura sem expor sua implementação interna.
     * Uso no sistema: aplica o padrão Iterator na fila e na lista linear do projeto.
     */
    public OficinaIterator<T> iterator() {
        return new FilaAtendimentoIterator<>(inicio);
    }
}
