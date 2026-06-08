# Etapa 53 — Envio do orçamento para pagamento pela tela de Itens da OS

## Objetivo

Esta etapa ajusta o fluxo operacional da Ordem de Serviço para que a tela principal de **Ordens de Serviço** não execute mais o avanço manual de fluxo. A montagem do orçamento passa a acontecer na tela **Itens da Ordem de Serviço**, e, após incluir os serviços e peças necessárias, o usuário pode enviar a OS diretamente para o módulo **Pagamentos**.

## Regra aplicada

A Ordem de Serviço nasce em **ORÇAMENTO**. Enquanto estiver nessa etapa, a oficina registra serviços, peças, colaborador responsável e fornecedor das peças. Após concluir o orçamento, a própria tela de itens disponibiliza o botão **Enviar orçamento para pagamento**.

Fluxo operacional adotado na interface:

```text
ORÇAMENTO → PAGAMENTO → FINALIZADO
```

A finalização continua sendo controlada pelo módulo **Pagamentos**, após a quitação financeira da OS.

## Alterações realizadas

- Removido o botão **Fluxo** da listagem de Ordens de Serviço.
- Removido o card visual de avanço de fluxo da tela de Ordens de Serviço.
- Adicionado botão **Enviar orçamento para pagamento** na tela Itens da OS.
- O botão só fica habilitado quando existe OS selecionada e pelo menos um serviço incluído.
- O backend ganhou endpoint específico para concluir o orçamento e enviar a OS para pagamento.
- A OS deixa de aparecer na tela de Itens após ser enviada para pagamento, pois essa tela mostra somente OS em orçamento.

## Endpoint criado

```http
PATCH /api/ordens-servico/{id}/enviar-para-pagamento
```

## Arquivos alterados

```text
src/main/java/br/com/avcar/oficina/business/ordemservico/controller/OrdemServicoController.java
src/main/java/br/com/avcar/oficina/business/ordemservico/service/OrdemServicoService.java
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.html
frontend/oficina-web/src/styles.css
```
