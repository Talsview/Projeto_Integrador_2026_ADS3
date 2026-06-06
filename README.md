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
