# Etapa 48B — Correção de build em Relatórios e Configurações

## Objetivo
Corrigir erro de compilação TypeScript causado por quebras de linha inseridas incorretamente dentro de strings nos componentes de Relatórios e Configurações.

## Problema corrigido
O Angular apresentava erros como:

```text
TS1002: Unterminated string literal
TS1005: ',' expected
TS2554: Expected 0-1 arguments, but got 3
```

A causa era o uso de uma quebra de linha física dentro de `join('...')` em arquivos `.ts`.

## Correção aplicada
Foram corrigidos os seguintes trechos:

```ts
this.comandos.join('\n')
linhas.map(...).join('\n')
```

Também foram ajustados os caminhos Windows exibidos na tela de Configurações usando `String.raw`, evitando problemas com barras invertidas.

## Arquivos alterados

```text
frontend/oficina-web/src/app/pages/configuracoes/configuracoes.component.ts
frontend/oficina-web/src/app/pages/relatorios/relatorios.component.ts
```

## Resultado esperado
Após a correção, o Angular deve compilar normalmente com:

```powershell
npm.cmd start
```
