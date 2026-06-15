# AV CAR AUTO CENTER — Sistema de Gestão para Oficina Mecânica

Sistema monolítico para gestão operacional de uma oficina mecânica local, desenvolvido para controlar clientes, veículos, colaboradores, funções, ordens de serviço, peças, fornecedores, pagamentos, garantias, fila de atendimento e geração de documentos em PDF.

O projeto foi desenvolvido com foco em rastreabilidade, integridade dos dados, organização acadêmica, arquitetura em camadas, uso de padrões de projeto e funcionamento local, sem dependência obrigatória de internet.

---

## 1. Visão Geral do Projeto

O sistema AV CAR AUTO CENTER tem como objetivo informatizar o controle das Ordens de Serviço de uma oficina mecânica, permitindo registrar todo o fluxo de atendimento desde o cadastro do cliente e do veículo até a finalização da OS, pagamento e emissão de documento em PDF.

O sistema permite controlar:

* Clientes pessoa física e pessoa jurídica;
* Veículos atendidos pela oficina;
* Histórico de proprietários dos veículos;
* Colaboradores e suas funções;
* Serviços internos e terceirizados;
* Ordens de Serviço;
* Peças aplicadas em Ordens de Serviço;
* Fornecedores das peças;
* Garantias de peças e serviços;
* Pagamentos;
* Fila de atendimento;
* Geração de nota/recibo interno em PDF.

---

## 2. Objetivo do Sistema

O principal objetivo do sistema é melhorar o controle das Ordens de Serviço da oficina, garantindo que cada atendimento seja registrado de forma organizada e rastreável.

A aplicação busca resolver problemas como:

* Falta de controle histórico das OS;
* Dificuldade para identificar peças utilizadas em cada serviço;
* Falta de vínculo entre peça, fornecedor e garantia;
* Falta de controle sobre quem executou cada serviço;
* Falta de organização no fluxo da OS;
* Falta de registro adequado dos pagamentos;
* Dificuldade para consultar garantias;
* Dificuldade para gerar documentos padronizados da OS.

---

## 3. Principais Funcionalidades

### 3.1 Clientes

O módulo de clientes permite cadastrar e consultar clientes da oficina.

O cliente pode ser:

* Pessoa Física;
* Pessoa Jurídica.

Dados principais:

* Nome;
* Telefone;
* E-mail;
* Endereço;
* CPF ou CNPJ;
* RG;
* Data de nascimento;
* Razão social;
* Nome fantasia;
* Inscrição estadual;
* Status.

Regras aplicadas:

* Todo cliente deve ser classificado como pessoa física ou pessoa jurídica;
* Um cliente pode solicitar nenhuma, uma ou várias Ordens de Serviço;
* O cadastro de cliente alimenta os módulos de veículos e ordens de serviço.

---

### 3.2 Veículos

O módulo de veículos permite cadastrar os veículos atendidos pela oficina.

Dados principais:

* Marca;
* Modelo;
* Placa;
* Chassi;
* Cor;
* Ano de fabricação;
* Ano do modelo;
* Quilometragem;
* Proprietário atual;
* Data de início da posse;
* Observações.

Regras aplicadas:

* Um veículo pode ter vários proprietários ao longo do tempo;
* O sistema mantém histórico de proprietários;
* Todo veículo deve estar vinculado a um proprietário;
* O veículo pode receber várias Ordens de Serviço;
* Marca e modelo são utilizados para auxiliar na rastreabilidade das peças aplicáveis.

---

### 3.3 Histórico de Proprietários

O histórico de proprietários registra a relação entre cliente e veículo ao longo do tempo.

Esse módulo é importante porque um mesmo veículo pode trocar de dono, mas ainda assim manter seu histórico de manutenção.

Dados principais:

* Cliente;
* Veículo;
* Data de início da posse;
* Data de fim da posse;
* Observações.

Regras aplicadas:

* Um veículo pode ter mais de um proprietário ao longo do tempo;
* O proprietário atual é identificado pelo histórico ativo;
* O histórico preserva a rastreabilidade entre cliente, veículo e OS.

---

### 3.4 Colaboradores

O módulo de colaboradores permite cadastrar os funcionários da oficina.

Dados principais:

* Nome;
* Telefone;
* E-mail;
* Endereço;
* Data de admissão;
* Status;
* Funções vinculadas.

Regras aplicadas:

* Todo colaborador pode ter uma ou mais funções;
* Um colaborador pode ser responsável por vários serviços em Ordens de Serviço;
* O sistema não cria entidades separadas para mecânico, atendente, gerente etc.;
* Mecânico, atendente, secretária, faxineiro e gerente são registros da entidade Função.

---

### 3.5 Funções

O módulo de funções permite cadastrar os cargos ou funções exercidas pelos colaboradores.

Exemplos:

* Mecânico;
* Atendente;
* Secretária;
* Estoquista;
* Gerente;
* Faxineiro.

Regras aplicadas:

* Uma função pode ser atribuída a vários colaboradores;
* Um colaborador deve possuir pelo menos uma função;
* A entidade Função evita a criação incorreta de entidades separadas para cada cargo.

---

### 3.6 Marcas e Modelos

O módulo de marcas e modelos permite organizar os dados dos veículos.

Dados principais de Marca:

* Nome da marca.

Dados principais de Modelo:

* Nome do modelo;
* Marca vinculada.

Regras aplicadas:

* Uma marca pode possuir vários modelos;
* Um modelo pertence a uma única marca;
* O veículo deve ser classificado por marca e modelo.

---

### 3.7 Serviços

O módulo de serviços permite cadastrar os serviços que a oficina executa.

O serviço pode ser:

* Interno;
* Terceirizado.

Dados principais:

* Nome;
* Tipo do serviço;
* Descrição;
* Valor base;
* Prazo de garantia em dias.

Regras aplicadas:

* Todo serviço deve ser classificado como interno ou terceirizado;
* Todo serviço executado em uma OS deve possuir colaborador responsável;
* Todo serviço gera garantia;
* O prazo da garantia varia conforme o tipo de serviço.

---

### 3.8 Empresas Terceirizadas

O módulo de empresas terceirizadas registra empresas externas que executam serviços para a oficina.

Dados principais:

* Nome;
* CNPJ;
* Telefone;
* E-mail;
* Endereço;
* Observações.

Regras aplicadas:

* Um serviço terceirizado pode ser executado por uma empresa externa;
* Mesmo quando o serviço é terceirizado, a oficina continua responsável perante o cliente;
* A execução terceirizada fica vinculada ao Item de Serviço da OS.

---

### 3.9 Peças e Fornecedores

O módulo de peças e fornecedores registra as peças usadas nas Ordens de Serviço e seus respectivos fornecedores.

Dados principais de Peça:

* Nome;
* Código;
* Marca;
* Aplicação;
* Valor;
* Prazo de garantia.

Dados principais de Fornecedor:

* Nome;
* CNPJ;
* Telefone;
* E-mail;
* Endereço.

Regras aplicadas:

* Toda peça utilizada em uma OS deve estar cadastrada;
* Toda peça usada deve estar vinculada a um fornecedor;
* A peça gera garantia após a finalização da OS;
* O fornecedor pode ser responsável por defeitos de peça dentro do prazo de garantia;
* A oficina continua atendendo o cliente, mesmo quando a responsabilidade é do fornecedor.

---

### 3.10 Ordens de Serviço

O módulo de Ordens de Serviço é o principal módulo operacional do sistema.

A OS representa o atendimento realizado pela oficina em um veículo de um cliente.

Dados principais:

* Cliente;
* Veículo;
* Data de abertura;
* Status;
* Prioridade;
* Observações;
* Valor total;
* Histórico de status.

Fluxo da OS:

