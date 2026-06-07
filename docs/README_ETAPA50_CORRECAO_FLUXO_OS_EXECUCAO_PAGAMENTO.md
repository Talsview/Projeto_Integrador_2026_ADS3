# Etapa 50 — Correção do fluxo da Ordem de Serviço entre Execução e Pagamento

## Objetivo
Corrigir o conflito operacional em que a Ordem de Serviço, após entrar em execução, não avançava de forma clara e segura para a etapa de pagamento.

## Problema identificado
A tela de Ordens de Serviço ainda exibia uma seleção manual de status. Como o campo podia permanecer com o mesmo status atual da OS, o usuário podia selecionar uma OS em `EXECUCAO` e tentar confirmar novamente `EXECUCAO`, gerando conflito e impedindo o avanço natural para `PAGAMENTO`.

Além disso, após a Etapa 46, as telas operacionais passaram a filtrar as OS por status correto:

- Serviços e Peças da OS: `ORCAMENTO`;
- Fila de Atendimento: `EXECUCAO`;
- Pagamentos: `PAGAMENTO`;
- Garantias: `FINALIZADO`.

Por isso, era necessário deixar o avanço de status mais explícito e automático.

## Decisão implementada
A tela de Ordens de Serviço deixou de trabalhar com escolha manual livre de status e passou a trabalhar com avanço guiado do fluxo.

Fluxo oficial mantido:

```text
ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO
```

## Alterações realizadas

### Frontend Angular
Arquivo alterado:

```text
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html
```

Foram adicionados métodos para calcular o próximo status:

```text
proximoStatusDaOrdem()
rotuloProximoStatus()
rotuloStatus()
```

Ao selecionar uma OS na listagem, o sistema calcula automaticamente a próxima etapa.

Exemplos:

```text
OS em ORCAMENTO  → botão mostra Avançar para Execução
OS em EXECUCAO   → botão mostra Avançar para Pagamento
OS em PAGAMENTO  → botão mostra Avançar para Finalizado
OS FINALIZADA    → botão fica desabilitado
```

### Regra preservada no backend
O backend continua validando o fluxo pela regra de negócio. O frontend apenas guia o usuário, mas quem garante a integridade final continua sendo o backend.

Regras mantidas:

- não permite pular etapas;
- não permite retornar status;
- não permite avançar para execução sem item de serviço;
- não permite finalizar sem pagamento quitado.

## Resultado esperado
A OS passa a avançar corretamente:

```text
1. Cadastro da OS → ORCAMENTO
2. Inclusão de serviços/peças → permanece em ORCAMENTO
3. Avançar fluxo → EXECUCAO
4. Avançar fluxo novamente → PAGAMENTO
5. Registrar pagamento na tela Pagamentos
6. Avançar fluxo → FINALIZADO
```

## Observação operacional
A tela de Pagamentos lista somente ordens em `PAGAMENTO`. Portanto, para uma OS aparecer em Pagamentos, ela precisa primeiro ser avançada de `EXECUCAO` para `PAGAMENTO` pela tela de Ordens de Serviço.
