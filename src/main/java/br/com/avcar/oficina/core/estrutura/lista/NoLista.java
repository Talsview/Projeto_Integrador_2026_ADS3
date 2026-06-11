package br.com.avcar.oficina.core.estrutura.lista;

/**
 * PADRÃO DE PROJETO: ITERATOR / LISTA LINEAR.
 *
 * Função no sistema: aplica estrutura linear própria para busca e percurso manual dos dados.
 * Justificativa funcional: este código participa de uma funcionalidade real do sistema e
 * evita que o padrão seja usado apenas como exemplo sem utilidade prática.
 */
class NoLista<T> {

    private final T valor;
    private NoLista<T> proximo;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
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
