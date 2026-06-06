package br.com.avcar.oficina.core.estrutura.iterator;

/**
 * PADRÃO DE PROJETO: ITERATOR.
 *
 * Aplicação: define uma forma padronizada de percorrer estruturas lineares
 * usadas no sistema da oficina, sem expor seus nós internos ou sua forma de
 * armazenamento.
 *
 * Justificativa acadêmica: atende ao padrão Iterator solicitado na disciplina e
 * permite navegar pela fila/lista de Ordens de Serviço de maneira encapsulada.
 */
public interface OficinaIterator<T> {

    boolean hasNext();

    T next();

    void reset();
}