1. Orçamento;
2. Execução;
3. Pagamento;
4. Finalizado.

Regras aplicadas:

* Toda OS pertence a um único cliente;
* Toda OS pertence a um único veículo;
* Uma OS deve possuir pelo menos um serviço;
* Uma OS pode ou não utilizar peças;
* Toda OS possui histórico de status;
* A OS só deve ser finalizada após o controle de pagamento;
* A finalização da OS inicia a contagem das garantias.

---

### 3.11 Serviços e Peças da OS

Esse módulo permite adicionar serviços e peças a uma Ordem de Serviço.

Funcionalidades:

* Selecionar uma OS;
* Adicionar serviço à OS;
* Definir colaborador responsável pelo serviço;
* Adicionar peça utilizada na OS;
* Informar fornecedor da peça;
* Atualizar valor total da OS;
* Consultar itens já vinculados.

Regras aplicadas:

* Todo Item de Serviço deve possuir colaborador responsável;
* Toda peça utilizada deve estar vinculada a uma Peça cadastrada;
* Toda peça utilizada deve possuir Fornecedor identificado;
* Item de Serviço gera Garantia de Serviço;
* Item de Peça gera Garantia de Peça.

---

### 3.12 Pagamentos

O módulo de pagamentos controla o recebimento financeiro das Ordens de Serviço.

Dados principais:

* Ordem de Serviço;
* Forma de pagamento;
* Valor pago;
* Data do pagamento;
* Status do pagamento;
* Observação.

Funcionalidades:

* Selecionar OS;
* Carregar resumo financeiro;
* Exibir total da OS;
* Exibir valor já pago;
* Exibir saldo pendente;
* Registrar novo pagamento;
* Atualizar tabela de pagamentos;
* Atualizar resumo financeiro automaticamente.

Regras aplicadas:

* Uma OS pode não ter pagamento enquanto estiver em orçamento ou execução;
* Uma OS pode ter um ou vários pagamentos;
* O valor pendente é calculado com base no total da OS menos os pagamentos registrados;
* Ao registrar pagamento suficiente para quitação, a OS pode avançar no fluxo conforme regra do backend.

---

### 3.13 Garantias

O módulo de garantias controla as garantias de peças e serviços.

Tipos de garantia:

* Garantia de peça;
* Garantia de serviço.

Dados principais:

* Ordem de Serviço;
* Cliente;
* Item garantido;
* Data de início;
* Data de fim;
* Status;
* Responsabilidade;
* Observações.

Status possíveis:

* Ativa;
* Acionada;
* Encerrada;
* Expirada.

Regras aplicadas:

* Toda peça usada em OS gera garantia;
* Todo serviço executado em OS gera garantia;
* A garantia da peça começa após a finalização da OS;
* A garantia do serviço varia conforme o tipo de serviço;
* Se a peça apresentar defeito dentro da garantia, a oficina atende o cliente, mas a responsabilidade pode ser do fornecedor.

---

### 3.14 Fila de Atendimento

A Fila de Atendimento representa a aplicação prática de estrutura de dados no sistema.

Ela organiza as Ordens de Serviço aguardando atendimento ou execução.

Funcionalidades:

* Exibir OS aguardando atendimento;
* Ordenar por data de abertura;
* Ordenar por prioridade;
* Ordenar por valor total;
* Consultar OS na fila;
* Apoiar o controle operacional da oficina.

Justificativa acadêmica:

* A fila representa uma estrutura de dados linear;
* O controle por ordem de chegada simula o atendimento real da oficina;
* A ordenação manual permite aplicar algoritmo próprio sem depender de bibliotecas prontas.

---

### 3.15 Geração de Nota/Recibo em PDF

O sistema possui geração de documento PDF baseado nos dados da Ordem de Serviço.

Esse documento funciona como uma nota/recibo interno simplificado para controle da oficina.

O PDF contém:

* Cabeçalho da oficina;
* Dados do cliente;
* Dados do veículo;
* Dados da OS;
* Serviços executados;
* Peças utilizadas;
* Fornecedores;
* Pagamentos;
* Totais;
* Saldo pendente;
* Observações;
* Campo de assinatura do cliente;
* Campo de assinatura da oficina.

Endpoint backend:

```http
GET /api/notas-fiscais/ordens-servico/{id}/pdf
```

No frontend, o botão fica em:

```text
Operação → Ordens de Serviço → Coluna Ações → Nota PDF
```

Observação importante:

Este documento é uma nota/recibo interno simplificado para fins acadêmicos e de controle operacional. Ele não substitui uma Nota Fiscal Eletrônica oficial autorizada por prefeitura, SEFAZ ou órgão fiscal competente.

---

## 4. Arquitetura do Sistema

O sistema utiliza arquitetura monolítica em camadas.

A aplicação foi organizada para separar responsabilidades, facilitar manutenção e atender aos requisitos acadêmicos do Projeto Integrador.

Camadas principais:

```text
Model
DTO
Repository
Validation
Service
Controller
Response
View
```

### 4.1 Model

A camada Model representa as entidades do domínio.

Exemplos:

* Pessoa;
* Cliente;
* PessoaFisica;
* PessoaJuridica;
* Colaborador;
* Funcao;
* Veiculo;
* OrdemServico;
* ItemServico;
* ItemPeca;
* Peca;
* Fornecedor;
* Pagamento;
* GarantiaPeca;
* GarantiaServico.

Também existe uma estrutura genérica com BaseModel para padronizar entidades.

---

### 4.2 DTO

A camada DTO é utilizada para transportar dados entre frontend e backend.

Ela evita expor diretamente as entidades do banco na API.

DTOs podem ser usados para:

* Cadastro;
* Atualização;
* Consulta;
* Resumo;
* Resposta simplificada;
* Geração de documentos.

---

### 4.3 Repository

A camada Repository é responsável pela comunicação com o banco de dados.

O projeto utiliza Spring Data JPA para simplificar operações de persistência.

Funções da camada:

* Salvar registros;
* Atualizar registros;
* Buscar por ID;
* Listar registros;
* Consultar dados relacionados;
* Verificar existência de registros.

---

### 4.4 Validation

A camada Validation concentra validações de regra de negócio antes de salvar ou alterar dados.

Exemplos de validações:

* Cliente deve ter tipo válido;
* CPF/CNPJ não deve ser vazio quando obrigatório;
* Colaborador deve ter pelo menos uma função;
* OS deve ter cliente e veículo;
* Serviço da OS deve ter colaborador responsável;
* Peça da OS deve ter fornecedor;
* Pagamento não pode violar regra financeira;
* Garantia deve estar vinculada a item válido.

---

### 4.5 Service

A camada Service concentra as regras de negócio.

Responsabilidades:

* Orquestrar operações;
* Aplicar validações;
* Chamar repositories;
* Controlar fluxo de OS;
* Calcular totais;
* Registrar histórico;
* Controlar garantias;
* Gerar PDF;
* Preparar respostas para controller.

---

### 4.6 Controller

A camada Controller expõe os endpoints REST da aplicação.

Responsabilidades:

* Receber requisições HTTP;
* Chamar services;
* Retornar respostas padronizadas;
* Disponibilizar endpoints para o Angular;
* Expor geração de PDF.

---

### 4.7 Response

A camada Response padroniza o retorno da API.

Ela ajuda o frontend a interpretar respostas de forma consistente.

Exemplo de retorno esperado:

```json
{
  "sucesso": true,
  "mensagem": "Registro salvo com sucesso.",
  "dados": {}
}
```

---

### 4.8 View

A camada View é implementada em Angular.

Ela consome a API REST do backend e apresenta as telas para o usuário.

Responsabilidades:

