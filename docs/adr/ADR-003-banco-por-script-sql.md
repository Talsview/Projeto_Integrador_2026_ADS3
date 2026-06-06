# ADR-003 — Banco físico criado por script SQL

## Status

Aceita.

## Contexto

O banco de dados da oficina será criado manualmente, e o repositório da aplicação deve apenas validar e manipular os registros.

## Decisão

O banco físico será criado por scripts SQL versionados na pasta `database`, e o Spring Boot será configurado com:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

## Consequências

- O Hibernate não criará tabelas automaticamente.
- O modelo físico fica explícito para entrega acadêmica.
- O script pode ser executado sempre que for necessário recriar o banco em outro computador.
- Os Models JPA deverão respeitar os nomes das tabelas e colunas definidos no script.
