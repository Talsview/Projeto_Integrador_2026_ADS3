# ADR-016 — Integração do Angular pelo VS Code com o Backend Spring Boot

## Status

Aceita.

## Contexto

O sistema da oficina mecânica AV CAR AUTO CENTER está sendo implementado em arquitetura monolítica modular no backend, com Spring Boot, Spring Data JPA, PostgreSQL local e API REST documentada pelo Swagger.

A camada View será desenvolvida em Angular no VS Code. Para isso, o frontend precisa consumir os endpoints do backend de forma estável durante o desenvolvimento local.

## Decisão

Foi decidido que o Angular será executado na porta padrão 4200 e que as chamadas para a API serão feitas usando o caminho relativo `/api`.

O arquivo `proxy.conf.json` do Angular redirecionará essas chamadas para:

```text
http://localhost:9081/api
```

Assim, a aplicação Angular poderá chamar:

```text
/api/clientes
```

sem precisar escrever diretamente:

```text
http://localhost:9081/api/clientes
```

## Justificativa

Essa decisão reduz problemas de CORS no ambiente de desenvolvimento, simplifica o código TypeScript e facilita a execução do projeto no VS Code.

Também mantém a separação correta entre:

```text
Frontend Angular
Backend Spring Boot REST
Banco PostgreSQL local
```

## Consequências positivas

```text
1. O frontend pode ser executado no VS Code com npm.cmd run start:proxy.
2. O backend permanece independente e testável pelo Swagger.
3. A configuração da URL da API fica centralizada no environment.
4. A integração local fica mais simples para apresentação acadêmica.
5. O caminho /api utilizado no Angular fica compatível com os controllers REST do Spring Boot.
```

## Consequências negativas

```text
1. O frontend precisa ser iniciado com start:proxy em ambiente de desenvolvimento.
2. Caso o backend mude de porta, será necessário ajustar o proxy.conf.json.
```

## Relação com os requisitos do projeto

Esta decisão atende à necessidade de implementação em camadas, separando a View Angular da API REST. Também apoia o funcionamento local do sistema, sem dependência obrigatória de internet para uso operacional da oficina.
