# Etapa 63 — Correção do método atualizarTela em Pagamentos

## Objetivo

Corrigir erro de compilação do Angular na tela de Pagamentos, onde o componente chamava o método `atualizarTela()`, mas o método não estava declarado na classe `PagamentosComponent`.

## Erro corrigido

```text
TS2339: Property 'atualizarTela' does not exist on type 'PagamentosComponent'.
```

## Arquivo alterado

```text
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.ts
```

## Solução aplicada

Foi adicionado o método privado `atualizarTela()` na classe `PagamentosComponent`, centralizando a chamada de `ChangeDetectorRef.detectChanges()`.

## Justificativa técnica

A tela de Pagamentos realiza operações assíncronas, como carregamento de inativos, ativação de registros, atualização de listas e controle de painel auxiliar. O método `atualizarTela()` mantém essas atualizações visuais em um único ponto, evitando chamadas repetidas e deixando o componente consistente com as demais telas que já utilizam o mesmo padrão.

## Impacto

- Corrige a compilação do frontend.
- Não altera regra de negócio.
- Não altera banco de dados.
- Não altera fluxo da OS.
- Não altera backend, Maven ou JDK.
