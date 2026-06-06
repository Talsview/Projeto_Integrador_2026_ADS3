# Frontend Angular — Oficina AV CAR AUTO CENTER

Este diretório contém a primeira estrutura da camada **View** do sistema, implementada com Angular e preparada para consumir a API REST Spring Boot do backend.

## Objetivo desta etapa

A Etapa 12 não substitui o backend. Ela cria a base do frontend, organiza os serviços HTTP, define os modelos TypeScript compatíveis com os DTOs Java e disponibiliza telas iniciais para validação da integração com Swagger/API REST.

## Requisitos locais

```bash
node --version
npm --version
```

Para Angular 22, utilizar Node.js compatível com a tabela oficial do Angular. Recomenda-se Node.js 22.22.3 ou superior dentro da série 22, ou versão compatível informada na documentação oficial.

## Instalação

```bash
cd frontend/oficina-web
npm install
npm start
```

A aplicação será aberta em:

```text
http://localhost:4200
```

## Execução com proxy opcional

```bash
npm run start:proxy
```

## Backend esperado

```text
http://localhost:9081
```

Swagger:

```text
http://localhost:9081/swagger-ui.html
```

## Estrutura criada

```text
src/app/core
src/app/models
src/app/pages/dashboard
src/app/pages/clientes
src/app/pages/veiculos
src/app/pages/ordens-servico
src/app/pages/padroes-projeto
src/app/pages/estrutura-dados
```

## Integrações iniciais

```text
GET /api/database/status
GET /api/padroes-projeto
GET /api/clientes
GET /api/clientes/pesquisar
POST /api/clientes/pessoa-fisica
POST /api/clientes/pessoa-juridica
GET /api/veiculos
GET /api/veiculos/pesquisar
GET /api/ordens-servico
GET /api/ordens-servico/pesquisar
PATCH /api/ordens-servico/{id}/status
GET /api/estrutura-dados/ordens-servico/fila-atendimento
GET /api/estrutura-dados/ordens-servico/ordenar
GET /api/estrutura-dados/ordens-servico/pesquisar-linear
GET /api/estrutura-dados/ordens-servico/{id}/total-recursivo
```

## Observação acadêmica

A camada View passa a ser representada pelo Angular. O backend mantém a arquitetura monolítica em camadas e expõe os recursos por Controller REST, Response e DTO.
