# Etapa 19 — Correção do Fluxo Automático de Pagamentos e Atualização Visual

## Objetivo

Esta etapa corrige o comportamento da tela de Pagamentos e da regra financeira da Ordem de Serviço.
O usuário não precisa mais clicar manualmente em vários botões para que a tabela, o resumo financeiro e o status da OS sejam atualizados.

## Correções realizadas

```text
1. Ao selecionar uma OS, a tela carrega pagamentos, resumo financeiro e status atual.
2. Ao salvar pagamento, a lista de pagamentos é recarregada automaticamente.
3. O resumo financeiro é atualizado automaticamente após salvar, alterar status ou inativar pagamento.
4. O campo Valor pago passa a ser preenchido automaticamente com o valor pendente da OS.
5. A data de pagamento passa a ser preenchida automaticamente com a data e hora atuais.
6. O botão manual de avanço de fluxo foi removido da tela de Pagamentos.
7. O backend agora conduz a OS automaticamente até PAGAMENTO ao registrar pagamento.
8. Se o pagamento quitar a OS, o backend finaliza a OS automaticamente.
9. A finalização automática preserva o HistoricoStatusOrdem e inicia as garantias.
```

## Regra de negócio aplicada

A Ordem de Serviço continua respeitando o fluxo obrigatório:

```text
ORCAMENTO -> EXECUCAO -> PAGAMENTO -> FINALIZADO
```

A diferença é que, na tela de Pagamentos, o sistema executa as transições necessárias de forma automática, registrando cada mudança no histórico. Assim, a usabilidade melhora sem violar a regra de negócio.

## Arquivos principais alterados

```text
src/main/java/br/com/avcar/oficina/business/pagamento/service/PagamentoService.java
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.ts
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.html
frontend/oficina-web/src/app/core/services/pagamento-api.service.ts
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
```

## Resultado esperado

Ao salvar um pagamento com status PAGO:

```text
1. O valor pago é registrado.
2. A tabela de pagamentos atualiza sozinha.
3. O resumo financeiro atualiza o valor pago e o valor pendente.
4. Se o pagamento quitar a OS, o status da OS passa para FINALIZADO.
5. As garantias de peça e serviço começam após a finalização da OS.
```
