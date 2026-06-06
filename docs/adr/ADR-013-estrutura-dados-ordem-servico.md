# ADR-013 — Aplicação de Estrutura de Dados I em Ordem de Serviço

## Status

Aceita.

## Contexto

O Projeto Integrador exige que o sistema aplique uma estrutura de dados linear, um algoritmo de ordenação manual, uma funcionalidade de pesquisa e uma função recursiva coerente com o domínio desenvolvido.

Como a Ordem de Serviço é o centro operacional do sistema da oficina, concentrando cliente, veículo, serviços, peças, garantias e pagamentos, decidiu-se aplicar os recursos de Estrutura de Dados I nesse módulo.

## Decisão

Foram adotadas as seguintes soluções:

```text
FilaAtendimento: estrutura linear para OS em orçamento.
ListaLinearBusca: estrutura linear para pesquisa manual de OS.
Insertion Sort: algoritmo manual de ordenação.
CalculadoraRecursivaTotalOrdemServico: função recursiva para totalização da OS.
Iterator: padrão aplicado ao percurso das estruturas lineares.
Template Method: padrão aplicado ao algoritmo manual de ordenação.
```

## Justificativa

A fila representa corretamente a ordem de chegada dos atendimentos. A lista linear demonstra pesquisa manual por dados relevantes da OS. O Insertion Sort atende à exigência de ordenação sem uso de bibliotecas prontas. A recursividade foi aplicada ao cálculo do total da OS, uma operação real do sistema.

## Consequências

A implementação gera evidências diretas para a documentação acadêmica e para a apresentação do sistema, sem interferir negativamente na persistência principal do banco de dados.

Além disso, a solução mantém a arquitetura em camadas, pois a lógica fica isolada em classes de estrutura e em um service específico:

```text
Controller → Service → Estruturas/Algoritmos → Repository
```
