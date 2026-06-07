# Sistema de Gestão da Oficina Mecânica AV CAR AUTO CENTER

## Situação desta versão

Esta versão corresponde à **Etapa 13 — Integração do Angular no VS Code com o backend Spring Boot**.

O projeto está sendo refatorado para funcionar como uma API REST em Spring Boot, com frontend Angular como camada View e PostgreSQL local como banco de dados.

## Principais decisões técnicas

```text
Estilo arquitetural: Monólito modular
Backend: Java 21 + Spring Boot
Frontend: Angular executado no VS Code
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

---

# Etapa 10 — Estrutura de Dados I

Nesta etapa foram implementadas as exigências de Estrutura de Dados I aplicadas ao módulo de Ordem de Serviço.

Foram implementados:

```text
FilaAtendimento
ListaLinearBusca
OficinaIterator
FilaAtendimentoIterator
ListaLinearIterator
OrdenadorTemplate
OrdenadorOrdemServicoPorDataAbertura
OrdenadorOrdemServicoPorValorTotal
OrdenadorOrdemServicoPorPrioridade
CalculadoraRecursivaTotalOrdemServico
```

Padrões de projeto aplicados nesta etapa:

```text
Iterator
Template Method
```

Recursos acadêmicos atendidos:

```text
Estrutura de Dados Linear: Fila de Atendimento de OS.
Pesquisa: Busca linear em lista encadeada.
Ordenação Manual: Insertion Sort.
Função Recursiva: cálculo do total da OS por itens de serviço e peças.
```

Endpoints criados:

```text
GET /api/estrutura-dados/ordens-servico/fila-atendimento
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=DATA_ABERTURA
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=VALOR_TOTAL
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=PRIORIDADE
GET /api/estrutura-dados/ordens-servico/pesquisar-linear?termo=valor
GET /api/estrutura-dados/ordens-servico/{idOrdemServico}/total-recursivo
```

Documentação:

```text
docs/README_ETAPA10_ESTRUTURA_DADOS.md
docs/adr/ADR-013-estrutura-dados-ordem-servico.md
```

---

## Próxima etapa recomendada

```text
Etapa 11 — Decorator para notificação/auditoria e revisão dos 6 padrões de projeto
```

A próxima etapa deve implementar o padrão Decorator restante, consolidar a documentação dos seis padrões de projeto e preparar a integração com Angular.

---

# Etapa 11 — Decorator, Notificação Interna e Consolidação dos Padrões de Projeto

Nesta etapa foi implementado o sexto padrão de projeto exigido pela disciplina:

```text
Decorator
```

O padrão foi aplicado no mecanismo de notificação interna e auditoria operacional do sistema.

Foram implementados:

```text
Notificador
NotificadorOperacional
NotificadorDecorator
NotificadorAuditoriaDecorator
NotificacaoDTO
NotificacaoResultadoDTO
NotificacaoService
NotificacaoController
PadraoProjetoDTO
PadraoProjetoController
```

Aplicação prática:

```text
Alteração de status da Ordem de Serviço
    ↓
Registro no HistoricoStatusOrdem
    ↓
NotificacaoService
    ↓
NotificadorAuditoriaDecorator
    ↓
