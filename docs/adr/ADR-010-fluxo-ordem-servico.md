# ADR-010 — Controle de fluxo da Ordem de Serviço por Histórico de Status

## Status

Aprovada.

## Contexto

O sistema da oficina mecânica precisa controlar o andamento das Ordens de Serviço de forma rastreável. A regra de negócio consolidada define que toda OS deve seguir o fluxo:

```text
Orçamento → Execução → Pagamento → Finalizado
```

Além disso, o modelo conceitual validado define que `OrdemServico` possui `HistoricoStatusOrdem` e que `HistoricoStatusOrdem` compõe `StatusOrdemServico`.

## Decisão

Foi decidido implementar o controle de status com três entidades:

```text
OrdemServico
StatusOrdemServico
HistoricoStatusOrdem
```

O status atual da OS não será armazenado diretamente em uma coluna simples da tabela `ordem_servico`. Ele será obtido a partir do maior status registrado no `HistoricoStatusOrdem`, respeitando a ordem do fluxo.

## Justificativa

Essa decisão preserva histórico e rastreabilidade, pois permite saber quando a OS mudou de etapa e qual observação foi registrada em cada mudança.

## Consequências positivas

```text
Maior rastreabilidade do ciclo de vida da OS.
Evita perda de histórico quando o status muda.
Permite auditoria futura de alterações.
Mantém coerência com o MER validado.
Facilita início das garantias após o status FINALIZADO.
```

## Consequências negativas

```text
As consultas precisam verificar o histórico para identificar o status atual.
A lógica de mudança de status fica mais elaborada do que um simples campo status na OS.
```

## Relação com as regras de negócio

```text
RN10 — Toda OS deve possuir histórico de status.
RN11 — OS segue: Orçamento, Execução, Pagamento e Finalizado.
RN26 — Garantia da peça começa após finalizar a OS.
RN27 — Garantia do serviço varia conforme o tipo de serviço.
```