* Exibir formulários;
* Exibir tabelas;
* Realizar consultas;
* Enviar dados para API;
* Exibir mensagens de sucesso e erro;
* Baixar documentos PDF;
* Manter interface responsiva.

---

## 5. Tecnologias Utilizadas

### Backend

* Java;
* Spring Boot;
* Spring Web;
* Spring Data JPA;
* Bean Validation;
* Maven;
* PostgreSQL;
* OpenPDF para geração de documentos PDF.

### Frontend

* Angular;
* TypeScript;
* HTML;
* CSS;
* RxJS;
* Angular Router;
* Serviços HTTP;
* Layout responsivo.

### Banco de Dados

* PostgreSQL;
* Scripts SQL para criação, seed e verificações;
* Funcionamento local.

### Documentação

* README;
* ADRs;
* Documentação de banco;
* Scripts de verificação;
* Modelo conceitual, lógico e físico;
* Documentos acadêmicos do projeto.

---

## 6. Estrutura Geral do Projeto

Estrutura resumida:

```text
car-repair/
├── backend/
│   └── src/
│       └── main/
│           ├── java/
│           └── resources/
│
├── frontend/
│   └── oficina-web/
│       ├── src/
│       │   ├── app/
│       │   ├── assets/
│       │   └── index.html
│       ├── public/
│       ├── angular.json
│       └── package.json
│
├── database/
│   ├── 01_schema/
│   ├── 02_seed/
│   ├── 03_verificacoes/
│   └── README_BANCO.md
│
├── docs/
│   ├── adr/
│   └── relatorios/
│
├── scripts/
│
├── README.md
└── pom.xml
```

Observação:

A estrutura pode variar levemente conforme a versão do projeto, mas a organização principal segue backend, frontend, database, docs e scripts.

---

## 7. Banco de Dados

O banco de dados foi pensado para funcionar localmente.

O sistema utiliza PostgreSQL.

### 7.1 Criação do Banco

Crie o banco no PostgreSQL:

```sql
CREATE DATABASE oficina_mecanica;
```

Depois execute os scripts SQL do projeto.

Organização recomendada:

```text
database/
├── 01_schema/
│   └── criação das tabelas
├── 02_seed/
│   └── dados iniciais
├── 03_verificacoes/
│   └── consultas de verificação
└── README_BANCO.md
```

### 7.2 Estratégia de Scripts

Como o banco será criado manualmente, o projeto mantém scripts SQL para:

* Criar tabelas;
* Criar constraints;
* Criar relacionamentos;
* Inserir dados iniciais;
* Validar dados cadastrados;
* Verificar comunicação com frontend;
* Verificar geração de PDF.

Essa abordagem permite recriar o banco em outro computador sem depender de internet.

---

## 8. Configuração do Backend

### 8.1 Pré-requisitos

Instalar:

* Java compatível com o projeto;
* Maven;
* PostgreSQL;
* IDE de preferência, como NetBeans, IntelliJ ou VS Code.

### 8.2 Configuração do Banco

No arquivo de configuração do Spring Boot, ajuste os dados do banco.

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/oficina_mecanica
spring.datasource.username=postgres
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Observação:

O projeto utiliza scripts SQL, portanto recomenda-se manter:

```properties
spring.jpa.hibernate.ddl-auto=none
```

Assim o Hibernate não tenta recriar o banco automaticamente.

---

### 8.3 Rodando o Backend

Na raiz do projeto:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn spring-boot:run
```

Após iniciar, a API deve ficar disponível em:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 9. Configuração do Frontend

### 9.1 Pré-requisitos

Instalar:

* Node.js;
* npm;
* Angular CLI.

Como o projeto foi ajustado para Angular mais recente, recomenda-se usar Node compatível com a versão do Angular instalada.

### 9.2 Caminho Correto do Angular

O projeto Angular fica em:

```powershell
C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
```

Erro comum:

Rodar `npm.cmd start` dentro da pasta errada:

```powershell
C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend
```

Essa pasta não contém `package.json`.

O comando correto é:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

Ou, se estiver usando proxy:

```powershell
npm.cmd run start:proxy
```

---

### 9.3 Acesso ao Frontend

Após rodar o Angular, acesse:

```text
http://localhost:4200
```

Caso alterações visuais ou favicon não apareçam, pressione:

```text
CTRL + F5
```

Isso força o navegador a limpar cache da aplicação.

---

## 10. Integração Angular com Backend

O Angular se comunica com o backend por meio de services HTTP.

A comunicação segue este fluxo:

```text
Componente Angular
→ Service Angular
→ API REST Spring Boot
→ Service Backend
→ Repository
→ Banco de Dados PostgreSQL
```

Após o retorno da API:

```text
Banco de Dados
→ Repository
→ Service Backend
→ Controller
→ Response
→ Service Angular
→ Componente Angular
→ Tela atualizada
```

Comportamento obrigatório das telas:

* Carregar dados automaticamente ao abrir;
* Atualizar listagem após salvar;
* Atualizar listagem após editar;
* Atualizar listagem após inativar;
* Atualizar listagem após registrar pagamento;
* Atualizar dados após adicionar serviço ou peça;
* Evitar depender de clique manual para atualizar;
* Exibir mensagens claras de erro ou sucesso.

---

## 11. Organização Visual do Sistema

O frontend foi reformulado para parecer um sistema administrativo real de oficina.

Características visuais:

* Menu superior por categorias;
* Paleta baseada na identidade da oficina;
* Azul automotivo;
* Laranja de destaque;
* Cinza metálico;
* Formulários organizados;
* Tabelas profissionais;
* Botões padronizados;
* Badges de status;
* Layout responsivo;
* Logos responsivas.

---

## 12. Menu do Sistema

O menu principal é organizado por categorias.

### Início

* Visão Geral da Oficina.

### Operação

* Ordens de Serviço;
* Serviços e Peças da OS;
* Fila de Atendimento;
* Pagamentos;
* Garantias.

### Cadastros

* Clientes;
* Veículos;
* Colaboradores;
* Funções;
* Marcas e Modelos;
* Serviços;
* Empresas Terceirizadas;
* Peças e Fornecedores.

### Gestão

* Relatórios;
* Configurações.

---

## 13. Tela Inicial — Visão Geral da Oficina

A tela inicial exibe um resumo operacional da oficina.

Indicadores:

* Ordens abertas;
* OS em execução;
* OS aguardando pagamento;
* Garantias ativas;
* Clientes cadastrados;
* Veículos cadastrados.

Atalhos:

* Nova OS;
* Novo Cliente;
* Novo Veículo;
* Registrar Pagamento;
* Consultar Garantia;
* Fila de Atendimento.

Também exibe uma tabela resumida de últimas Ordens de Serviço.

---

## 14. Fluxo Completo de Uso

### 14.1 Cadastro Inicial

Fluxo recomendado:

1. Cadastrar funções;
2. Cadastrar colaboradores;
3. Cadastrar marcas;
4. Cadastrar modelos;
5. Cadastrar clientes;
6. Cadastrar veículos;
7. Cadastrar serviços;
8. Cadastrar fornecedores;
9. Cadastrar peças;
10. Abrir Ordem de Serviço.

---

### 14.2 Abertura de OS

Fluxo:

1. Acessar Ordens de Serviço;
2. Selecionar cliente;
3. Selecionar veículo;
4. Informar data de abertura;
5. Informar observações;
6. Salvar OS.

A OS inicia no status:

```text
Orçamento
```

---

### 14.3 Inclusão de Serviços e Peças

Fluxo:

1. Acessar Serviços e Peças da OS;
2. Selecionar Ordem de Serviço;
3. Adicionar serviço;
4. Selecionar colaborador responsável;
5. Adicionar peça se necessário;
6. Selecionar fornecedor da peça;
7. Salvar itens.

O sistema atualiza o valor da OS.

---

### 14.4 Execução

Após aprovação do orçamento, a OS pode avançar para execução.

Status:

```text
Execução
```

Nesse momento, a oficina realiza os serviços e aplica as peças.

---

### 14.5 Pagamento

Após conclusão da execução, a OS entra em controle de pagamento.

Status:

```text
Pagamento
```

Fluxo:

1. Acessar Pagamentos;
2. Selecionar OS;
3. Ver total da OS;
4. Ver valor já pago;
5. Ver valor pendente;
6. Registrar pagamento;
7. Salvar.

---

### 14.6 Finalização

Após pagamento suficiente, a OS pode ser finalizada.

Status:

```text
Finalizado
```

Ao finalizar:

* A garantia das peças passa a contar;
* A garantia dos serviços passa a contar;
* A OS fica disponível para emissão de documento em PDF.

---

### 14.7 Geração de PDF

Fluxo:

1. Acessar Ordens de Serviço;
2. Localizar a OS;
3. Ir na coluna Ações;
4. Clicar em Nota PDF;
5. O navegador abre ou baixa o PDF.

---

## 15. Padrões de Projeto Aplicados

O sistema AV CAR AUTO CENTER implementa os seis padrões de projeto exigidos pela disciplina:

```text
Singleton
Adapter
Iterator
Template Method
Factory Method
Decorator
```

Cada padrão foi aplicado em uma parte específica do sistema, com objetivo funcional e justificativa técnica. As classes possuem comentários no código identificando o padrão aplicado, facilitando a avaliação e apresentação do projeto.

---

### 15.1 Singleton

#### Localização no projeto

```text
src/main/java/br/com/avcar/oficina/core/designpattern/singleton/DatabaseConnectionSingleton.java
```

#### Classe principal

```text
DatabaseConnectionSingleton
```

#### Onde é aplicado

O padrão Singleton foi aplicado na verificação centralizada da conexão local com o banco de dados PostgreSQL.

Embora o sistema utilize Spring Data JPA para persistência, essa classe foi criada para demonstrar o padrão Singleton e permitir uma verificação técnica da disponibilidade do banco local.

#### Justificativa

Como o sistema da oficina deve funcionar localmente, sem dependência obrigatória de internet, é importante existir um ponto único para verificar se o banco de dados está disponível.

O Singleton garante que exista apenas uma instância responsável por essa verificação, evitando duplicidade de configuração e espalhamento da lógica de conexão pelo sistema.

#### Responsabilidade no sistema

```text
Centralizar a checagem técnica da conexão local com PostgreSQL.
```

---

### 15.2 Factory Method

#### Localização no projeto

```text
src/main/java/br/com/avcar/oficina/business/pessoa/designpattern/factory/
```

#### Classes principais

```text
ClienteFactoryMethod.java
ClienteCadastroFactory.java
ClientePessoaFisicaFactory.java
ClientePessoaJuridicaFactory.java
```

#### Onde é aplicado

O padrão Factory Method foi aplicado no cadastro de clientes, especificamente na criação de Cliente Pessoa Física e Cliente Pessoa Jurídica.

O sistema possui a regra de que todo cliente deve ser classificado como Pessoa Física ou Pessoa Jurídica, nunca os dois ao mesmo tempo. Dessa forma, a criação do cliente não deve ficar espalhada diretamente no Controller ou no Service.

#### Funcionamento

O fluxo de criação é organizado da seguinte forma:

```text
ClienteService
        ↓
