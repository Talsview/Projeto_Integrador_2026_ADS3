# ADR-066 — Correção do método atualizarTela na tela de Pagamentos

## Status

Aceita.

## Contexto

Após a etapa de reativação de registros inativos e melhoria dos comentários, o componente `PagamentosComponent` passou a chamar `this.atualizarTela()` em operações assíncronas. Entretanto, o método não havia sido declarado na classe, causando erro de compilação TypeScript.

## Decisão

Adicionar o método privado `atualizarTela()` ao componente de Pagamentos, centralizando a chamada de `ChangeDetectorRef.detectChanges()`.

## Consequências

A tela de Pagamentos passa a compilar corretamente e mantém o mesmo padrão visual usado em outras telas do sistema. A mudança é restrita ao frontend e não interfere nas regras de negócio, no banco de dados ou no backend.
