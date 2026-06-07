# Etapa 50 — Correção do avanço de fluxo da Ordem de Serviço

## Objetivo
Corrigir o conflito identificado no avanço de status da Ordem de Serviço, especialmente quando a OS estava em **EXECUÇÃO** e deveria avançar para **PAGAMENTO**.

## Problema identificado
A tela permitia selecionar manualmente qualquer status no campo de alteração. Como o valor padrão permanecia em `EXECUCAO`, uma OS que já estava em execução poderia tentar ser confirmada novamente como `EXECUCAO`, gerando conflito de regra de negócio.

## Solução aplicada
A tela de Ordens de Serviço passou a trabalhar com avanço sequencial automático do fluxo:

```text
ORCAMENTO  → EXECUCAO
EXECUCAO   → PAGAMENTO
PAGAMENTO  → FINALIZADO
FINALIZADO → sem próxima etapa
```

## Regras preservadas
- A OS não pode pular etapas.
- A OS não pode voltar para etapa anterior.
- A OS em execução agora avança corretamente para pagamento.
- A tela de pagamentos continua exibindo somente OS em status `PAGAMENTO`.
- A finalização continua exigindo quitação financeira no backend.

## Arquivos alterados
- `frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts`
- `frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html`
- `frontend/oficina-web/src/styles.css`