NotificadorOperacional
```

Endpoints criados:

```text
POST /api/notificacoes/simular
GET  /api/padroes-projeto
```

Padrões de projeto consolidados:

```text
Singleton        → DatabaseConnectionSingleton
Factory Method   → ClienteCadastroFactory / ClienteFactoryMethod
Adapter          → VeiculoResponseAdapter
Iterator         → OficinaIterator / FilaAtendimentoIterator / ListaLinearIterator
Template Method  → OrdenadorTemplate
Decorator        → NotificadorAuditoriaDecorator
```

Documentação:

```text
docs/README_ETAPA11_PADROES_PROJETO.md
docs/README_PADROES_PROJETO.md
docs/adr/ADR-014-decorator-notificacao-auditoria.md
```

---


# Etapa 12 — Frontend Angular e integração inicial com API REST

Foi criada a primeira versão da camada **View** em Angular no diretório:

```text
frontend/oficina-web
```

A aplicação Angular foi preparada para consumir a API REST local do backend:

```text
http://localhost:9081/api
```

Foram criados:

```text
Estrutura base Angular
Rotas principais
Layout com menu lateral
Serviços HTTP
Interceptador de erro da API
Models TypeScript compatíveis com DTOs Java
Tela de painel operacional
Tela inicial de clientes
Tela inicial de veículos
Tela inicial de ordens de serviço
Tela de estrutura de dados
Tela de padrões de projeto
Link direto para Swagger
```

Comandos de execução do frontend no VS Code:

```powershell
cd frontend/oficina-web
npm.cmd install
npm.cmd run start:proxy
```

Documentação:

```text
docs/README_ETAPA12_ANGULAR.md
docs/adr/ADR-015-angular-integracao-api-rest.md
docs/COMO_EXECUTAR_BACKEND_FRONTEND.md
```


---

# Etapa 13 — Integração do Angular no VS Code com o Backend

Esta etapa estabiliza o uso do frontend Angular no VS Code e prepara a comunicação real com a API REST do backend Spring Boot.

Foram realizados:

```text
Configuração do Angular para consumir /api via proxy.conf.json
Ajuste dos environments para uso de /api no desenvolvimento
Atualização do ApiResponse TypeScript para compatibilidade com respostas data/dados
Ajuste do BaseApiService para respostas paginadas do backend
Correção da tela de Clientes para integração com GET /api/clientes e POST de PF/PJ
Documentação dos endpoints para implementação das telas Angular
Criação de script auxiliar de verificação do banco para integração frontend/backend
Atualização da ADR de decisão de integração Angular + VS Code + backend
```

Comandos principais no ambiente Windows:

Backend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn.cmd spring-boot:run
```

