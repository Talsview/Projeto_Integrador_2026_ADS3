# Sistema de Gestão da Oficina Mecânica AV CAR AUTO CENTER

## Situação desta versão

Esta versão corresponde à **Etapa 7 — Módulo Ordem de Serviço, Status, Histórico de Status e ItemServico**.

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

# Etapa 7 — Módulo Ordem de Serviço, Status, Histórico de Status e ItemServico

Foram implementados:

```text
OrdemServico
StatusOrdemServico
HistoricoStatusOrdem
ItemServico
ExecucaoServicoTerceirizado
```

Regras acadêmicas atendidas:

```text
Cliente solicita OrdemServico.
Veiculo recebe OrdemServico.
OrdemServico possui HistoricoStatusOrdem.
A OS segue o fluxo: ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO.
A OS deve possuir pelo menos um ItemServico antes de entrar em execução.
Todo ItemServico possui Serviço cadastrado e Colaborador responsável.
Serviço terceirizado gera ExecucaoServicoTerceirizado.
ItemPeca passou a validar a existência da OrdemServico e recalcular o total da OS.
```

Endpoints de Ordem de Serviço:

```text
POST   /api/ordens-servico
PUT    /api/ordens-servico/{id}
PATCH  /api/ordens-servico/{id}/status
GET    /api/ordens-servico/{id}
GET    /api/ordens-servico
GET    /api/ordens-servico/pesquisar?termo=valor
DELETE /api/ordens-servico/{id}
```

Endpoints de ItemServico:

```text
POST   /api/itens-servico
PUT    /api/itens-servico/{id}
GET    /api/itens-servico/{id}
GET    /api/itens-servico
GET    /api/itens-servico/ordem-servico/{idOrdemServico}
GET    /api/itens-servico/ordem-servico/{idOrdemServico}/pesquisar?termo=valor
DELETE /api/itens-servico/{id}
```

Endpoints de Status da OS:

```text
GET    /api/status-ordem-servico/{id}
GET    /api/status-ordem-servico
GET    /api/status-ordem-servico/pesquisar?termo=valor
```

Documentação:

```text
docs/README_ETAPA7_ORDEM_SERVICO.md
docs/adr/ADR-010-fluxo-ordem-servico.md
```

---

## Próxima etapa recomendada

```text
Etapa 8 — GarantiaPeca, GarantiaServico e início automático das garantias após finalização da OS
```

Essa próxima etapa completará a regra de garantia, iniciando os prazos de peça e serviço após o status FINALIZADO.

---

# Etapa 8 — Garantias de Peças e Serviços

Nesta etapa foi implementado o módulo de garantias, integrando `GarantiaPeca` e `GarantiaServico` ao fluxo real da Ordem de Serviço.

Foram implementados:

```text
GarantiaPeca
GarantiaServico
StatusGarantia
ResponsabilidadeGarantiaPeca
```

Regras atendidas:

```text
ItemPeca gera GarantiaPeca.
ItemServico gera GarantiaServico.
A garantia de peça começa após a finalização da OS.
A garantia de serviço começa após a finalização da OS.
O prazo de garantia da peça é definido no cadastro da peça.
O prazo de garantia do serviço vem do cadastro do serviço.
A garantia de peça pode ter responsabilidade do fornecedor, mantendo a oficina responsável pelo atendimento ao cliente.
```

Fluxo implementado:

```text
1. Cadastrar ItemPeca ou ItemServico.
2. Sistema cria a garantia com status AGUARDANDO_FINALIZACAO_OS.
3. Alterar OS para FINALIZADO.
4. Sistema define dataInicio, dataFim e status VIGENTE nas garantias.
```

Endpoints de GarantiaPeca:

```text
GET    /api/garantias/pecas/{id}
GET    /api/garantias/pecas
GET    /api/garantias/pecas/item-peca/{idItemPeca}
GET    /api/garantias/pecas/ordem-servico/{idOrdemServico}
PATCH  /api/garantias/pecas/{id}/acionar
PATCH  /api/garantias/pecas/{id}/encerrar
```

Endpoints de GarantiaServico:

```text
GET    /api/garantias/servicos/{id}
GET    /api/garantias/servicos
GET    /api/garantias/servicos/item-servico/{idItemServico}
GET    /api/garantias/servicos/ordem-servico/{idOrdemServico}
PATCH  /api/garantias/servicos/{id}/acionar
PATCH  /api/garantias/servicos/{id}/encerrar
```

Documentação:

```text
docs/README_ETAPA8_GARANTIAS.md
docs/adr/ADR-011-garantias-apos-finalizacao-os.md
```

---

## Próxima etapa recomendada

```text
Etapa 9 — Pagamento
```

A próxima etapa deve implementar os pagamentos da Ordem de Serviço, permitindo registrar valores pagos, formas de pagamento, status do pagamento e validação do fluxo financeiro da OS.

---

# Etapa 9 — Pagamento

Nesta etapa foi implementado o módulo financeiro da Ordem de Serviço, permitindo registrar nenhum, um ou vários pagamentos para a mesma OS.

Foram implementados:

```text
Pagamento
FormaPagamento
StatusPagamento
ResumoPagamentoOrdemServicoDTO
```

Regras atendidas:

```text
OrdemServico gera Pagamento.
Uma OS pode gerar nenhum, um ou vários pagamentos.
Pagamento só pode ser registrado quando a OS está no status PAGAMENTO.
Apenas pagamentos com status PAGO abatem o saldo financeiro da OS.
Pagamentos PENDENTE, CANCELADO ou ESTORNADO não quitam a OS.
A OS só pode avançar para FINALIZADO quando o valor pago for igual ou superior ao valor total da OS.
Após a finalização da OS, pagamentos não podem ser incluídos, alterados ou inativados.
```

Endpoints de Pagamento:

```text
POST   /api/pagamentos
PUT    /api/pagamentos/{id}
PATCH  /api/pagamentos/{id}/status?statusPagamento=PAGO
GET    /api/pagamentos/{id}
GET    /api/pagamentos
GET    /api/pagamentos/ordem-servico/{idOrdemServico}
GET    /api/pagamentos/ordem-servico/{idOrdemServico}/pesquisar?termo=valor
GET    /api/pagamentos/ordem-servico/{idOrdemServico}/resumo
DELETE /api/pagamentos/{id}
```

Documentação:

```text
docs/README_ETAPA9_PAGAMENTOS.md
docs/adr/ADR-012-pagamento-condicao-finalizacao-os.md
```

---

## Próxima etapa recomendada

```text
Etapa 10 — Estrutura de Dados I
```

A próxima etapa deve implementar a Fila de Atendimento de Ordens de Serviço, a pesquisa por dados principais da OS, a ordenação manual por data, valor ou prioridade e a função recursiva para cálculo de totais.
