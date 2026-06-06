package br.com.avcar.oficina.core.estrutura.fila;

class NoFila<T> {

    private final T valor;
    private NoFila<T> proximo;

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
