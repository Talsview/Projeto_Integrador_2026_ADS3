# Sistema de Gestão da Oficina Mecânica AV CAR AUTO CENTER

## Situação desta versão

Esta versão corresponde à **Etapa 3 — Módulo Colaborador, Função e ColaboradorFunção**.

O projeto está sendo refatorado para funcionar como uma API REST em Spring Boot, com frontend Angular como camada View e PostgreSQL local como banco de dados.

## Principais decisões técnicas

```text
Estilo arquitetural: Monólito modular
Backend: Java 21 + Spring Boot
Frontend previsto: Angular
Banco de dados: PostgreSQL local
Documentação da API: Swagger/OpenAPI
Identificadores: Long
Criação do banco: script SQL manual/versionado
Persistência: Spring Data JPA com ddl-auto=validate
```

## Camadas adotadas

```text
Angular View
    ↓
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
PostgreSQL
```

## Como executar

1. Criar o banco `car_repair` no PostgreSQL.
2. Executar os scripts:

```text
database/01_create_schema.sql
database/02_seed_inicial.sql
```

3. Conferir usuário e senha em:

```text
src/main/resources/application.properties
```

4. Executar a aplicação pelo NetBeans ou terminal:

```bash
mvn spring-boot:run
```

5. Acessar o Swagger:

```text
http://localhost:9081/swagger-ui.html
```

6. Testar o endpoint de verificação do banco:

```text
GET http://localhost:9081/api/database/status
```

---

# Etapa 1 — Core Genérico

Foi criada a base técnica da API REST:

```text
BaseModel
BaseDTO
ApiResponse
ErrorResponse
PageResponse
GenericController
GenericService
GenericValidation
GenericRepository
Configuração de CORS
Configuração do Swagger/OpenAPI
DatabaseConnectionSingleton
DatabaseStatusController
```

Padrão de projeto aplicado:

```text
Singleton → DatabaseConnectionSingleton
```

Documentação:

```text
docs/README_ETAPA1_CORE.md
docs/adr/ADR-001-angular-como-view.md
docs/adr/ADR-002-uso-long-como-identificador.md
docs/adr/ADR-003-banco-por-script-sql.md
docs/adr/ADR-004-monolito-em-camadas.md
```

---

# Etapa 2 — Módulo Pessoa e Cliente

Foram implementados:

```text
Pessoa
Cliente
PessoaFisica
PessoaJuridica
```

Padrão de projeto aplicado:

```text
Factory Method → cadastro de Cliente Pessoa Física e Pessoa Jurídica
```

Endpoints principais:

```text
POST   /api/clientes/pessoa-fisica
POST   /api/clientes/pessoa-juridica
PUT    /api/clientes/pessoa-fisica/{id}
PUT    /api/clientes/pessoa-juridica/{id}
GET    /api/clientes/{id}
GET    /api/clientes
GET    /api/clientes/pesquisar?termo=valor
DELETE /api/clientes/{id}
```

Documentação:

```text
docs/README_ETAPA2_CLIENTES.md
docs/adr/ADR-005-factory-method-cliente.md
```

---

# Etapa 3 — Módulo Colaborador, Função e ColaboradorFunção

Foram implementados:

```text
Colaborador
Funcao
ColaboradorFuncao
```

Regra acadêmica atendida:

```text
Mecânico, Atendente, Secretária, Faxineiro, Estoquista e Gerente são registros de Funcao,
não entidades separadas.
```

Endpoints de função:

```text
POST   /api/funcoes
PUT    /api/funcoes/{id}
GET    /api/funcoes/{id}
GET    /api/funcoes
GET    /api/funcoes/pesquisar?termo=valor
DELETE /api/funcoes/{id}
```

Endpoints de colaborador:

```text
POST   /api/colaboradores
PUT    /api/colaboradores/{id}
GET    /api/colaboradores/{id}
GET    /api/colaboradores
GET    /api/colaboradores/pesquisar?termo=valor
DELETE /api/colaboradores/{id}
```

Documentação:

```text
docs/README_ETAPA3_COLABORADORES.md
docs/adr/ADR-006-funcao-como-registro.md
```

---

## Próxima etapa recomendada

```text
Etapa 4 — Marca, Modelo, Veiculo e HistoricoProprietario
```

Essa próxima etapa permitirá ligar clientes a veículos, preservar o histórico de proprietários e preparar a base para a Ordem de Serviço.