Frontend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd run start:proxy
```

Acessos principais:

```text
Frontend Angular: http://localhost:4200
Backend Spring Boot: http://localhost:9081
Swagger: http://localhost:9081/swagger-ui.html
Verificação do banco: http://localhost:9081/api/database/status
```

Documentação adicionada:

```text
docs/README_ETAPA13_INTEGRACAO_ANGULAR_BACKEND.md
docs/API_ENDPOINTS_FRONTEND.md
docs/adr/ADR-016-integracao-angular-vscode-backend.md
database/03_verificacao_integracao_frontend.sql
```

## Próxima etapa recomendada

```text
Etapa 14 — Refinar o frontend Angular com CRUD completo de Clientes, Veículos e Ordens de Serviço
```

A próxima etapa deve evoluir as telas Angular para uso em apresentação, com cadastro, consulta, edição, exclusão lógica e validações visuais.

---

# Etapa 14 — Ajuste de Desempenho da Integração Angular/Backend

Esta etapa corrige a lentidão percebida na tela inicial do Angular, especialmente no bloco de **Verificação da API**.

Foram realizados:

```text
Correção do botão que ficava preso em "Verificando..." quando havia erro de comunicação.
Aplicação de timeout de 3 segundos nas chamadas de diagnóstico do dashboard.
Tratamento amigável para backend fora do ar.
Melhoria no endpoint GET /api/database/status.
Retorno do tempo de resposta do backend para o Angular.
Ajuste do Singleton de conexão para não manter conexão JDBC aberta apenas para diagnóstico.
Criação de documentação da etapa e ADR específica.
Criação de script SQL auxiliar para verificação rápida do banco.
```

Arquivos principais atualizados:

```text
frontend/oficina-web/src/app/core/models/database-status.model.ts
frontend/oficina-web/src/app/core/services/dashboard.service.ts
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.ts
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.html
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.css
src/main/java/br/com/avcar/oficina/core/database/DatabaseStatusResult.java
src/main/java/br/com/avcar/oficina/core/database/DatabaseConnectionChecker.java
src/main/java/br/com/avcar/oficina/core/designpattern/singleton/DatabaseConnectionSingleton.java
src/main/java/br/com/avcar/oficina/view/api/DatabaseStatusController.java
```

Documentação adicionada:

```text
docs/README_ETAPA14_DESEMPENHO_INTEGRACAO_API.md
docs/adr/ADR-017-ajuste-desempenho-integracao-api.md
database/04_verificacao_desempenho_integracao.sql
```

Comandos para testar:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn.cmd spring-boot:run
```

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd run start:proxy
```

Acessar:

```text
http://localhost:4200
```

Ao clicar em **Verificar banco**, o sistema deve responder rapidamente, informando se o backend e o PostgreSQL local estão disponíveis.

---

# Etapa 15 — Ampliação do Frontend Angular com Telas Operacionais

Esta etapa amplia o frontend Angular para contemplar as principais telas operacionais do sistema da oficina mecânica.

Foram criadas ou atualizadas as telas:

```text
Painel operacional
Clientes
Funções
Colaboradores
Marcas e Modelos
Veículos
Serviços
Empresas Terceirizadas
Peças e Fornecedores
Ordens de Serviço
Itens da OS
Pagamentos
Garantias
Estrutura de Dados I
Padrões de Projeto
```

Também foram criados novos serviços Angular para integração com a API REST:

```text
FuncaoApiService
ColaboradorApiService
MarcaApiService
ModeloApiService
ServicoApiService
EmpresaTerceirizadaApiService
FornecedorApiService
PecaApiService
ItemServicoApiService
ItemPecaApiService
PagamentoApiService
GarantiaApiService
```

Models TypeScript adicionados ou atualizados:

```text
pessoa.model.ts
servico.model.ts
peca.model.ts
pagamento.model.ts
garantia.model.ts
ordem-servico.model.ts
```

Documentação adicionada:

```text
docs/README_ETAPA15_FRONTEND_TELAS.md
docs/adr/ADR-018-ampliacao-front-end-angular.md
database/05_verificacao_frontend_telas.sql
```

Comandos para testar:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn.cmd spring-boot:run
```

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd run start:proxy
```

Acessar:

```text
http://localhost:4200
```

---

# Etapa 16 — Correção dos botões e atualização automática das tabelas no Angular

Nesta etapa foi corrigido o comportamento em que alguns botões ficavam visualmente presos em estado de processamento após comunicação com o backend.

Foram aplicados os seguintes ajustes:

```text
1. Separação entre estado de consulta e estado de processamento.
2. Uso de finalize do RxJS para encerrar carregamento em sucesso ou erro.
3. Atualização automática das tabelas após salvar, alterar, excluir ou acionar registros.
4. Atualização imediata em memória quando a API retorna o objeto salvo.
5. Reconsulta ao backend para manter fidelidade com o PostgreSQL.
6. Timeout de 10 segundos nas chamadas genéricas da API.
7. Mensagem clara quando o backend demora para responder.
```

Componentes ajustados:

```text
Clientes
Funções
Colaboradores
Marcas e Modelos
Veículos
Serviços
Empresas Terceirizadas
Peças e Fornecedores
Ordens de Serviço
Itens da OS
Pagamentos
Garantias
```

Documentação adicionada:

```text
docs/README_ETAPA16_FRONTEND_ESTADOS_ATUALIZACAO.md
docs/adr/ADR-019-estados-atualizacao-automatica-front-end.md
database/06_verificacao_frontend_estados_atualizacao.sql
```

---

# Etapa 17 — Correção definitiva da atualização visual do Angular após retorno da API

Nesta etapa foi corrigido o problema em que as telas do Angular só atualizavam visualmente após o usuário clicar em outro botão ou executar outra ação na página.

O comportamento observado era:

```text
1. O registro era salvo no backend.
2. A API retornava corretamente.
3. A tabela era atualizada internamente no TypeScript.
4. Porém, a tela só redesenhava depois de outro clique.
```

A causa provável estava na execução das respostas HTTP fora do ciclo de detecção de mudanças do Angular em algumas chamadas assíncronas. Por isso, o interceptor global da API foi ajustado para garantir que todas as respostas, erros e finalizações de requisições HTTP retornem para dentro do `NgZone`.

Arquivos ajustados:

```text
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
frontend/oficina-web/src/app/app.config.ts
docs/README_ETAPA17_CORRECAO_CHANGE_DETECTION_ANGULAR.md
docs/adr/ADR-020-correcao-atualizacao-visual-angular.md
database/07_verificacao_correcao_atualizacao_visual.sql
```

Resultado esperado:

```text
1. Ao clicar em Salvar, o botão volta ao estado normal sem depender de outro clique.
2. A tabela é redesenhada automaticamente após o retorno da API.
3. Mensagens de sucesso ou erro aparecem imediatamente.
4. As telas deixam de depender de ações manuais para atualizar a visualização.
```

Comandos recomendados após atualizar esta etapa:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd run start:proxy
```

