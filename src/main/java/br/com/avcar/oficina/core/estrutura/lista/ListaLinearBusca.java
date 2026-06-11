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
/**
 * PADRÃO DE PROJETO: ITERATOR / LISTA LINEAR.
 *
 * Função no sistema: aplica estrutura linear própria para busca e percurso manual dos dados.
 * Justificativa funcional: este código participa de uma funcionalidade real do sistema e
 * evita que o padrão seja usado apenas como exemplo sem utilidade prática.
 */
public class ListaLinearBusca<T> {

    private NoLista<T> inicio;
    private NoLista<T> fim;
    private int tamanho;

    /**
     * Função: Insere um elemento na lista linear customizada.
     * Uso no sistema: alimenta a estrutura usada nas demonstrações acadêmicas de busca e ordenação.
     */
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

    /**
     * Função: Localiza informações de módulo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
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
        return new ListaLinearIterator<>(inicio);
    }
}
