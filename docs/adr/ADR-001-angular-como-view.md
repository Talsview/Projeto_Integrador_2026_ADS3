# ADR-001 — Uso do Angular como camada View

## Status

Aceita.

## Contexto

O projeto anterior possuía indicações de interface desktop em Java Swing. Entretanto, a nova direção do projeto prevê o uso de API REST em Spring Boot, documentação via Swagger/OpenAPI e frontend em Angular.

## Decisão

A camada View será implementada em Angular, consumindo a API REST do backend monolítico.

## Consequências

- O backend passa a expor endpoints REST.
- A camada Controller torna-se obrigatória.
- A camada Response passa a padronizar as respostas da API.
- O Swagger/OpenAPI passa a documentar os endpoints.
- A aplicação continua monolítica no backend, mas com frontend separado.