Caso o navegador ainda apresente comportamento antigo, interrompa o Angular com `CTRL + C`, rode novamente `npm.cmd run start:proxy` e atualize o navegador com `CTRL + F5`.


---

# Etapa 18 — Correção da tela de Pagamentos e status da OS

Foi corrigida a tela Angular de Pagamentos para respeitar visualmente o fluxo da Ordem de Serviço:

```text
ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO
```

A partir desta etapa:

```text
1. A tela mostra o status atual da OS selecionada.
2. O botão Salvar pagamento fica bloqueado se a OS não estiver em PAGAMENTO.
3. A tela possui botão para avançar a OS para a próxima etapa do fluxo.
4. O interceptor do Angular exibe a mensagem detalhada enviada pelo backend.
5. A regra de negócio continua protegida no backend.
```

Documentação adicionada:

```text
docs/README_ETAPA18_CORRECAO_PAGAMENTOS_STATUS_OS.md
docs/adr/ADR-021-correcao-pagamentos-status-os.md
database/08_verificacao_pagamentos_status_os.sql
```


## Etapa 19 — Fluxo automático de pagamentos

A tela de Pagamentos e o backend foram ajustados para que, ao salvar um pagamento, o sistema atualize automaticamente a tabela, o resumo financeiro e o status da Ordem de Serviço. O backend conduz a OS até PAGAMENTO respeitando o histórico e finaliza automaticamente a OS quando o valor pago quita o total.

---

## Etapa 20 — Correção de compilação Angular nos services especializados

Nesta etapa foi corrigido um erro de compilação do frontend Angular envolvendo herança entre `BaseApiService`, `OrdemServicoApiService` e `PagamentoApiService`.

### Problema corrigido

O Angular acusava erro porque os services especializados declaravam novamente a propriedade `tempoLimiteMs`, já existente na classe base.

### Correção aplicada

```text
BaseApiService
- tempoLimiteMs alterado para protected.

OrdemServicoApiService
- removida declaração duplicada de tempoLimiteMs.

PagamentoApiService
- removida declaração duplicada de tempoLimiteMs.
```

### Arquivos alterados

```text
frontend/oficina-web/src/app/core/services/base-api.service.ts
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
frontend/oficina-web/src/app/core/services/pagamento-api.service.ts

docs/README_ETAPA20_CORRECAO_COMPILACAO_ANGULAR_SERVICES.md
docs/adr/ADR-023-correcao-heranca-services-angular.md
database/10_verificacao_correcao_compilacao_angular.sql
```


---

# Etapa 21 — Script SQL único do banco de dados

Foi criado o arquivo:

```text
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
```

Esse arquivo permanece disponível como script consolidado opcional para criar o banco físico e inserir os dados iniciais obrigatórios em uma única execução no pgAdmin.

A partir da Etapa 22, os scripts foram reorganizados por finalidade. Para montar o banco do zero, recomenda-se usar `database/01_schema/01_create_schema.sql` e depois `database/02_seed/02_seed_inicial.sql`. O script completo permanece em `database/04_completo` apenas como alternativa opcional.

Documentação adicionada:

```text
docs/README_ETAPA21_SCRIPT_SQL_UNICO.md
docs/adr/ADR-024-script-sql-unico-banco.md
```

---

# Etapa 22 — Organização dos Scripts SQL e Carregamento Inicial Automático das Telas

Nesta etapa foram feitos dois ajustes solicitados durante os testes do sistema.

## 1. Organização dos scripts SQL

Os scripts do banco foram reorganizados por finalidade:

```text
database/01_schema/01_create_schema.sql       → criação das tabelas e constraints
database/02_seed/02_seed_inicial.sql          → dados iniciais obrigatórios
database/03_verificacoes/*.sql                → consultas auxiliares de conferência
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql → script completo opcional
```