ClienteCadastroFactory
        ↓
ClienteFactoryMethod
        ↓
ClientePessoaFisicaFactory ou ClientePessoaJuridicaFactory
        ↓
PessoaModel + ClienteModel + Especialização PF/PJ
```

#### Justificativa

O Factory Method foi escolhido porque o cadastro de cliente possui especializações diferentes.

A fábrica permite criar corretamente a estrutura de objetos conforme o tipo do cliente informado:

```text
Pessoa Física → PessoaModel + ClienteModel + PessoaFisicaModel
Pessoa Jurídica → PessoaModel + ClienteModel + PessoaJuridicaModel
```

Isso melhora a organização, reduz duplicidade e respeita a generalização/especialização definida no modelo de banco de dados.

#### Responsabilidade no sistema

```text
Criar clientes Pessoa Física e Pessoa Jurídica de forma organizada, extensível e compatível com o modelo conceitual.
```

---

### 15.3 Adapter

#### Localização no projeto

```text
src/main/java/br/com/avcar/oficina/business/veiculo/adapter/VeiculoResponseAdapter.java
```

#### Classe principal

```text
VeiculoResponseAdapter
```

#### Onde é aplicado

O padrão Adapter foi aplicado na montagem das respostas de veículo enviadas para o frontend Angular.

No banco de dados e no domínio, os dados de veículo estão distribuídos entre várias entidades:

```text
VeiculoModel
ModeloModel
MarcaModel
HistoricoProprietarioModel
ClienteModel
PessoaModel
```

Porém, para a tela Angular, é mais adequado receber uma resposta consolidada com:

```text
placa
marca
modelo
proprietário atual
histórico de proprietários
dados resumidos do veículo
```

#### Justificativa

O Adapter foi escolhido porque a estrutura interna do domínio não deve ser exposta diretamente para a View.

Ele converte entidades internas complexas em DTOs mais adequados para consumo pelo frontend.

#### DTOs gerados pelo Adapter

```text
VeiculoDTO
VeiculoResumoDTO
HistoricoProprietarioDTO
```

#### Responsabilidade no sistema

```text
Adaptar os dados internos de veículo, modelo, marca e histórico de proprietário para respostas apropriadas ao Angular.
```

---

### 15.4 Iterator

#### Localização no projeto

```text
src/main/java/br/com/avcar/oficina/core/estrutura/iterator/OficinaIterator.java
src/main/java/br/com/avcar/oficina/core/estrutura/fila/FilaAtendimentoIterator.java
src/main/java/br/com/avcar/oficina/core/estrutura/lista/ListaLinearIterator.java
```

#### Classes principais

```text
OficinaIterator
FilaAtendimentoIterator
ListaLinearIterator
```

#### Onde é aplicado

O padrão Iterator foi aplicado nas estruturas lineares utilizadas no controle operacional da oficina, principalmente na Fila de Atendimento das Ordens de Serviço.

A fila organiza as OS aguardando atendimento, respeitando uma lógica próxima ao funcionamento real de uma oficina mecânica.

#### Funcionamento

O Iterator permite percorrer a estrutura sem expor sua implementação interna.

Exemplo conceitual:

```text
Fila de Atendimento
        ↓
FilaAtendimentoIterator
        ↓
hasNext()
        ↓
next()
        ↓
reset()
```

#### Justificativa

O Iterator foi escolhido porque o sistema utiliza estruturas lineares customizadas e precisa percorrer seus elementos de forma controlada.

Com isso, o Service ou Controller não precisa conhecer os nós internos da fila ou da lista.

#### Responsabilidade no sistema

```text
Percorrer filas e listas lineares de Ordens de Serviço sem expor a estrutura interna.
```

---

### 15.5 Template Method

#### Localização no projeto

```text
src/main/java/br/com/avcar/oficina/core/estrutura/ordenacao/OrdenadorTemplate.java
src/main/java/br/com/avcar/oficina/business/ordemservico/estrutura/ordenacao/
```

#### Classes principais

```text
OrdenadorTemplate.java
OrdenadorOrdemServicoPorDataAbertura.java
OrdenadorOrdemServicoPorPrioridade.java
OrdenadorOrdemServicoPorValorTotal.java
```

#### Onde é aplicado

O padrão Template Method foi aplicado no algoritmo manual de ordenação das Ordens de Serviço.

O sistema permite ordenar a fila/listagem de OS por diferentes critérios:

```text
Data de abertura
Prioridade
Valor total
```

#### Funcionamento

A classe abstrata `OrdenadorTemplate` define o esqueleto fixo do algoritmo de ordenação.

As subclasses alteram apenas o critério de comparação.

Fluxo conceitual:

```text
OrdenadorTemplate
        ↓
