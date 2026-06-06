# ADR-020 — Correção da atualização visual do Angular após comunicação com a API

## Status

Aprovada.

## Contexto

Durante os testes do frontend Angular, foi identificado que algumas telas só atualizavam visualmente após o usuário executar outra ação, como clicar em outro botão.

Esse comportamento era perceptível ao cadastrar clientes e consultar listas. O registro era salvo no backend, mas o botão permanecia visualmente em estado de processamento e a tabela só era redesenhada após novo clique.

## Decisão

Foi decidido ajustar o interceptor global da API para garantir que todos os eventos HTTP retornem para dentro do `NgZone` do Angular.

Dessa forma, a detecção de mudanças do Angular é acionada corretamente após:

```text
1. Resposta de sucesso da API.
2. Resposta de erro da API.
3. Finalização da requisição.
```

Também foi configurado explicitamente o `provideZoneChangeDetection` no `app.config.ts`.

## Justificativa

A solução foi escolhida porque corrige o problema de forma global, evitando replicar `ChangeDetectorRef.detectChanges()` manualmente em todos os componentes.

A abordagem mantém a arquitetura do frontend mais limpa e centraliza a responsabilidade de comunicação assíncrona no interceptor da API.

## Consequências positivas

```text
1. Botões deixam de ficar presos em estado de processamento.
2. Tabelas atualizam automaticamente após salvar, editar, excluir ou acionar registros.
3. O frontend passa a refletir imediatamente o retorno do backend.
4. A solução atende todas as telas sem duplicação excessiva de código.
```

## Consequências negativas

```text
1. O interceptor passa a ter responsabilidade adicional de garantir o retorno ao ciclo de mudança visual do Angular.
2. A equipe deve manter as chamadas HTTP passando pelos serviços Angular para aproveitar a correção global.
```

## Arquivos relacionados

```text
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
frontend/oficina-web/src/app/app.config.ts
```
