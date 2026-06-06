# ADR-004 — Arquitetura monolítica em camadas

## Status

Aceita.

## Contexto

O Projeto Integrador solicita implementação utilizando estilo arquitetural monolítico, com separação em Model, DTO, Repository, Validation, Service, Controller, Response e View.

## Decisão

O backend será um monólito modular em Spring Boot, organizado por camadas e por módulos de negócio.

## Consequências

- A Controller não acessa Repository diretamente.
- A View Angular não acessa o banco diretamente.
- As validações ficam isoladas na camada Validation.
- As regras de negócio ficam na camada Service.
- O acesso ao banco fica concentrado em Repository.
- Os dados trafegam por DTO e Response.
