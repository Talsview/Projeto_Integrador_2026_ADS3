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

    /**
     * Função: Informa se ainda existe próximo elemento a ser percorrido na estrutura linear.
     * Padrão aplicado: ITERATOR.
     * Justificativa: permite percorrer fila ou lista sem expor nós, índices internos ou detalhes da
     * implementação.
     * Uso no sistema: apoia a fila de atendimento e as estruturas lineares exigidas na disciplina de
     * Estrutura de Dados.
     */
    boolean hasNext();

    /**
     * Função: Retorna o próximo elemento da estrutura e avança o cursor de leitura.
     * Padrão aplicado: ITERATOR.
     * Justificativa: padroniza a travessia da fila/lista usada nas funcionalidades acadêmicas do
     * sistema.
     * Uso no sistema: apoia a fila de atendimento e as estruturas lineares exigidas na disciplina de
     * Estrutura de Dados.
     */
    T next();

    /**
     * Função: Participa da navegação sequencial da estrutura no passo reset.
     * Padrão aplicado: ITERATOR.
     * Justificativa: evita que outras classes acessem diretamente a organização interna da fila ou
     * lista.
     * Uso no sistema: apoia a fila de atendimento e as estruturas lineares exigidas na disciplina de
     * Estrutura de Dados.
     */
    void reset();
}