Método ordenar()
        ↓
Algoritmo de ordenação manual
        ↓
comparar()
        ↓
Subclasse define o critério
```

#### Justificativa

Esse padrão foi escolhido porque permite reaproveitar o mesmo algoritmo de ordenação, alterando somente a regra de comparação.

Além disso, atende ao requisito acadêmico de implementar um algoritmo de ordenação manual, sem depender de bibliotecas prontas como `Collections.sort()` ou `Stream.sorted()`.

#### Responsabilidade no sistema

```text
Definir o esqueleto do algoritmo de ordenação manual e permitir variação do critério por subclasses.
```

---

### 15.6 Decorator

#### Localização no projeto

```text
src/main/java/br/com/avcar/oficina/core/designpattern/decorator/
```

#### Classes principais

```text
Notificador.java
NotificadorOperacional.java
NotificadorDecorator.java
NotificadorAuditoriaDecorator.java
```

#### Classes de apoio

```text
src/main/java/br/com/avcar/oficina/core/notification/dto/NotificacaoDTO.java
src/main/java/br/com/avcar/oficina/core/notification/dto/NotificacaoResultadoDTO.java
src/main/java/br/com/avcar/oficina/core/notification/service/NotificacaoService.java
src/main/java/br/com/avcar/oficina/core/notification/controller/NotificacaoController.java
```

#### Onde é aplicado

O padrão Decorator foi aplicado no mecanismo de notificação interna com auditoria operacional.

Quando ocorre uma operação importante, como alteração de status de Ordem de Serviço, o sistema pode gerar uma notificação interna e adicionar informações de auditoria sem modificar a classe principal de notificação.

#### Funcionamento

A composição ocorre da seguinte forma:

```text
NotificadorOperacional
        ↓ decorado por
NotificadorAuditoriaDecorator
        ↓
Notificação com auditoria
```

#### Justificativa

O Decorator foi escolhido porque permite adicionar comportamento extra a uma notificação sem alterar diretamente a classe base.

Isso respeita o princípio de extensão sem modificação e melhora a rastreabilidade do sistema.

#### Responsabilidade no sistema

```text
Adicionar auditoria a notificações internas sem alterar a classe principal de notificação.
```

---

## 16. Resumo dos Padrões de Projeto

| Padrão          | Localização                                                              | Classes principais                                                                                                                      | Aplicação no sistema                                            |
| --------------- | ------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------- |
| Singleton       | `core/designpattern/singleton`                                           | `DatabaseConnectionSingleton`                                                                                                           | Verificação centralizada da conexão local com PostgreSQL        |
| Factory Method  | `business/pessoa/designpattern/factory`                                  | `ClienteFactoryMethod`, `ClienteCadastroFactory`, `ClientePessoaFisicaFactory`, `ClientePessoaJuridicaFactory`                          | Criação de clientes Pessoa Física e Pessoa Jurídica             |
| Adapter         | `business/veiculo/adapter`                                               | `VeiculoResponseAdapter`                                                                                                                | Adaptação de dados de veículo para DTOs consumidos pelo Angular |
| Iterator        | `core/estrutura/iterator`, `core/estrutura/fila`, `core/estrutura/lista` | `OficinaIterator`, `FilaAtendimentoIterator`, `ListaLinearIterator`                                                                     | Percurso de fila e lista linear sem expor implementação interna |
| Template Method | `core/estrutura/ordenacao`, `business/ordemservico/estrutura/ordenacao`  | `OrdenadorTemplate`, `OrdenadorOrdemServicoPorDataAbertura`, `OrdenadorOrdemServicoPorPrioridade`, `OrdenadorOrdemServicoPorValorTotal` | Ordenação manual de Ordens de Serviço por critérios diferentes  |
| Decorator       | `core/designpattern/decorator`                                           | `Notificador`, `NotificadorOperacional`, `NotificadorDecorator`, `NotificadorAuditoriaDecorator`                                        | Notificação interna com auditoria operacional                   |

---

## 17. Endpoint de Apoio para Demonstração dos Padrões

O sistema também possui um endpoint acadêmico para listar os padrões de projeto aplicados.

```http
GET /api/padroes-projeto
```

Esse endpoint pode ser acessado pelo Swagger e ajuda na apresentação do projeto, pois mostra diretamente pela API quais padrões foram utilizados e onde estão aplicados.

Localização do controller:

```text
src/main/java/br/com/avcar/oficina/core/designpattern/catalog/controller/PadraoProjetoController.java
```

DTO utilizado:

```text
src/main/java/br/com/avcar/oficina/core/designpattern/catalog/dto/PadraoProjetoDTO.java
```

Esse recurso não faz parte da operação diária da oficina. Ele foi criado como apoio acadêmico para facilitar a validação dos padrões de projeto exigidos pela disciplina.

---

## 18. Relação dos Padrões com os Requisitos do Projeto

A aplicação dos padrões de projeto contribui para a organização e manutenção do sistema.

### Singleton

Contribui para o requisito de funcionamento local, pois centraliza a verificação da conexão com o banco PostgreSQL.

### Factory Method

Contribui para a regra de negócio de clientes, garantindo que o cliente seja corretamente criado como Pessoa Física ou Pessoa Jurídica.

### Adapter

Contribui para a integração entre backend e frontend, adaptando dados complexos do domínio para respostas simples e adequadas à tela.

### Iterator

Contribui para a estrutura de dados da Fila de Atendimento, permitindo percorrer Ordens de Serviço sem expor a implementação interna.

### Template Method

Contribui para o algoritmo de ordenação manual, permitindo ordenar Ordens de Serviço por data, prioridade ou valor total.

### Decorator

Contribui para rastreabilidade e auditoria, adicionando comportamento extra às notificações internas sem modificar a classe base.

---

## 19. Evidência dos Comentários no Código

As classes relacionadas aos padrões de projeto possuem comentários identificando explicitamente sua aplicação.

Exemplos de comentários presentes no código:

```text
PADRÃO DE PROJETO: SINGLETON
PADRÃO DE PROJETO: ADAPTER
PADRÃO DE PROJETO: ITERATOR
PADRÃO DE PROJETO: TEMPLATE METHOD
PADRÃO DE PROJETO: FACTORY METHOD
PADRÃO DE PROJETO: DECORATOR
```

Esses comentários foram adicionados para facilitar a correção acadêmica e demonstrar claramente onde cada padrão está aplicado no sistema.

Também recomendo trocar a numeração das próximas seções do README, porque essa versão expande bastante a parte dos padrões.

## 20. Estrutura de Dados Aplicada

O projeto aplica estrutura de dados no módulo de Fila de Atendimento.

### 16.1 Fila

Uso:

* Controle de Ordens de Serviço aguardando atendimento.

Justificativa:

* Representa a ordem de chegada dos veículos;
* Facilita priorização;
* Simula funcionamento real de oficina.

---

### 16.2 Ordenação Manual

Uso:

* Ordenar OS por data;
* Ordenar OS por prioridade;
* Ordenar OS por valor.

Justificativa:

* Atende ao requisito acadêmico de algoritmo manual;
* Evita uso exclusivo de bibliotecas prontas;
* Ajuda no gerenciamento operacional.

---

### 16.3 Pesquisa

Uso:

* Buscar OS por cliente;
* Buscar OS por veículo;
* Buscar OS por placa;
* Buscar OS por número.

Justificativa:

* Facilita localização de registros;
* Apoia atendimento rápido;
* Reduz tempo de consulta.

---

## 21. Regras de Negócio Consolidadas

Principais regras:

1. Toda pessoa cadastrada deve ser Cliente, Colaborador ou ambos;
2. Todo cliente deve ser Pessoa Física ou Pessoa Jurídica;
3. Um veículo pode ter vários proprietários ao longo do tempo;
4. Todo veículo deve possuir histórico de proprietário;
5. Cliente pode solicitar várias OS;
6. Toda OS pertence a um único cliente;
7. Toda OS pertence a um único veículo;
8. Toda OS deve possuir histórico de status;
9. A OS segue o fluxo Orçamento, Execução, Pagamento e Finalizado;
10. Toda OS deve possuir ao menos um serviço;
11. Todo serviço da OS deve possuir colaborador responsável;
12. Colaborador pode possuir uma ou mais funções;
13. Mecânico é uma função, não uma entidade separada;
14. Todo serviço é interno ou terceirizado;
15. Serviço terceirizado gera execução terceirizada;
16. Mesmo terceirizando, a oficina continua responsável perante o cliente;
17. OS pode ou não utilizar peças;
18. Toda peça usada deve estar registrada como ItemPeca;
19. Toda peça usada deve ter fornecedor identificado;
20. ItemPeca gera GarantiaPeca;
21. ItemServico gera GarantiaServico;
22. Garantia da peça começa após finalização da OS;
23. Garantia do serviço varia conforme o tipo;
24. OS pode gerar nenhum, um ou vários pagamentos;
25. O sistema deve priorizar rastreabilidade;
26. O sistema deve funcionar localmente.

---

## 22. Principais Endpoints

Os endpoints podem variar conforme implementação, mas seguem a organização REST.

Exemplos:

```http
GET    /api/clientes
POST   /api/clientes
PUT    /api/clientes/{id}
GET    /api/clientes/{id}

