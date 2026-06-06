# Etapa 18 — Correção da tela de Pagamentos e tratamento do status da OS

## 1. Objetivo

Esta etapa corrige o comportamento da tela de **Pagamentos** no Angular, evitando que o usuário tente registrar pagamento em uma Ordem de Serviço que ainda não chegou à etapa **PAGAMENTO** do fluxo operacional.

A regra de negócio do projeto permanece preservada:

```text
Orçamento → Execução → Pagamento → Finalizado
```

Portanto, o pagamento só pode ser registrado quando a OS estiver no status `PAGAMENTO`.

## 2. Problema identificado

Na tela de Pagamentos, todas as Ordens de Serviço eram listadas sem deixar evidente o status atual. Assim, o usuário selecionava uma OS em `ORCAMENTO` ou `EXECUCAO`, preenchia o pagamento e recebia uma mensagem genérica de violação de regra de negócio.

Esse comportamento era tecnicamente correto no backend, mas ruim para a experiência do usuário.

## 3. Correções realizadas

Foram aplicadas as seguintes alterações:

```text
1. A tela de Pagamentos agora exibe o status atual da OS selecionada.
2. O botão Salvar pagamento fica bloqueado quando a OS não está em PAGAMENTO.
3. Foi adicionada uma mensagem orientando o usuário a avançar o fluxo da OS.
4. Foi criado botão para avançar a OS para a próxima etapa do fluxo.
5. Se a OS estiver em ORCAMENTO, o botão avança para EXECUCAO.
6. Se a OS estiver em EXECUCAO, o botão avança para PAGAMENTO.
7. Se a OS estiver em PAGAMENTO, o pagamento pode ser registrado.
8. Se a OS estiver FINALIZADO, pagamentos não podem ser alterados.
9. O interceptor Angular passou a exibir o detalhe real enviado pelo backend.
```

## 4. Arquivos alterados

```text
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.ts
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.html
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
README.md
database/README_BANCO.md
database/08_verificacao_pagamentos_status_os.sql
docs/adr/ADR-021-correcao-pagamentos-status-os.md
```

## 5. Justificativa acadêmica

A correção reforça a integridade do fluxo de negócio da Ordem de Serviço e melhora a usabilidade da camada View em Angular. O backend continua responsável por proteger a regra de domínio, enquanto o frontend passa a orientar o usuário antes que a operação inválida seja enviada à API.

## 6. Como testar

1. Criar uma Ordem de Serviço.
2. Adicionar pelo menos um ItemServico.
3. Ir para a tela de Pagamentos.
4. Selecionar a OS.
5. Clicar em **Avançar fluxo da OS** até o status chegar em `PAGAMENTO`.
6. Registrar o pagamento.
7. Conferir se o resumo financeiro foi atualizado.

Caso a OS esteja em `ORCAMENTO` e não possua ItemServico, o backend continuará impedindo o avanço para `EXECUCAO`, pois essa é uma regra válida do domínio.
