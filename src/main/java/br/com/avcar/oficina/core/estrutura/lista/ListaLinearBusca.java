package br.com.avcar.oficina.core.estrutura.lista;

import br.com.avcar.oficina.core.estrutura.iterator.OficinaIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Estrutura de Dados Linear: Lista Encadeada Simples.
 *
 * Aplicação no sistema: apoiar a pesquisa manual de Ordens de Serviço por
 * número da OS, placa do veículo ou nome do cliente.
 *
 * Justificativa: a lista encadeada permite inserir os dados carregados da base
 * local e percorrê-los sequencialmente com Iterator, demonstrando a lógica de
 * pesquisa sem depender de algoritmos prontos da linguagem.
 */
public class ListaLinearBusca<T> {

    private NoLista<T> inicio;
    private NoLista<T> fim;
    private int tamanho;

    public void adicionar(T valor) {
        NoLista<T> novo = new NoLista<>(valor);
        if (inicio == null) {
            inicio = novo;
            fim = novo;
        } else {
            fim.setProximo(novo);
            fim = novo;
        }
        tamanho++;
    }

    public List<T> buscarTodos(Predicate<T> criterio) {
        List<T> encontrados = new ArrayList<>();
        OficinaIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            T valor = iterator.next();
            if (criterio.test(valor)) {
                encontrados.add(valor);
            }
        }
        return encontrados;
    }

    public int tamanho() {
        return tamanho;
    }

    public OficinaIterator<T> iterator() {
        return new ListaLinearIterator<>(inicio);
    }
}
