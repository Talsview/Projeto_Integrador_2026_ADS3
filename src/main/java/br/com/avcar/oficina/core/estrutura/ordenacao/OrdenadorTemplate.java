package br.com.avcar.oficina.core.estrutura.ordenacao;

import java.util.ArrayList;
import java.util.List;

/**
 * PADRÃO DE PROJETO: TEMPLATE METHOD.
 *
 * Aplicação: define o esqueleto fixo de um algoritmo de ordenação manual por
 * inserção. As subclasses alteram apenas o critério de comparação.
 *
 * Justificativa acadêmica: atende simultaneamente ao padrão Template Method e à
 * exigência de Algoritmo de Ordenação implementado manualmente, sem uso de
 * Collections.sort, Stream.sorted ou bibliotecas prontas de ordenação.
 */
public abstract class OrdenadorTemplate<T> {

    public final List<T> ordenar(List<T> entrada) {
        List<T> lista = new ArrayList<>(entrada);

        for (int i = 1; i < lista.size(); i++) {
            T chave = lista.get(i);
            int j = i - 1;

            while (j >= 0 && comparar(lista.get(j), chave) > 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }

            lista.set(j + 1, chave);
        }

        return lista;
    }

    protected abstract int comparar(T anterior, T atual);
}
