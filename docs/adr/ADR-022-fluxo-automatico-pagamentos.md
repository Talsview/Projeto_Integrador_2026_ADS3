# ADR-022 — Automatização do Fluxo Financeiro da Ordem de Serviço

## Status

Aceita.

## Contexto

A tela de Pagamentos exigia que o usuário avançasse manualmente a Ordem de Serviço até a etapa PAGAMENTO antes de registrar o pagamento. Na prática operacional da oficina, isso prejudicava a usabilidade e causava erro de regra de negócio quando o usuário tentava salvar diretamente o pagamento.

## Decisão

Foi decidido que o backend será responsável por conduzir automaticamente a OS até a etapa PAGAMENTO quando um pagamento for registrado. Caso o pagamento quite o valor total da OS, o backend também finalizará automaticamente a Ordem de Serviço.

## Consequências

```text
1. A regra de fluxo continua preservada.
2. O HistoricoStatusOrdem registra as transições automáticas.
3. A tela de Pagamentos fica mais simples e mais intuitiva.
4. O resumo financeiro passa a ser atualizado automaticamente.
5. As garantias continuam iniciando somente após a finalização da OS.
```

## Justificativa acadêmica

A decisão mantém a rastreabilidade, a integridade e o histórico, pois nenhuma etapa é ignorada. O que muda é a responsabilidade pela transição: em vez de exigir vários cliques do usuário, o service executa o fluxo correto e registra cada status.
