# Etapa 49B — Correção de Build na Exportação de Planilha

## Objetivo
Corrigir erro de compilação TypeScript na tela de Relatórios após a implementação da exportação para planilha Excel.

## Problema identificado
O Angular não conseguiu inferir corretamente o tipo do item de garantia no método de exportação, pois a lista de garantias usa união de tipos:

```ts
GarantiaPeca | GarantiaServico
```

O acesso direto a `nomeServico` em uma expressão ternária com `'nomePeca' in g` não foi aceito pelo compilador em modo estrito.

## Correção aplicada
Foi criado um type guard explícito:

```ts
private ehGarantiaPeca(garantia: GarantiaPeca | GarantiaServico): garantia is GarantiaPeca
```

Também foi criado o método:

```ts
obterNomeGarantia(garantia: GarantiaPeca | GarantiaServico): string
```

Com isso, tanto a exportação quanto a tabela de relatório passam a diferenciar garantia de peça e garantia de serviço sem erro de tipagem.

## Arquivos alterados

```text
frontend/oficina-web/src/app/pages/relatorios/relatorios.component.ts
frontend/oficina-web/src/app/pages/relatorios/relatorios.component.html
```