A forma recomendada para montar o banco do zero passa a ser:

```text
1. Executar database/01_schema/01_create_schema.sql
2. Executar database/02_seed/02_seed_inicial.sql
```

O script completo permanece disponível, mas apenas como alternativa de demonstração ou recriação rápida.

## 2. Correção do carregamento inicial das telas Angular

Foi corrigido o comportamento em que algumas telas, como Funções, só exibiam os dados do banco depois de o usuário clicar em um botão ou digitar em algum campo.

A correção foi aplicada no interceptor global da API, garantindo que as respostas HTTP atualizem a interface automaticamente.

Também foi ajustado o `package.json` para que `npm.cmd start` já execute o Angular com proxy para o backend:

```text
ng serve --proxy-config proxy.conf.json --open
```

## Resultado esperado

Ao abrir telas como Clientes, Funções, Colaboradores, Veículos, Serviços ou Pagamentos, os dados existentes no PostgreSQL devem aparecer automaticamente, sem necessidade de clicar em “Listar todos”.

Documentação adicionada:

```text
docs/README_ETAPA22_ORGANIZACAO_SQL_CARREGAMENTO_TELAS.md
docs/adr/ADR-025-organizacao-sql-carregamento-inicial-telas.md
database/03_verificacoes/11_verificacao_carregamento_inicial_telas.sql
```

---

# Etapa 23 — Atualização Imediata das Tabelas após Cadastro

Nesta etapa foi corrigido o comportamento observado no frontend Angular em que, após salvar um registro, a tabela só exibia os dados atualizados depois de o usuário clicar novamente em algum botão, como “Listar todos”.

## Problema identificado

Em telas como Clientes e Funções, o cadastro era concluído no backend, porém a interface permanecia temporariamente com a tabela antiga ou com mensagem de atualização. Isso prejudicava a usabilidade, pois o usuário precisava realizar uma segunda ação para enxergar o registro salvo.

## Correção aplicada

A comunicação com a API foi ajustada para que, após uma operação de gravação, o frontend execute a sequência correta:

```text
1. Enviar cadastro para o backend.
2. Aguardar confirmação da API.
3. Consultar novamente a lista diretamente no banco, sem cache.
4. Atualizar a tabela local com uma nova referência de array.
5. Encerrar os estados de carregamento e processamento.
6. Exibir feedback de sucesso ao usuário.
```

## Arquivos alterados

```text
frontend/oficina-web/src/app/core/services/base-api.service.ts
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.html
frontend/oficina-web/src/app/pages/funcoes/funcoes.component.ts
frontend/oficina-web/src/app/pages/funcoes/funcoes.component.html
```

## Melhorias técnicas

```text
- As consultas GET agora usam parâmetro _t com timestamp para evitar resposta em cache.
- As consultas GET também enviam cabeçalhos Cache-Control e Pragma como no-cache.
- O interceptor HTTP foi simplificado para tratar apenas erros da API.
- A atualização visual passou a ser controlada diretamente pelos componentes críticos.
- O salvamento de Clientes e Funções passou a usar fluxo encadeado: salvar → listar → atualizar tabela.
```

## Resultado esperado

Ao abrir a tela, os dados já devem ser carregados automaticamente. Ao salvar um cliente ou uma função, a tabela deve ser atualizada automaticamente sem exigir clique manual em “Listar todos”.

Documentação adicionada:

```text
docs/README_ETAPA23_ATUALIZACAO_IMEDIATA_TELAS.md
docs/adr/ADR-026-atualizacao-imediata-tabelas-angular.md
database/03_verificacoes/12_verificacao_atualizacao_imediata_telas.sql
```

---

# Etapa 24 — Atualização Global das Telas Angular e Organização Operacional

Nesta etapa foi ampliada a correção de atualização imediata das tabelas para todas as abas do frontend Angular.

## Problema corrigido

Algumas telas salvavam os dados corretamente no backend, porém a interface somente mostrava a listagem atualizada após o usuário clicar novamente em outro botão. Esse comportamento foi corrigido globalmente.

