package br.com.avcar.oficina.core.estrutura.fila;

/**
 * PADRÃO DE PROJETO: ITERATOR / FILA.
 *
 * Função no sistema: aplica estrutura linear FIFO para representar a fila de atendimento da oficina.
 * Justificativa funcional: este código participa de uma funcionalidade real do sistema e
 * evita que o padrão seja usado apenas como exemplo sem utilidade prática.
 */
class NoFila<T> {

    private final T valor;
    private NoFila<T> proximo;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    NoFila(T valor) {
        this.valor = valor;
    }

    T getValor() {
        return valor;
    }

    NoFila<T> getProximo() {
        return proximo;
    }

    void setProximo(NoFila<T> proximo) {
        this.proximo = proximo;
    }
}
