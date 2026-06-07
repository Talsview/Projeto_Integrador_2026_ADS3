# Etapa 51 — Fluxo de OS limitado até Pagamento na tela de Ordens de Serviço

## Objetivo

Corrigir a tela de Ordens de Serviço para que ela avance o fluxo operacional somente até a etapa **PAGAMENTO**.

A etapa **FINALIZADO** não deve ser acionada manualmente pela tela de Ordens de Serviço, pois depende da quitação financeira e deve ocorrer pelo módulo **Pagamentos**.

## Regra aplicada

Fluxo operacional da tela de OS:

```text
ORÇAMENTO → EXECUÇÃO → PAGAMENTO
```

Finalização automática pelo módulo Pagamentos:

```text
PAGAMENTO + OS quitada → FINALIZADO
```

## Alterações realizadas

- A tela de OS não mostra mais o botão para avançar quando a OS está em PAGAMENTO.
- Quando a OS está em PAGAMENTO, a tela orienta o usuário a finalizar pelo módulo Pagamentos.
- O backend passou a bloquear tentativa manual de envio para FINALIZADO pelo endpoint operacional da OS.
- O módulo Pagamentos continua podendo finalizar automaticamente a OS após quitação.

## Arquivos alterados

```text
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html
src/main/java/br/com/avcar/oficina/business/ordemservico/service/OrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/ordemservico/controller/OrdemServicoController.java
```