GET    /api/veiculos
POST   /api/veiculos
PUT    /api/veiculos/{id}

GET    /api/colaboradores
POST   /api/colaboradores

GET    /api/funcoes
POST   /api/funcoes

GET    /api/ordens-servico
POST   /api/ordens-servico
PUT    /api/ordens-servico/{id}

GET    /api/pagamentos
POST   /api/pagamentos

GET    /api/garantias

GET    /api/notas-fiscais/ordens-servico/{id}/pdf
```

Documentação interativa:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 23. Geração de PDF

A geração de PDF usa o backend.

Biblioteca utilizada:

```xml
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>1.3.43</version>
</dependency>
```

Fluxo:

```text
Angular
→ chama endpoint de PDF
→ backend busca dados da OS
→ backend monta documento
→ backend retorna application/pdf
→ navegador baixa ou abre o arquivo
```

Formato do documento:

* Cabeçalho;
* Dados da oficina;
* Dados do cliente;
* Dados do veículo;
* Itens da OS;
* Serviços;
* Peças;
* Pagamentos;
* Totais;
* Assinaturas.

---

## 24. Logos e Identidade Visual

O sistema utiliza logos responsivas para diferentes tamanhos de tela.

Arquivos principais:

```text
src/assets/branding/
├── av-car-logo-horizontal.png
├── av-car-logo-header.png
├── av-car-logo-header-sm.png
├── av-car-logo-icon.png
├── av-car-logo-stacked.png
├── favicon.ico
├── favicon-16x16.png
├── favicon-32x32.png
├── apple-touch-icon.png
├── android-chrome-192x192.png
└── android-chrome-512x512.png
```

Uso:

* Logo horizontal para telas grandes;
* Logo reduzida para telas médias;
* Ícone para telas pequenas;
* Favicon para aba do navegador.

---

## 25. Paleta Visual

A paleta visual foi pensada para parecer com uma oficina mecânica moderna.

Cores principais:

```css
Azul automotivo escuro
Laranja de destaque
Cinza metálico
Grafite
Branco
```

Objetivo:

* Passar identidade automotiva;
* Melhorar contraste;
* Deixar o sistema mais profissional;
* Facilitar leitura;
* Manter aparência moderna.

---

## 26. Responsividade

O sistema foi ajustado para funcionar em:

* Desktop;
* Notebook;
* Telas médias;
* Telas menores.

Comportamentos:

* Menu superior por categorias;
* Tabelas com rolagem horizontal;
* Formulários adaptáveis;
* Logos responsivas;
* Cards reorganizados;
* Botões acessíveis.

---

## 27. Como Testar o Sistema

### 23.1 Teste do Backend

1. Iniciar PostgreSQL;
2. Verificar banco criado;
3. Rodar scripts SQL;
4. Iniciar Spring Boot;
5. Abrir Swagger;
6. Testar endpoint de clientes ou OS.

Comando:

```powershell
mvn spring-boot:run
```

---

### 23.2 Teste do Frontend

1. Entrar na pasta correta;
2. Instalar dependências;
3. Rodar Angular;
4. Acessar navegador.

Comandos:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

---

### 23.3 Teste de Cadastro

Teste recomendado:

1. Cadastrar cliente;
2. Verificar se aparece na tabela;
3. Cadastrar veículo;
4. Verificar vínculo com cliente;
5. Cadastrar serviço;
6. Cadastrar colaborador;
7. Abrir OS;
8. Adicionar serviço;
9. Adicionar peça;
10. Registrar pagamento;
11. Gerar PDF.

---

## 28. Problemas Comuns

### 24.1 Erro: package.json não encontrado

Erro:

```text
Could not read package.json
```

Causa:

O comando npm foi executado na pasta errada.

Solução:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

---

### 24.2 Dados não aparecem na tela

Possíveis causas:

* Backend desligado;
* Banco sem dados;
* Endpoint com erro;
* Cache do navegador;
* Angular rodando versão antiga.

Soluções:

```text
1. Verificar se backend está rodando.
2. Verificar se PostgreSQL está ligado.
3. Verificar console do navegador.
4. Usar CTRL + F5.
5. Rodar scripts de verificação.
```

---

### 24.3 PDF não abre

Possíveis causas:

* Backend desligado;
* OS inexistente;
* Endpoint de PDF com erro;
* Bloqueio de popup/download no navegador.

Soluções:

```text
1. Verificar se a OS existe.
2. Testar endpoint no Swagger.
3. Verificar console do navegador.
4. Permitir download no navegador.
```

---

### 24.4 Favicon ou logo antiga aparece

Causa:

Cache do navegador.

Solução:

```text
CTRL + F5
```

Ou limpar cache manualmente.

---

### 24.5 Erro de conexão com banco

Verificar:

```text
1. PostgreSQL está iniciado.
2. Banco existe.
3. Usuário e senha estão corretos.
4. application.properties está configurado.
5. Scripts foram executados.
```

---

## 29. Documentação Técnica

A documentação do projeto inclui:

```text
docs/
├── adr/
├── relatorios/
└── README_ETAPAS...
```

As ADRs registram decisões arquiteturais importantes, como:

* Uso de arquitetura monolítica;
* Uso de Angular no frontend;
* Uso de Spring Boot no backend;
* Uso de PostgreSQL;
* Organização dos scripts SQL;
* Reformulação visual;
* Geração de PDF;
* Logos responsivas.

---

## 30. Observações Acadêmicas

O projeto atende aos seguintes pontos acadêmicos:

* Levantamento de requisitos;
* Modelagem de banco de dados;
* MER;
* Modelo lógico;
* Modelo físico;
* Arquitetura monolítica;
* Implementação em camadas;
* Uso de Generics;
* Uso de DTO;
* Repository;
* Validation;
* Service;
* Controller;
* Response;
* View;
* Padrões de projeto;
* Estrutura de dados;
* Algoritmo de ordenação;
* Pesquisa;
* Documentação do projeto;
* Geração de artefatos;
* Sistema funcionando.


---

## Atualização — Etapa 54: correção da busca da OS sem duplicidade visual

Foi corrigida a tela **Ordens de Serviço** para evitar que o cliente e o veículo selecionados apareçam duplicados no campo de busca e no campo de seleção.

### Ajustes realizados

* O campo de busca do cliente agora é usado apenas para pesquisa.
* O campo de busca do veículo agora é usado apenas para pesquisa.
* Após selecionar cliente ou veículo, o texto da busca é limpo automaticamente.
* O valor selecionado permanece somente no `select` de cliente ou veículo.
* A edição de OS também não preenche mais os campos de pesquisa com os dados selecionados.
* A regra cliente → veículo foi preservada.
* A ação de inativação da OS foi mantida no componente para compatibilidade com o botão existente na interface.

### Arquivo alterado

```text
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
```

---

## 31. Limitações Conhecidas

O sistema é um projeto acadêmico e possui algumas limitações:

* Não emite NF-e ou NFS-e oficial;
* O PDF gerado é um documento interno simplificado;
* Não possui integração fiscal real com prefeitura ou SEFAZ;
* Não possui autenticação completa por perfil de usuário, caso ainda não esteja implementada;
* Não possui controle avançado de estoque, salvo se expandido;
* Depende do backend local para funcionamento completo;
* Depende do PostgreSQL local configurado.

---

## 32. Possíveis Melhorias Futuras

Melhorias que podem ser implementadas futuramente:

* Login com controle de permissões;
* Perfis de usuário;
* Dashboard gerencial avançado;
* Relatórios financeiros;
* Controle de estoque de peças;
* Emissão fiscal oficial;
* Integração com WhatsApp;
* Integração com e-mail;
* Backup automático do banco;
* Histórico de alterações por usuário;
* Impressão direta de OS;
* Upload de fotos do veículo;
* Laudos técnicos;
* Assinatura digital;
* Módulo de orçamento separado;
* Controle de agendamento;
* Controle de fornecedores por histórico de garantia.

---

## 33. Comandos Principais

### Backend

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn spring-boot:run
```

