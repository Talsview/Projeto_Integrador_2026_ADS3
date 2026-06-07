# ADR-054 — Correção do fluxo guiado da Ordem de Serviço entre Execução e Pagamento

## Status
Aceito

## Data
07/06/2026

## Contexto
Após a separação das telas operacionais por status, a tela de Pagamentos passou a exibir somente Ordens de Serviço em `PAGAMENTO`. Entretanto, a tela de Ordens de Serviço ainda permitia escolha manual livre do novo status, o que gerava confusão quando uma OS já estava em `EXECUCAO` e o campo permanecia selecionado como `EXECUCAO`.

Isso impedia o uso fluido do fluxo operacional da oficina, pois o usuário esperava que a OS avançasse naturalmente para pagamento.

## Decisão
Substituir o comportamento de seleção manual livre por um avanço guiado de fluxo na tela de Ordens de Serviço.

A partir dessa decisão, o frontend calcula automaticamente a próxima etapa:

```text
ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO
```

O usuário não escolhe mais qualquer status livremente. Ele apenas confirma o avanço para a próxima etapa permitida.

## Justificativa
A Ordem de Serviço possui fluxo fechado e sequencial. Permitir escolha manual de status torna a operação mais sujeita a erro e conflita com as regras de negócio.

O avanço guiado melhora a usabilidade e preserva a rastreabilidade do processo.

## Consequências positivas
- Reduz erro operacional.
- Deixa claro quando uma OS irá para Pagamento.
- Preserva o fluxo oficial da oficina.
- Mantém compatibilidade com os filtros das telas operacionais.
- Facilita a apresentação acadêmica do sistema.

## Consequências negativas / riscos
- Reduz a flexibilidade de alteração manual de status.
- Caso seja necessário um ajuste administrativo excepcional, ele deve ser feito por rotina específica futura.

## Mitigações
O backend continua validando todas as transições, impedindo pular etapas ou retroceder status.

## Alternativas consideradas
- Manter o select livre de status.
- Criar botões separados para cada status.
- Criar uma tela exclusiva de fluxo da OS.

A opção escolhida foi o avanço guiado por ser a mais simples, segura e coerente com o fluxo da oficina.

## Follow-up
Avaliar futuramente uma tela de histórico visual da OS, mostrando linha do tempo com orçamento, execução, pagamento e finalização.
