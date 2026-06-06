# ADR-023 — Correção da herança dos services Angular especializados

## Status

Aceita.

## Contexto

Durante a execução do frontend Angular no VS Code, foi identificado erro de compilação nos services `OrdemServicoApiService` e `PagamentoApiService`.

Esses services estendem `BaseApiService`, mas declaravam novamente a propriedade `tempoLimiteMs`, que já existia na classe base como atributo privado.

## Decisão

Foi decidido centralizar a propriedade `tempoLimiteMs` em `BaseApiService` como `protected`, permitindo o uso pelas subclasses sem redeclaração.

As declarações duplicadas foram removidas dos services especializados.

## Consequências

- Elimina o erro de compilação TS2415.
- Elimina o erro de compilação TS4114.
- Mantém o timeout padronizado para chamadas HTTP.
- Reduz duplicidade de código.
- Mantém coerência com a proposta de uso de generics e reaproveitamento de estrutura.
