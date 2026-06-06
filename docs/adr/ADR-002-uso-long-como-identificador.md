# ADR-002 — Uso de Long como identificador padrão

## Status

Aceita.

## Contexto

A versão anterior do core utilizava UUID como identificador. Para simplificar a implementação acadêmica, facilitar criação manual do banco físico e alinhar com scripts SQL usando BIGSERIAL, foi definida a troca para Long.

## Decisão

Todos os identificadores principais do backend Java passam a utilizar Long.

## Consequências

- BaseModel usa Long.
- BaseDTO usa Long.
- Repositories usam JpaRepository<E, Long>.
- Services, Validations e Controllers recebem Long nos métodos de busca, atualização e exclusão.
- O modelo físico usa BIGSERIAL/BIGINT.