## Correção aplicada

```text
1. O interceptor global da API passou a sincronizar a atualização visual após respostas HTTP.
2. O BaseApiService passou a retornar novas referências de array nas listagens.
3. Serviços com consultas específicas também foram ajustados para retornar novas listas.
4. As telas passam a refletir os dados do backend sem exigir novo clique manual.
```

## Abas cobertas

```text
Clientes
Funções
Colaboradores
Marcas e Modelos
Veículos
Serviços
Empresas Terceirizadas
Peças e Fornecedores
Ordens de Serviço
Itens da OS
Pagamentos
Garantias
Fila de Atendimento
```

## Organização visual

O frontend foi mantido como um sistema de gestão de oficina mecânica, com menu agrupado em:

```text
Cadastros
Ordens de Serviço
```

A aba de Padrões de Projeto não aparece mais no menu operacional. A funcionalidade de Estrutura de Dados passou a aparecer como Fila de Atendimento, nome mais adequado para o uso real da oficina.

Documentação adicionada:

```text
docs/README_ETAPA24_ATUALIZACAO_GLOBAL_FRONTEND.md
docs/adr/ADR-027-atualizacao-global-telas-angular.md
database/03_verificacoes/13_verificacao_atualizacao_global_telas.sql
```

---

## Etapa 25 — Correção global da comunicação Angular/Backend

Esta etapa corrigiu o problema em que algumas telas do Angular só exibiam os dados do banco após clique, digitação ou nova ação manual.

Foram ajustadas globalmente as telas de cadastros e de ordens de serviço para que:

```text
1. Ao abrir a tela, os dados sejam carregados automaticamente.
2. Após salvar, editar, inativar, acionar ou encerrar registros, a tabela seja recarregada automaticamente.
3. As listas recebam nova referência, garantindo atualização visual imediata.
4. As consultas GET usem anti-cache para evitar dados antigos.
5. O BaseApiService aceite respostas ApiResponse, PageResponse, array direto ou objeto direto.
```

Telas revisadas:

```text
Veículos
Colaboradores
Marcas e Modelos
Serviços
Empresas Terceirizadas
Peças e Fornecedores
Ordens de Serviço
Serviços e Peças da OS
Pagamentos
Garantias
Fila de Atendimento
```

A tela antiga de Estrutura de Dados foi mantida tecnicamente, mas no menu operacional é apresentada como **Fila de Atendimento**, com linguagem mais compatível com uma oficina mecânica.

Validação TypeScript executada:

```bash
./node_modules/.bin/tsc --noEmit -p tsconfig.app.json
```

Resultado: sem erros de TypeScript.


# Etapa 26 — Otimização técnica e auditoria de redundâncias

Esta etapa reorganizou pontos internos do projeto para reduzir duplicação e preparar a próxima fase visual do Angular.

Principais ajustes realizados:

```text
1. Inclusão do script scripts/auditoria_otimizacao_codigo.py.
2. Criação do relatório docs/relatorios/RELATORIO_AUDITORIA_OTIMIZACAO_CODIGO.md.
3. Remoção da tela operacional de Padrões de Projeto do frontend, mantendo os padrões documentados no backend e nas ADRs.
4. Menu principal do Angular passou a ser orientado por dados em AppComponent, evitando repetição de links no HTML.
5. BaseApiService recebeu métodos reutilizáveis para salvar/excluir e recarregar a lista automaticamente.
6. Componentes simples passaram a usar operações centralizadas do BaseApiService quando aplicável.
7. Organização SQL mantida em schema, seed, verificações e script completo opcional.
```

Comando de auditoria local:

```bash
python scripts/auditoria_otimizacao_codigo.py
```

No frontend:

```bash
cd frontend/oficina-web
npm.cmd install
npm.cmd start
```


---

## Etapa 27 — Reformulação Visual Profissional do Angular

Nesta etapa, o frontend Angular foi reformulado para se aproximar de um sistema administrativo real de oficina mecânica, com foco em usabilidade, rastreabilidade e operação diária.

Principais ajustes realizados:

