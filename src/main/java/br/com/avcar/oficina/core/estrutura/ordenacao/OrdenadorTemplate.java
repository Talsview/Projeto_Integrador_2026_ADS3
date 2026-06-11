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

    /**
     * Função: Executa o fluxo padrão de ordenação manual definido pela classe base.
     * Padrão aplicado: TEMPLATE METHOD.
     * Justificativa: o algoritmo fica em um ponto único, enquanto as subclasses alteram somente o
     * critério de comparação.
     * Uso no sistema: atende à ordenação manual exigida no projeto e organiza diferentes critérios de
     * consulta de OS.
     */
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

    /**
     * Função: Define o critério específico usado para comparar duas Ordens de Serviço durante a
     * ordenação.
     * Padrão aplicado: TEMPLATE METHOD.
     * Justificativa: permite ordenar por data, valor ou prioridade sem duplicar o algoritmo de
     * ordenação.
     * Uso no sistema: atende à ordenação manual exigida no projeto e organiza diferentes critérios de
     * consulta de OS.
     */
    protected abstract int comparar(T anterior, T atual);
}
