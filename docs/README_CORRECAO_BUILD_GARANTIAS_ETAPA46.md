# Correção de build - Garantias Etapa 46

## Problema corrigido

O Angular apresentava erro de compilação na tela de Garantias:

```text
TS2554: Expected 0 arguments, but got 2.
TS2349: This expression is not callable.
```

O erro ocorria porque o método `confirmarAtendimentoGarantia()` montava uma variável com retorno em união de tipos (`Observable<GarantiaPeca>` ou `Observable<GarantiaServico>`). O compilador do Angular/TypeScript não conseguiu inferir corretamente a assinatura de `pipe()`, `switchMap()` e `subscribe()` nessa união.

## Correção aplicada

Foi adicionada tipagem explícita para a ação da garantia e para o método de recarregamento:

```typescript
const acao$: Observable<GarantiaPeca | GarantiaServico> = ...

private recarregarGarantiasDoTipoAtual(): Observable<GarantiaPeca[] | GarantiaServico[]> { ... }
```

Com isso, o compilador passa a entender que a ação sempre é um `Observable` válido, independentemente de a garantia ser de peça ou de serviço.

## Arquivo alterado

```text
frontend/oficina-web/src/app/pages/garantias/garantias.component.ts
```