### Frontend

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

### Frontend com proxy

```powershell
npm.cmd run start:proxy
```

### Limpar cache visual

```text
CTRL + F5
```

---

## 34. Resumo Final

O AV CAR AUTO CENTER é um sistema de gestão de oficina mecânica desenvolvido com arquitetura monolítica em camadas, backend em Spring Boot, frontend em Angular e banco PostgreSQL.

O sistema controla todo o ciclo operacional da oficina:

```text
Cliente
→ Veículo
→ Ordem de Serviço
→ Serviços
→ Peças
→ Fornecedores
→ Pagamentos
→ Garantias
→ Documento PDF
```

A aplicação prioriza:

* Rastreabilidade;
* Integridade dos dados;
* Organização;
* Histórico;
* Clareza operacional;
* Funcionamento local;
* Interface profissional;
* Atendimento aos requisitos acadêmicos.

---

---

## 35. Etapa 55 — Correção da seleção da OS com campo de busca limpo

A Etapa 55 reforça a correção visual da tela **Ordens de Serviço**. O objetivo foi eliminar o caso em que o nome do cliente ou a identificação do veículo ainda permanecia no campo superior de busca depois da seleção na combobox.

A regra adotada é:

```text
Campo de busca = apenas localizar registros
Combobox = registrar a escolha da Ordem de Serviço
```

Alterações aplicadas:

* seleção de cliente tratada por método específico com `ngModelChange`;
* seleção de veículo tratada por método específico com `ngModelChange`;
* limpeza do campo de busca ao selecionar cliente;
* limpeza do campo de busca ao selecionar veículo;
* limpeza do campo de busca ao clicar ou focar na combobox;
* preservação da regra de vínculo entre cliente e veículo;
* nenhuma alteração no banco de dados ou no backend.

Arquivos alterados:

```text
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html
docs/README_ETAPA55_CORRECAO_SELECAO_OS_CAMPO_BUSCA_LIMPO.md
docs/adr/ADR-059-correcao-selecao-os-campo-busca-limpo.md
```


---

## 36. Etapa 56 — Correção da Fila de Atendimento e do fluxo de execução da OS

A Etapa 56 corrige o fluxo operacional da Ordem de Serviço e a regra da tela **Fila de Atendimento**.

Fluxo adotado:

```text
ORÇAMENTO → EXECUÇÃO → PAGAMENTO → FINALIZADO
```

Antes desta etapa, ao concluir o orçamento na tela **Itens da OS**, a ordem era enviada diretamente para **PAGAMENTO**, pulando a etapa de **EXECUÇÃO**. Isso fazia a Fila de Atendimento não representar corretamente os serviços que deveriam ser executados.

Alterações aplicadas:

* o botão da tela **Itens da OS** passou a enviar o orçamento para **EXECUÇÃO**;
* a OS não vai mais diretamente de orçamento para pagamento;
* a **Fila de Atendimento** agora lista somente OS em **EXECUÇÃO**;
* a fila recebeu ação para enviar a OS para **PAGAMENTO** após a execução do serviço;
* o resultado do cálculo recursivo deixou de ser exibido como JSON bruto;
* o cálculo recursivo agora aparece em cards com serviços, peças, total geral e justificativa acadêmica;
* o endpoint antigo de envio para pagamento foi mantido por compatibilidade, mas executa a regra correta de envio para execução.

Arquivos alterados:

```text
src/main/java/br/com/avcar/oficina/business/ordemservico/controller/OrdemServicoController.java
src/main/java/br/com/avcar/oficina/business/ordemservico/service/OrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/ordemservico/estrutura/service/EstruturaDadosOrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/pagamento/service/PagamentoService.java
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
frontend/oficina-web/src/app/models/ordem-servico.model.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.html
frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.ts
frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.html
frontend/oficina-web/src/styles.css
docs/README_ETAPA56_CORRECAO_FILA_FLUXO_EXECUCAO.md
docs/adr/ADR-060-correcao-fila-fluxo-execucao.md
```


---

## 37. Etapa 57 — Reformulação do Singleton e do Decorator

A Etapa 57 fortalece dois padrões de projeto que estavam corretos tecnicamente, mas poderiam agregar mais valor operacional ao sistema: **Singleton** e **Decorator**.

### Singleton

O `DatabaseConnectionSingleton` foi reformulado para atuar como monitor único do ambiente local de banco de dados. Agora, além de verificar se o PostgreSQL responde, ele registra:

* último resultado da verificação;
* tempo de resposta;
* quantidade de verificações realizadas;
* falhas consecutivas;
* data e hora da última verificação;
* data e hora da última mudança de status;
* último erro sanitizado;
* uso de cache operacional para evitar conexões repetidas em curto intervalo.

Essa alteração torna o padrão útil para execução local em computadores diferentes, facilitando o diagnóstico de problemas de ambiente.

### Decorator

O `NotificadorAuditoriaDecorator` foi reformulado para persistir auditoria em banco. Antes, a auditoria existia apenas no resultado da notificação. Agora, notificações operacionais, principalmente mudanças de status da Ordem de Serviço, geram registros na tabela:

```text
notificacao_auditoria
```

Endpoints adicionados ou reforçados:

```text
GET  /api/notificacoes/auditoria
GET  /api/notificacoes/auditoria/referencia?referencia={numeroOs}
POST /api/notificacoes/simular
```

Também foi liberada a tela **Padrões de Projeto** no menu de Gestão, com evidência funcional de cada padrão.

Arquivos principais alterados:

