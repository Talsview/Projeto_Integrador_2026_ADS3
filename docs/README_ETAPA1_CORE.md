# Etapa 1 — Refatoração do Core Genérico

## Objetivo

Esta etapa prepara o projeto para funcionar como uma API REST monolítica em camadas, mantendo coerência com o Projeto Integrador da oficina mecânica AV CAR AUTO CENTER.

## Alterações realizadas

```text
1. Padronização do pacote base para br.com.avcar.oficina.
2. Renomeação da aplicação principal para OficinaApplication.
3. Troca do identificador UUID para Long.
4. Refatoração do BaseModel.
5. Refatoração do BaseDTO.
6. Refatoração do IGenericRepository para JpaRepository<E, Long>.
7. Refatoração do IGenericService e GenericService para Long.
8. Refatoração do IGenericValidation e GenericValidation para Long.
9. Criação da camada Response.
10. Criação do ApiResponse.
11. Criação do ErrorResponse.
12. Criação do PageResponse.
13. Criação do GlobalExceptionHandler.
14. Refatoração do GenericController para respostas REST padronizadas.
15. Configuração de CORS para Angular em http://localhost:4200.
16. Configuração do OpenAPI/Swagger.
17. Alteração do ddl-auto para validate.
18. Criação dos scripts SQL iniciais do modelo físico.
19. Aplicação inicial do padrão Singleton para verificação de conexão local com o banco.
```

## Arquitetura aplicada

```text
Angular
    ↓ HTTP/JSON
Controller
    ↓
Response / DTO
    ↓
Service
    ↓
Validation
    ↓
Mapper
    ↓
Repository
    ↓
PostgreSQL local
```

## Estrutura principal

```text
src/main/java/br/com/avcar/oficina
├── OficinaApplication.java
├── core
│   ├── config
│   ├── controller
│   ├── database
│   ├── designpattern
│   ├── dto
│   ├── exception
│   ├── mapper
│   ├── model
│   ├── repository
│   ├── response
│   ├── service
│   └── validation
├── business
│   ├── garantia
│   ├── ordemservico
│   ├── pagamento
│   ├── peca
│   ├── pessoa
│   ├── servico
│   └── veiculo
└── view
    └── api
```

## Padrão de projeto já iniciado

### Singleton

Classe:

```text
src/main/java/br/com/avcar/oficina/core/designpattern/singleton/DatabaseConnectionSingleton.java
```

Aplicação: ponto único para verificação de conexão local com o PostgreSQL.

Justificativa acadêmica: como o sistema da oficina deve funcionar localmente, a verificação de conexão com o banco precisa ficar centralizada e reutilizável.

## Próxima etapa recomendada

Implementar o módulo Pessoa/Cliente:

```text
Pessoa
Cliente
PessoaFisica
PessoaJuridica
ClienteFactory
PessoaMapper
ClienteMapper
ClienteController
```

Essa etapa também permitirá aplicar o padrão Factory Method de forma coerente na criação de Cliente Pessoa Física e Cliente Pessoa Jurídica.
