# Etapa 10 — Estrutura de Dados I aplicada à Ordem de Serviço

## 1. Objetivo da etapa

Esta etapa implementa as exigências da unidade curricular de Estrutura de Dados I dentro do sistema de controle da oficina mecânica AV CAR AUTO CENTER.

A aplicação foi feita sobre o módulo de Ordem de Serviço, pois a OS é o centro operacional do sistema e reúne cliente, veículo, serviços, peças, fornecedor, garantias e pagamentos.

---

## 2. Recursos implementados

Foram implementados quatro recursos principais:

```text
1. Estrutura de Dados Linear: Fila de Atendimento de OS.
2. Estrutura de Dados Linear: Lista Encadeada para pesquisa manual.
3. Algoritmo de Ordenação manual: Insertion Sort.
4. Função Recursiva: cálculo do total da OS.
```

Além disso, esta etapa também aplica dois padrões de projeto exigidos pela disciplina:

```text
Iterator
Template Method
```

---

## 3. Estrutura de Dados Linear — Fila

### 3.1 Onde foi utilizada

A fila foi utilizada no endpoint:

```text
GET /api/estrutura-dados/ordens-servico/fila-atendimento
```

### 3.2 Classes principais

```text
src/main/java/br/com/avcar/oficina/core/estrutura/fila/FilaAtendimento.java
src/main/java/br/com/avcar/oficina/core/estrutura/fila/FilaAtendimentoIterator.java
src/main/java/br/com/avcar/oficina/core/estrutura/fila/NoFila.java
```

### 3.3 Justificativa

A fila é adequada para o controle de atendimento porque preserva o princípio FIFO, ou seja, o primeiro veículo que chega aguardando atendimento deve aparecer primeiro na gestão operacional da oficina.

No sistema, a fila considera as Ordens de Serviço em status `ORCAMENTO`, pois elas representam atendimentos ainda aguardando aprovação, triagem ou início operacional.

---

## 4. Padrão de Projeto Iterator

### 4.1 Onde foi aplicado

O padrão Iterator foi aplicado nas estruturas lineares:

```text
OficinaIterator
FilaAtendimentoIterator
ListaLinearIterator
```

### 4.2 Justificativa

O Iterator permite percorrer a fila e a lista sem expor os nós internos das estruturas. Assim, a camada de serviço consegue navegar pelos elementos sem conhecer detalhes de implementação.

### 4.3 Comentário no código

As classes possuem comentários explícitos informando:

```text
PADRÃO DE PROJETO: ITERATOR
```

---

## 5. Pesquisa com Estrutura de Dados

### 5.1 Onde foi utilizada

A pesquisa linear foi disponibilizada no endpoint:

```text
GET /api/estrutura-dados/ordens-servico/pesquisar-linear?termo=valor
```

### 5.2 Classe principal

```text
src/main/java/br/com/avcar/oficina/core/estrutura/lista/ListaLinearBusca.java
```

### 5.3 Critérios pesquisados

A pesquisa manual percorre as Ordens de Serviço e compara o termo informado com:

```text
Número da OS
Nome do cliente
Placa do veículo
Descrição do veículo
Prioridade
Status atual
```

### 5.4 Justificativa

A busca linear é suficiente para demonstrar a aplicação acadêmica de estrutura de dados e permite localizar rapidamente uma OS por dados usados na rotina da oficina.

---

## 6. Algoritmo de Ordenação Manual

### 6.1 Onde foi utilizado

A ordenação manual foi disponibilizada no endpoint:

```text
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=DATA_ABERTURA
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=VALOR_TOTAL
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=PRIORIDADE
```

### 6.2 Algoritmo implementado

Foi implementado o algoritmo:

```text
Insertion Sort
```

Sem uso de:

```text
Collections.sort
Stream.sorted
bibliotecas prontas de ordenação
```

### 6.3 Classes principais

```text
src/main/java/br/com/avcar/oficina/core/estrutura/ordenacao/OrdenadorTemplate.java
src/main/java/br/com/avcar/oficina/business/ordemservico/estrutura/ordenacao/OrdenadorOrdemServicoPorDataAbertura.java
src/main/java/br/com/avcar/oficina/business/ordemservico/estrutura/ordenacao/OrdenadorOrdemServicoPorValorTotal.java
src/main/java/br/com/avcar/oficina/business/ordemservico/estrutura/ordenacao/OrdenadorOrdemServicoPorPrioridade.java
```

---

## 7. Padrão de Projeto Template Method

### 7.1 Onde foi aplicado

O padrão Template Method foi aplicado em:

```text
OrdenadorTemplate<T>
```

### 7.2 Justificativa

A classe abstrata define o esqueleto fixo do algoritmo de ordenação manual. As subclasses alteram apenas o critério de comparação.

Exemplo:

```text
OrdenadorOrdemServicoPorDataAbertura
OrdenadorOrdemServicoPorValorTotal
OrdenadorOrdemServicoPorPrioridade
```

Dessa forma, o algoritmo principal permanece estável, enquanto os critérios de ordenação podem variar.

---

## 8. Função Recursiva

### 8.1 Onde foi utilizada

A função recursiva foi disponibilizada no endpoint:

```text
GET /api/estrutura-dados/ordens-servico/{idOrdemServico}/total-recursivo
```

### 8.2 Classe principal

```text
src/main/java/br/com/avcar/oficina/business/ordemservico/estrutura/service/CalculadoraRecursivaTotalOrdemServico.java
```

### 8.3 Justificativa

A recursividade foi aplicada ao cálculo do valor total da OS, somando sequencialmente os itens de serviço e os itens de peça.

A solução é coerente com o domínio da oficina, pois o valor total da OS depende da soma de múltiplos itens vinculados ao atendimento.

---

## 9. Controller criado

```text
src/main/java/br/com/avcar/oficina/business/ordemservico/estrutura/controller/EstruturaDadosOrdemServicoController.java
```

Endpoints:

```text
GET /api/estrutura-dados/ordens-servico/fila-atendimento
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=DATA_ABERTURA
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=VALOR_TOTAL
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=PRIORIDADE
GET /api/estrutura-dados/ordens-servico/pesquisar-linear?termo=PQX
GET /api/estrutura-dados/ordens-servico/{idOrdemServico}/total-recursivo
```

---

## 10. Contribuição para o controle da oficina

A implementação desta etapa contribui para o sistema porque:

```text
1. Organiza as OS em orçamento por ordem de chegada.
2. Permite localizar OS por placa, cliente, número ou status.
3. Permite ordenar OS por data, valor ou prioridade.
4. Demonstra cálculo total da OS por recursividade.
5. Gera evidências técnicas claras para a avaliação acadêmica.
6. Reforça a rastreabilidade operacional do sistema.
```

---

## 11. Status da etapa

```text
Etapa 10 concluída.
```

A próxima etapa recomendada é implementar o padrão Decorator em uma estrutura de notificação/auditoria da API e, em seguida, iniciar a preparação da camada Angular.