```text
src/main/java/br/com/avcar/oficina/core/designpattern/singleton/DatabaseConnectionSingleton.java
src/main/java/br/com/avcar/oficina/core/database/DatabaseConnectionChecker.java
src/main/java/br/com/avcar/oficina/core/database/DatabaseStatusResult.java
src/main/java/br/com/avcar/oficina/core/designpattern/decorator/NotificadorAuditoriaDecorator.java
src/main/java/br/com/avcar/oficina/core/notification/model/NotificacaoAuditoriaModel.java
src/main/java/br/com/avcar/oficina/core/notification/repository/INotificacaoAuditoriaRepository.java
src/main/java/br/com/avcar/oficina/core/notification/service/NotificacaoService.java
src/main/java/br/com/avcar/oficina/core/notification/controller/NotificacaoController.java
src/main/java/br/com/avcar/oficina/core/designpattern/catalog/controller/PadraoProjetoController.java
frontend/oficina-web/src/app/pages/configuracoes/configuracoes.component.html
frontend/oficina-web/src/app/pages/padroes-projeto/padroes-projeto.component.html
database/01_schema/03_create_notificacao_auditoria.sql
docs/README_ETAPA57_REFORMULACAO_SINGLETON_DECORATOR.md
docs/adr/ADR-061-reformulacao-singleton-decorator.md
```

Observação importante: como o projeto usa `spring.jpa.hibernate.ddl-auto=validate`, a tabela `notificacao_auditoria` deve ser criada no banco antes de iniciar o backend atualizado.

---

## 38. Etapa 58 — Atualizações em peças, fornecedores, veículos, orçamento e pagamentos

A Etapa 58 realizou ajustes operacionais importantes para melhorar a usabilidade e a rastreabilidade do sistema.

Principais alterações:

* Peça agora possui fornecedor padrão vinculado no cadastro.
* Peça agora possui valor unitário padrão.
* Ao selecionar uma peça na OS, o sistema preenche automaticamente o fornecedor e o valor unitário.
* Ao selecionar um serviço na OS, o sistema preenche automaticamente o valor base cadastrado.
* Ao alterar a quantidade de serviço ou peça, o total é recalculado automaticamente.
* Cadastro de veículo recebeu campo de busca de cliente acima do proprietário atual.
* Quilometragem do veículo não inicia mais com zero no formulário; fica em branco até ser informada.
* Tela de pagamentos teve o botão `Marcar pago` removido, deixando o fluxo financeiro mais limpo.

Script incremental necessário para bancos já existentes:

```text
database/01_schema/04_alter_peca_fornecedor_valor.sql
```

Arquivos principais alterados:

```text
src/main/java/br/com/avcar/oficina/business/peca/model/PecaModel.java
src/main/java/br/com/avcar/oficina/business/peca/dto/PecaDTO.java
src/main/java/br/com/avcar/oficina/business/peca/mapper/PecaMapper.java
src/main/java/br/com/avcar/oficina/business/peca/service/PecaService.java
src/main/java/br/com/avcar/oficina/business/peca/service/ItemPecaService.java
src/main/java/br/com/avcar/oficina/business/ordemservico/service/ItemServicoService.java
src/main/java/br/com/avcar/oficina/business/veiculo/model/VeiculoModel.java
src/main/java/br/com/avcar/oficina/business/veiculo/mapper/VeiculoMapper.java
frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.ts
frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.html
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.html
frontend/oficina-web/src/app/pages/veiculos/veiculos.component.ts
frontend/oficina-web/src/app/pages/veiculos/veiculos.component.html
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.html
database/01_schema/04_alter_peca_fornecedor_valor.sql
docs/README_ETAPA58_ATUALIZACOES_PECAS_VEICULOS_ORCAMENTO_PAGAMENTO.md
docs/adr/ADR-062-atualizacoes-pecas-veiculos-orcamento-pagamento.md
```

## 39. Autor

Projeto desenvolvido para fins acadêmicos no contexto do Projeto Integrador.

Sistema: AV CAR AUTO CENTER — Gestão de Oficina.

---

## Atualização — Etapa 59

A Etapa 59 removeu a tela visual de **Padrões de Projeto** do menu do sistema e simplificou as mensagens de conclusão das telas. Os padrões de projeto continuam implementados e documentados, mas não aparecem mais como página operacional. As mensagens agora usam textos objetivos, como `Salvo.`, `Inativado.`, `PDF gerado.`, `Enviado para execução.` e `Enviado para pagamento.`.

## Atualização final — Etapa 60: comentários no código

Nesta etapa, o código-fonte recebeu comentários explicativos nos métodos do backend e do frontend. Os comentários descrevem de forma simples a função de cada método no sistema.

Também foram reforçados os comentários nos pontos onde os padrões de projeto são aplicados, especialmente Singleton, Factory Method, Adapter, Iterator, Template Method e Decorator. Nos padrões, além da função do método, o código também informa a justificativa de uso do padrão e sua contribuição funcional para o sistema.

## Atualização — Etapa 61

Foi adicionada a recuperação de cadastros inativados em todas as telas que possuem inativação lógica. Cada módulo com botão **Inativar** agora possui também a opção **Ver inativos**, com filtros por texto e data, além do botão **Ativar** para recuperar o cadastro sem apagar histórico. A tela **Ordens de Serviço** também recebeu filtros melhores na consulta, incluindo status, período de abertura e botão rápido para visualizar OS finalizadas.

## Etapa 63 — Correção do componente de Pagamentos

Foi corrigido o erro de compilação `TS2339` no `PagamentosComponent`, adicionando o método `atualizarTela()` utilizado nas operações de carregamento e reativação de registros inativos. A alteração não modifica regras de negócio nem banco de dados; apenas corrige a consistência do componente Angular.

## Etapa 64 — Aba de Histórico de Proprietários

Foi adicionada uma aba específica em **Cadastros > Histórico de Proprietários** para consultar o histórico de posse dos veículos e registrar transferência de proprietário. A alteração utiliza a entidade `HistoricoProprietario`, já prevista no modelo de banco de dados, permitindo visualizar proprietário atual, proprietários anteriores, data de início, data de fim e observações de posse.

Também foi incluído o botão **Histórico** na tela de Veículos, direcionando o usuário diretamente para a consulta histórica do veículo selecionado.

## Etapa 67 — Histórico de proprietários por cliente

A tela **Histórico de Proprietários** foi corrigida para exibir a consulta principal por cliente, sem duplicar o mesmo cliente quando ele possui ou já possuiu mais de um veículo. Ao selecionar o cliente, o sistema apresenta os veículos vinculados, as datas de início e fim de posse, o status atual/anterior e as observações. Também foi criado o endpoint consolidado `GET /api/veiculos/historico-proprietarios`, retornando proprietários atuais e anteriores para preservar a rastreabilidade.

## Etapa 68 — Seed atualizada para a versão atual do sistema

A seed inicial do banco foi atualizada para refletir melhor a versão atual da tela **Gestão > Histórico de Proprietários**.

A alteração adiciona posses anteriores e posses atuais na tabela `historico_proprietario`, permitindo demonstrar que o histórico de proprietário não desaparece quando uma transferência é realizada. Também foram incluídos cenários em que um mesmo cliente possui mais de um veículo, evitando duplicidade na listagem por cliente.

Arquivos principais alterados:

```text
database/02_seed/02_seed_inicial.sql
database/03_verificacoes/03_verificacao_geral_sistema.sql
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
database/README_BANCO.md
docs/README_ETAPA68_SEED_ATUALIZADA_HISTORICO_PROPRIETARIOS.md
docs/adr/ADR-068-seed-atualizada-historico-proprietarios.md
```

Não houve alteração no modelo físico, backend, frontend, JDK, Maven ou fluxo da Ordem de Serviço.
