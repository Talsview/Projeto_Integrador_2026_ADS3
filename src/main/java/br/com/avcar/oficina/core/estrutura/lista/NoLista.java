package br.com.avcar.oficina.core.estrutura.lista;

class NoLista<T> {

    private final T valor;
    private NoLista<T> proximo;

    NoLista(T valor) {
        this.valor = valor;
    }

    T getValor() {
        return valor;
    }

    NoLista<T> getProximo() {
        return proximo;
    }

    void setProximo(NoLista<T> proximo) {
        this.proximo = proximo;
    }
}
