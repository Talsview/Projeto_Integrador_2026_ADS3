# Sistema de Gestão da Oficina Mecânica AV CAR AUTO CENTER

## Situação desta versão

Esta versão corresponde à **Etapa 6 — Módulo Peça, Fornecedor e ItemPeca**.

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

# Etapa 4 — Módulo Marca, Modelo, Veículo e Histórico de Proprietário

Foram implementados:

```text
Marca
Modelo
Veiculo
HistoricoProprietario
```

Regra acadêmica atendida:

```text
O veículo não possui cliente fixo como atributo direto.
A relação Cliente-Veículo é controlada por HistoricoProprietario,
preservando proprietário atual e proprietários anteriores.
```

Padrão de projeto aplicado:

```text
Adapter → VeiculoResponseAdapter
```

Endpoints de marca:

```text
POST   /api/marcas
PUT    /api/marcas/{id}
GET    /api/marcas/{id}
GET    /api/marcas
GET    /api/marcas/pesquisar?termo=valor
DELETE /api/marcas/{id}
```

Endpoints de modelo:

```text
POST   /api/modelos
PUT    /api/modelos/{id}
GET    /api/modelos/{id}
GET    /api/modelos
GET    /api/modelos/marca/{marcaId}
GET    /api/modelos/pesquisar?termo=valor
DELETE /api/modelos/{id}
```

Endpoints de veículo:

```text
POST   /api/veiculos
PUT    /api/veiculos/{id}
PATCH  /api/veiculos/{id}/transferir-proprietario
GET    /api/veiculos/{id}
GET    /api/veiculos
GET    /api/veiculos/pesquisar?termo=valor
DELETE /api/veiculos/{id}
```

Documentação:

```text
docs/README_ETAPA4_VEICULOS.md
docs/adr/ADR-007-adapter-veiculo-response.md
```

---

## Próxima etapa recomendada

```text
Etapa 5 — Serviço, Serviço Interno, Serviço Terceirizado e Empresa Terceirizada
```

Essa próxima etapa permitirá cadastrar os serviços executados pela oficina e preparar a composição da Ordem de Serviço.

---

# Etapa 5 — Módulo Serviço e Empresa Terceirizada

Foram implementados:

```text
Servico
ServicoInterno
ServicoTerceirizado
EmpresaTerceirizada
```

Regra acadêmica atendida:

```text
Servico especializa em ServicoInterno e ServicoTerceirizado.
Tipo: exclusiva e total (xt).
Todo Serviço é Interno ou Terceirizado, nunca ambos.
```

Também foi preservada a regra de que o serviço terceirizado continua sob responsabilidade da oficina perante o cliente.

Endpoints de serviço:

```text
POST   /api/servicos
PUT    /api/servicos/{id}
GET    /api/servicos/{id}
GET    /api/servicos
GET    /api/servicos/tipo/{tipoServico}
GET    /api/servicos/pesquisar?termo=valor
DELETE /api/servicos/{id}
```

Endpoints de empresa terceirizada:

```text
POST   /api/empresas-terceirizadas
PUT    /api/empresas-terceirizadas/{id}
GET    /api/empresas-terceirizadas/{id}
GET    /api/empresas-terceirizadas
GET    /api/empresas-terceirizadas/pesquisar?termo=valor
DELETE /api/empresas-terceirizadas/{id}
```

Documentação:

```text
docs/README_ETAPA5_SERVICOS.md
docs/adr/ADR-008-especializacao-servico.md
```

---

# Etapa 6 — Módulo Peça, Fornecedor e ItemPeca

Foram implementados:

```text
Fornecedor
Peca
ItemPeca
```

Regras acadêmicas atendidas:

```text
Toda peça usada na OS deve ser registrada como ItemPeca.
Todo ItemPeca deve estar vinculado a uma Peca cadastrada.
Todo ItemPeca deve ter Fornecedor identificado.
O ItemPeca fica preparado para gerar GarantiaPeca após a finalização da OS.
```

Endpoints de fornecedor:

```text
POST   /api/fornecedores
PUT    /api/fornecedores/{id}
GET    /api/fornecedores/{id}
GET    /api/fornecedores
GET    /api/fornecedores/pesquisar?termo=valor
DELETE /api/fornecedores/{id}
```

Endpoints de peça:

```text
POST   /api/pecas
PUT    /api/pecas/{id}
GET    /api/pecas/{id}
GET    /api/pecas
GET    /api/pecas/pesquisar?termo=valor
DELETE /api/pecas/{id}
```

Endpoints de item de peça:

```text
POST   /api/itens-peca
PUT    /api/itens-peca/{id}
GET    /api/itens-peca/{id}
GET    /api/itens-peca
GET    /api/itens-peca/ordem-servico/{idOrdemServico}
GET    /api/itens-peca/ordem-servico/{idOrdemServico}/pesquisar?termo=valor
DELETE /api/itens-peca/{id}
```

Documentação:

```text
docs/README_ETAPA6_PECAS.md
docs/adr/ADR-009-item-peca-como-entidade-associativa.md
```

---

## Próxima etapa recomendada

```text
Etapa 7 — OrdemServico, StatusOrdemServico, HistoricoStatusOrdem e ItemServico
```

Essa próxima etapa será o centro operacional do sistema, integrando Cliente, Veículo, Serviço, Colaborador, Peças, Fornecedor, Garantias e Pagamento.
