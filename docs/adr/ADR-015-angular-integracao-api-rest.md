# ADR-015 — Frontend Angular integrado à API REST

## Status

Aceita.

## Contexto

O Projeto Integrador exige implementação em camadas e a presença da camada View. Durante a refatoração, o backend foi estruturado como API REST em Spring Boot, expondo seus recursos por Controllers e Responses padronizadas. Com isso, tornou-se necessário criar uma interface capaz de consumir os endpoints REST e apresentar as funcionalidades da oficina de forma organizada.

## Decisão

Adotar Angular como camada View do sistema, mantendo o backend como monólito modular em Spring Boot.

A aplicação Angular ficará no diretório:

```text
frontend/oficina-web
```

A comunicação ocorrerá por HTTP/JSON com a API em:

```text
http://localhost:9081/api
```

## Consequências positivas

```text
1. Mantém a separação entre interface e regras de negócio.
2. Facilita a validação dos endpoints pelo Swagger.
3. Permite evolução visual sem alterar o domínio do backend.
4. Atende à camada View exigida no projeto.
5. Facilita a apresentação acadêmica do sistema funcionando.
```

## Consequências negativas

```text
1. Exige instalação do Node.js e dependências npm.
2. A equipe passa a manter dois ambientes de execução: backend e frontend.
3. Os DTOs Java precisam ser refletidos em interfaces TypeScript.
```

## Justificativa acadêmica

A decisão preserva a arquitetura monolítica do backend, mas melhora a organização da camada visual. O Angular consome a API REST por meio de serviços HTTP e mantém os dados trafegando em formato JSON, compatível com a proposta de Controllers, Responses e DTOs.