```text
1. Reformulação completa do layout global do Angular.
2. Criação de sidebar profissional com grupos: Atendimento e OS, Cadastros e Gestão.
3. Criação de topbar com busca visual, usuário logado, atalho para nova OS e status do backend local.
4. Remoção da tela operacional de Padrões de Projeto do menu do sistema.
5. Renomeação operacional da antiga Estrutura de Dados para Fila de Atendimento.
6. Substituição do dashboard genérico por Visão Geral da Oficina.
7. Criação de indicadores reais calculados a partir dos services existentes.
8. Criação de atalhos operacionais para OS, clientes, veículos, pagamentos, garantias e fila.
9. Padronização visual de cards, tabelas, botões, badges, formulários, alertas e responsividade.
10. Inclusão de telas reservadas para Relatórios e Configurações no grupo Gestão.
```

A comunicação com o backend Spring Boot REST foi preservada. Nenhum endpoint foi alterado.


## Etapa 28 — Ajuste visual conforme modelo de referência

- Layout Angular reformulado para seguir o modelo visual de referência enviado.
- Sidebar escura, topbar clara, cards operacionais, fluxo de OS, ações rápidas e tabelas profissionais.
- Mantida a comunicação validada com backend e banco de dados.
- Validação TypeScript executada sem erros.



## Etapa 29 — Limpeza da barra superior e remoção de ícones decorativos

Nesta etapa, o frontend Angular foi ajustado para uma apresentação mais sóbria e profissional. A busca global da barra superior foi removida, o botão de notificação foi retirado e os símbolos decorativos foram substituídos por siglas funcionais nos menus e cards.

Principais ajustes:

```text
- Remoção da busca superior "Buscar no sistema...".
- Remoção do ícone de notificações.
- Remoção do botão visual de modo escuro.
- Remoção de emojis e símbolos decorativos da tela inicial.
- Substituição dos ícones do menu por siglas operacionais, como OS, CL, VE, PG e GT.
- Manutenção da comunicação Angular com o backend Spring Boot.
```


---

# Etapa 30 — Refatoração visual com abas superiores e layout quadrado

Nesta etapa, o frontend Angular foi refatorado para abandonar o menu lateral e utilizar uma navegação superior em abas, com aparência mais direta, quadrada e semelhante a um sistema administrativo de cadastro utilizado em ambiente local de oficina mecânica.

## Alterações principais

```text
1. Remoção da sidebar lateral escura.
2. Criação de cabeçalho superior fixo com identificação da AV CAR AUTO CENTER.
3. Criação de abas superiores agrupadas por Operação, Cadastros e Gestão.
4. Ajuste visual para componentes mais quadrados, com bordas menores e sombras discretas.
5. Remoção de elementos decorativos excessivos no painel inicial.
6. Correção do texto dos cards que aparecia sobreposto.
7. Manutenção da comunicação Angular → Spring Boot → PostgreSQL.
8. Preservação das rotas e services Angular existentes.
```

## Resultado esperado

O sistema passa a ter aparência mais próxima de um sistema de cadastro operacional, com navegação superior, menos elementos decorativos e telas mais objetivas para uso na oficina.

## Etapa 31 — Menu superior com categorias

Nesta etapa, a navegação superior do Angular foi reorganizada para exibir categorias em vez de mostrar todas as telas diretamente na barra principal.

Nova organização:

```text
Início
Operação
Cadastros
Gestão
```

As opções de cada grupo aparecem em menus suspensos. A alteração melhora a usabilidade, reduz a poluição visual e deixa o sistema com aparência mais adequada a um software administrativo de oficina mecânica.

Arquivos principais alterados:

```text
frontend/oficina-web/src/app/app.component.html
frontend/oficina-web/src/app/app.component.css
docs/README_ETAPA31_MENU_SUPERIOR_CATEGORIAS.md
docs/adr/ADR-034-menu-superior-categorias-angular.md
```


## Etapa 32 — Paleta visual de oficina e implementação das logos

Nesta etapa foi aplicada uma identidade visual mais compatível com uma oficina mecânica, utilizando azul automotivo escuro, grafite, cinza metálico e laranja como cor de destaque. O layout superior por categorias foi mantido, mas a aparência foi ajustada para ficar mais profissional e alinhada à marca AV CAR AUTO CENTER.

Alterações principais:

- Implementação da logo completa no cabeçalho superior do Angular.
- Implementação da logo em formato de ícone como favicon da aba do navegador.
- Criação da pasta `frontend/oficina-web/public/assets/branding` para armazenar os arquivos visuais da marca.
- Ajuste da paleta global no `src/styles.css`.
- Reformulação do cabeçalho, menu superior, botões, cards, tabelas, campos e badges.
- Preservação das rotas, services, endpoints e integração Angular com o backend Spring Boot.

Arquivos principais alterados:

- `frontend/oficina-web/src/index.html`
- `frontend/oficina-web/src/app/app.component.html`
- `frontend/oficina-web/src/app/app.component.css`
- `frontend/oficina-web/src/styles.css`
- `frontend/oficina-web/src/app/pages/dashboard/dashboard.component.css`
- `frontend/oficina-web/public/assets/branding/av-car-favicon.png`
- `frontend/oficina-web/public/assets/branding/av-car-logo-horizontal.png`

## Etapa 33 — Logos responsivas implementadas no Angular

Nesta etapa foi implementado no código do frontend Angular o pacote de logos responsivas da AV CAR AUTO CENTER. A aplicação agora utiliza favicon, ícones em múltiplos tamanhos e logo adaptável no cabeçalho do sistema.

Arquivos principais alterados:

```text
frontend/oficina-web/src/index.html
frontend/oficina-web/src/app/app.component.html
frontend/oficina-web/src/app/app.component.css
frontend/oficina-web/src/styles.css
frontend/oficina-web/public/assets/branding
```

A implementação mantém a comunicação com o backend Spring Boot e não altera endpoints, services ou regras de negócio. Para visualizar corretamente a nova logo e o favicon, recomenda-se executar o frontend e limpar o cache do navegador com `CTRL + F5`.


## Etapa 34 - Nota Fiscal / Recibo em PDF

Foi adicionada a geração de PDF para Ordens de Serviço. A tela **Ordens de Serviço** agora possui o botão **Nota PDF**, que baixa um documento com cabeçalho da oficina, dados do cliente, veículo, peças, serviços, pagamentos, totais e assinaturas.

Endpoint criado no backend:

```http
GET /api/notas-fiscais/ordens-servico/{id}/pdf
```

Arquivos principais:

```text
src/main/java/br/com/avcar/oficina/business/notafiscal/controller/NotaFiscalController.java
src/main/java/br/com/avcar/oficina/business/notafiscal/service/NotaFiscalPdfService.java
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html
```

Observação: o documento gerado é uma nota/comprovante interno simplificado e não substitui uma nota fiscal eletrônica oficial autorizada por órgão fiscal.

## Etapa 35 — Validação real de CPF e CNPJ

Foi adicionada validação matemática de CPF e CNPJ no cadastro de clientes. O sistema agora não aceita documentos apenas pelo tamanho; os dígitos verificadores são calculados e validados tanto no frontend Angular quanto no backend Spring Boot.

Arquivos principais:

```text
src/main/java/br/com/avcar/oficina/core/validation/DocumentoValidationUtils.java
src/main/java/br/com/avcar/oficina/business/pessoa/validation/ClienteValidation.java
frontend/oficina-web/src/app/core/validation/documento-validation.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.html
frontend/oficina-web/src/styles.css
```

Exemplos bloqueados:

```text
000.000.000-00
111.111.111-11
123.456.789-00
00.000.000/0000-00
11.111.111/1111-11
```

## Etapa 36 — Validações de campos e exceptions

Foram adicionadas validações de entrada no Angular e no backend Spring Boot para bloquear dados inválidos antes da persistência. A etapa inclui validação de CPF e CNPJ reais, e-mail, telefone, placa, chassi, anos de veículo, datas futuras, valores negativos, pagamentos acima do saldo pendente, garantias fora do prazo e geração de nota/recibo interno apenas com dados mínimos consistentes.

Documentação complementar:

- `docs/README_ETAPA36_VALIDACOES_EXCEPTIONS_CAMPOS.md`
- `docs/adr/ADR-039-validacoes-exceptions-campos-regras.md`
