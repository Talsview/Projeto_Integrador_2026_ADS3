# AV CAR AUTO CENTER — Sistema de Gestão para Oficina Mecânica

Sistema acadêmico e operacional para controle de uma oficina mecânica local, desenvolvido para registrar e acompanhar clientes, veículos, histórico de proprietários, colaboradores, funções, ordens de serviço, serviços, peças, fornecedores, garantias, pagamentos, fila de atendimento, relatórios, emissão de documento em PDF e evidências de padrões de projeto.

O projeto foi construído com foco em rastreabilidade, integridade dos dados, histórico operacional, organização em camadas e funcionamento local, sem dependência obrigatória de internet para a operação principal.

---

## Sumário

- [1. Visão geral](#1-visão-geral)
- [2. Objetivo do sistema](#2-objetivo-do-sistema)
- [3. Contexto acadêmico e operacional](#3-contexto-acadêmico-e-operacional)
- [4. Principais funcionalidades](#4-principais-funcionalidades)
- [5. Fluxo operacional da Ordem de Serviço](#5-fluxo-operacional-da-ordem-de-serviço)
- [6. Regras de negócio consolidadas](#6-regras-de-negócio-consolidadas)
- [7. Arquitetura do sistema](#7-arquitetura-do-sistema)
- [8. Tecnologias utilizadas](#8-tecnologias-utilizadas)
- [9. Estrutura de pastas](#9-estrutura-de-pastas)
- [10. Modelo de dados e entidades principais](#10-modelo-de-dados-e-entidades-principais)
- [11. Banco de dados](#11-banco-de-dados)
- [12. Como executar o projeto](#12-como-executar-o-projeto)
- [13. Acesso às telas do frontend](#13-acesso-às-telas-do-frontend)
- [14. Endpoints principais da API](#14-endpoints-principais-da-api)
- [15. Padrões de projeto aplicados](#15-padrões-de-projeto-aplicados)
- [16. Estruturas de Dados I](#16-estruturas-de-dados-i)
- [17. Validações e tratamento de erros](#17-validações-e-tratamento-de-erros)
- [18. Relatórios e documentos](#18-relatórios-e-documentos)
- [19. Configurações e diagnóstico local](#19-configurações-e-diagnóstico-local)
- [20. Scripts SQL e documentação](#20-scripts-sql-e-documentação)
- [21. Testes e verificação](#21-testes-e-verificação)
- [22. Problemas comuns e soluções](#22-problemas-comuns-e-soluções)
- [23. Observações para GitHub](#23-observações-para-github)
- [24. Status do projeto](#24-status-do-projeto)
- [25. Créditos acadêmicos](#25-créditos-acadêmicos)

---

## 1. Visão geral

O **AV CAR AUTO CENTER** é um sistema para gestão de oficina mecânica, voltado para o controle de Ordens de Serviço e dos dados envolvidos em cada atendimento.

O sistema permite registrar o atendimento desde a identificação do cliente e do veículo até a finalização da Ordem de Serviço, mantendo vínculos entre:

- Cliente;
- Veículo;
- Proprietário atual e histórico de proprietários;
- Ordem de Serviço;
- Serviços executados;
- Colaborador responsável por cada serviço;
- Peças utilizadas;
- Fornecedor de cada peça aplicada;
- Garantias de peças e serviços;
- Pagamentos;
- Histórico de status da OS;
- Auditoria de notificações operacionais.

A aplicação foi pensada para uma oficina local, com operação em ambiente interno, utilizando banco de dados PostgreSQL local e frontend Angular consumindo uma API REST Spring Boot.

---

## 2. Objetivo do sistema

O objetivo principal do sistema é substituir controles manuais ou incompletos por um controle informatizado, rastreável e organizado das Ordens de Serviço da oficina.

O sistema busca resolver problemas como:

- Falta de rastreabilidade entre cliente, veículo, serviço e peças;
- Dificuldade de saber qual colaborador executou cada serviço;
- Falta de histórico de proprietários dos veículos;
- Falta de identificação do fornecedor da peça aplicada;
- Falta de controle correto do fluxo da OS;
- Falta de controle de garantias;
- Falta de histórico de status da Ordem de Serviço;
- Dificuldade para controlar pagamentos parciais e quitação;
- Dificuldade para comprovar padrões de projeto e estrutura de dados no trabalho acadêmico.

---

## 3. Contexto acadêmico e operacional

Este projeto foi desenvolvido para o Projeto Integrador do curso de Análise e Desenvolvimento de Sistemas, com o tema **Sistema para o controle de uma Oficina Mecânica**.

O sistema atende às exigências acadêmicas de:

- Aplicação em Java;
- Arquitetura monolítica em camadas;
- Uso de Model, DTO, Repository, Validation, Service, Controller, Response e View;
- Aplicação de Generics;
- Uso de banco de dados relacional;
- Implementação de padrões de projeto;
- Implementação de estrutura de dados linear, algoritmo de ordenação e função recursiva;
- Documentação técnica e decisões arquiteturais;
- Interface gráfica web em Angular;
- Funcionamento local em ambiente Windows ou Linux.

O sistema foi modelado com base nas necessidades de uma oficina mecânica local, cujo objetivo é controlar corretamente as Ordens de Serviço e manter histórico das informações operacionais.

---

## 4. Principais funcionalidades

### 4.1 Dashboard

Tela inicial com visão geral da oficina, permitindo acesso rápido aos módulos principais.

Funcionalidades previstas:

- Resumo operacional;
- Atalhos para cadastros e operações;
- Acesso às telas de OS, fila, pagamentos, garantias e relatórios;
- Identidade visual da oficina.

---

### 4.2 Clientes

Permite cadastrar, listar, pesquisar, editar e inativar clientes.

O cliente pode ser:

- Pessoa Física;
- Pessoa Jurídica.

Dados tratados:

- Nome;
- Telefone;
- E-mail;
- Endereço;
- CPF;
- RG;
- Data de nascimento;
- CNPJ;
- Razão social;
- Nome fantasia;
- Inscrição estadual;
- Status ativo/inativo.

Regras importantes:

- Todo cliente deve ser Pessoa Física ou Pessoa Jurídica;
- Um cliente pode solicitar várias Ordens de Serviço;
- A inativação é lógica, preservando histórico.

Padrão de projeto aplicado neste módulo:

- **Factory Method**, usado para criar corretamente cliente PF ou PJ.

---

### 4.3 Veículos

Permite cadastrar veículos atendidos pela oficina.

Dados tratados:

- Marca;
- Modelo;
- Placa;
- Chassi;
- Cor;
- Ano de fabricação;
- Ano do modelo;
- Quilometragem;
- Proprietário atual;
- Observações;
- Histórico de proprietários.

Regras importantes:

- Um veículo pode ter vários proprietários ao longo do tempo;
- O sistema mantém o histórico de proprietários;
- A Ordem de Serviço sempre pertence a um veículo;
- A identificação de marca, modelo, ano do veículo e ano do modelo apoia a rastreabilidade das peças aplicadas.

Padrão de projeto aplicado neste módulo:

- **Adapter**, usado para transformar dados complexos de veículo, marca, modelo e histórico em DTOs adequados para o frontend.

---

### 4.4 Histórico de proprietários

Registra a relação entre cliente e veículo ao longo do tempo.

Importância:

- Preserva o histórico do veículo mesmo que ele troque de dono;
- Permite identificar o proprietário atual;
- Mantém rastreabilidade entre cliente, veículo e Ordens de Serviço.

Regras importantes:

- Todo veículo deve possuir pelo menos um histórico de proprietário;
- Um cliente pode aparecer em vários históricos de posse;
- O proprietário atual é identificado pelo histórico ativo ou sem data final.

---

### 4.5 Colaboradores

Permite cadastrar pessoas que trabalham na oficina.

Dados tratados:

- Nome;
- Telefone;
- E-mail;
- Endereço;
- Data de admissão;
- Funções vinculadas;
- Status ativo/inativo.

Regras importantes:

- Todo colaborador pode ter uma ou mais funções;
- Todo serviço executado em uma OS deve ter um colaborador responsável;
- Mecânico, atendente, secretária, faxineiro, estoquista e gerente são registros de Função, e não entidades separadas.

---

### 4.6 Funções

Permite cadastrar funções exercidas pelos colaboradores.

Exemplos:

- Mecânico;
- Atendente;
- Secretária;
- Faxineiro;
- Estoquista;
- Gerente.

Regras importantes:

- Uma função pode ser atribuída a vários colaboradores;
- Um colaborador pode possuir várias funções;
- A entidade associativa `ColaboradorFuncao` registra a relação entre colaborador e função.

---

### 4.7 Marcas e modelos

Permite organizar os veículos por marca e modelo.

Regras importantes:

- Uma marca pode possuir vários modelos;
- Um modelo pertence a uma única marca;
- O veículo deve estar vinculado a um modelo e, indiretamente, a uma marca.

---

### 4.8 Serviços

Permite cadastrar serviços prestados pela oficina.

Tipos de serviço:

- Serviço Interno;
- Serviço Terceirizado.

Dados tratados:

- Nome;
- Descrição;
- Tipo do serviço;
- Valor base;
- Prazo de garantia em dias;
- Status.

Regras importantes:

- Todo serviço deve ser interno ou terceirizado;
- Todo item de serviço em uma OS deve ter colaborador responsável;
- Todo serviço executado gera garantia;
- O prazo da garantia varia conforme o tipo de serviço.

---

### 4.9 Empresas terceirizadas

Permite cadastrar empresas externas que executam serviços para a oficina.

Regras importantes:

- Serviços terceirizados podem ser executados por empresas externas;
- Mesmo terceirizando, a oficina continua responsável perante o cliente;
- A execução terceirizada é registrada por meio da entidade `ExecucaoServicoTerceirizado`.

---

### 4.10 Peças e fornecedores

Permite cadastrar peças e fornecedores.

Dados de peças:

- Código;
- Nome;
- Descrição;
- Marca;
- Aplicação;
- Valor;
- Prazo de garantia.

Dados de fornecedores:

- Nome;
- CNPJ;
- Telefone;
- E-mail;
- Endereço;
- Observações.

Regras importantes:

- Toda peça usada em OS deve estar cadastrada;
- Toda peça usada em OS deve ter fornecedor identificado;
- Toda peça aplicada gera garantia;
- A garantia da peça começa após a finalização da OS.

---

### 4.11 Ordens de Serviço

Módulo principal do sistema.

Permite:

- Criar nova OS;
- Selecionar cliente;
- Selecionar veículo vinculado ao cliente;
- Definir prioridade;
- Consultar OS;
- Editar dados principais;
- Inativar OS sem apagar fisicamente;
- Controlar status;
- Manter histórico de status.

Regras importantes:

- A OS nasce no status `ORCAMENTO`;
- O número da OS é automático e não deve ser reutilizado;
- OS inativada mantém o número reservado;
- Toda OS pertence a um único cliente;
- Toda OS pertence a um único veículo;
- Toda OS deve ter histórico de status.

---

### 4.12 Serviços e peças da OS

Tela responsável pelo orçamento técnico da OS.

Permite:

- Adicionar serviços à OS;
- Vincular colaborador responsável ao serviço;
- Adicionar peças utilizadas;
- Identificar fornecedor da peça;
- Calcular valores;
- Enviar orçamento para execução.

Regra atual corrigida:

```text
ORCAMENTO → EXECUCAO
```

Depois que o orçamento é montado, a OS vai para execução e passa a aparecer na Fila de Atendimento.

---

### 4.13 Fila de atendimento

Representa a fila operacional das OS em execução.

A tela exibe somente Ordens de Serviço com status:

```text
EXECUCAO
```

Essa fila representa clientes/veículos que já tiveram o orçamento montado e aguardam ou estão em execução do serviço.

Funcionalidades:

- Listagem da fila de atendimento;
- Ordenação manual por data;
- Ordenação manual por valor;
- Ordenação manual por prioridade;
- Pesquisa linear;
- Cálculo total recursivo da OS;
- Envio da OS para pagamento ao finalizar a execução.

Regra operacional:

```text
ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO
```

---

### 4.14 Pagamentos

Controla os pagamentos vinculados à OS.

Funcionalidades:

- Registrar pagamento;
- Editar pagamento;
- Consultar pagamentos por OS;
- Visualizar resumo financeiro;
- Controlar pagamentos parciais;
- Identificar quitação;
- Finalizar OS após quitação.

Regras importantes:

- Apenas OS em `PAGAMENTO` devem entrar no módulo de pagamentos;
- A OS só deve ser finalizada após quitação financeira;
- A OS pode ter nenhum, um ou vários pagamentos;
- Ao quitar o valor total, a OS segue para `FINALIZADO`.

---

### 4.15 Garantias

Controla garantias de peças e serviços.

Funcionalidades:

- Consultar garantias de peças;
- Consultar garantias de serviços;
- Acionar garantia;
- Registrar motivo do acionamento;
- Registrar defeito apresentado;
- Registrar responsável pela análise;
- Encerrar atendimento de garantia;
- Registrar solução aplicada;
- Registrar quem assumiu o custo.

Regras importantes:

- Todo item de peça gera garantia de peça;
- Todo item de serviço gera garantia de serviço;
- A garantia da peça começa após finalização da OS;
- A garantia de serviço varia conforme o tipo de serviço;
- A oficina atende o cliente mesmo quando a responsabilidade pode ser do fornecedor.

---

### 4.16 Relatórios

Módulo de consultas gerenciais e exportação.

Funcionalidades previstas/implementadas:

- Relatórios operacionais;
- Relatórios de OS;
- Relatórios de pagamentos;
- Relatórios de garantias;
- Exportação de dados para planilha;
- Fallback de dados para evitar tela vazia quando algum endpoint não retorna registros.

---

### 4.17 Configurações

Tela para diagnóstico do ambiente local.

Mostra informações relacionadas à conexão com o banco de dados, usando o Singleton reformulado como monitor operacional.

Informações exibidas:

- Status da conexão;
- Tempo de resposta do PostgreSQL;
- Quantidade de verificações realizadas;
- Falhas consecutivas;
- Última verificação;
- Última mudança de status;
- Último erro sanitizado;
- Indicação de cache.

---

### 4.18 Padrões de Projeto

Tela criada para evidenciar academicamente os padrões aplicados no sistema.

Permite visualizar:

- Nome do padrão;
- Classe principal;
- Local de aplicação;
- Justificativa;
- Evidência funcional.

Endpoint de apoio:

```text
GET /api/padroes-projeto
```

---

## 5. Fluxo operacional da Ordem de Serviço

O fluxo oficial da OS no sistema é:

```text
ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO
```

### 5.1 Criação da OS

Quando uma OS é cadastrada, ela nasce automaticamente como:

```text
ORCAMENTO
```

Neste momento, o cliente e o veículo são identificados, e o atendimento é registrado.

### 5.2 Montagem do orçamento

Na tela **Serviços e Peças da OS**, são adicionados:

- Serviços necessários;
- Colaborador responsável por cada serviço;
- Peças a serem utilizadas;
- Fornecedor de cada peça;
- Valores.

Ao concluir o orçamento, a OS vai para:

```text
EXECUCAO
```

### 5.3 Fila de atendimento

A tela **Fila de Atendimento** lista as OS em:

```text
EXECUCAO
```

Essa tela representa a operação da oficina: serviços que já foram orçados e agora precisam ser executados.

Após a execução, a OS segue para:

```text
PAGAMENTO
```

### 5.4 Pagamento

Na tela **Pagamentos**, são registrados os pagamentos da OS.

A OS pode receber:

- Um pagamento único;
- Vários pagamentos parciais.

Quando o valor pago atinge o total da OS, o sistema pode finalizar a Ordem de Serviço.

### 5.5 Finalização

Após quitação, a OS segue para:

```text
FINALIZADO
```

A partir da finalização, inicia-se a contagem das garantias de peças e serviços.

---

## 6. Regras de negócio consolidadas

Principais regras aplicadas no sistema:

| Código | Regra |
|---|---|
| RN01 | Toda pessoa cadastrada deve ser Cliente, Colaborador ou ambos. |
| RN02 | Todo cliente deve ser Pessoa Física ou Pessoa Jurídica. |
| RN03 | Um veículo pode ter vários proprietários ao longo do tempo. |
| RN04 | O histórico de proprietário registra Cliente e Veículo. |
| RN05 | Todo veículo deve possuir pelo menos um histórico de proprietário. |
| RN06 | Cliente pode solicitar várias Ordens de Serviço. |
| RN07 | Toda OS pertence a um único Cliente. |
| RN08 | Veículo pode receber várias Ordens de Serviço. |
| RN09 | Toda OS pertence a um único Veículo. |
| RN10 | Toda OS deve possuir histórico de status. |
| RN11 | A OS segue o fluxo ORCAMENTO, EXECUCAO, PAGAMENTO e FINALIZADO. |
| RN12 | Toda OS deve possuir pelo menos um item de serviço para avançar no fluxo. |
| RN13 | Todo item de serviço possui colaborador responsável. |
| RN14 | Colaborador pode possuir uma ou mais funções. |
| RN15 | Funções como mecânico e atendente são registros de Função. |
| RN16 | Todo serviço é Interno ou Terceirizado. |
| RN17 | Serviço terceirizado gera execução terceirizada. |
| RN18 | Empresa terceirizada pode executar várias terceirizações. |
| RN19 | A oficina permanece responsável pelo serviço terceirizado perante o cliente. |
| RN20 | OS pode ou não utilizar peças. |
| RN21 | Peça usada na OS deve ser registrada como ItemPeca. |
| RN22 | ItemPeca deve estar vinculado a uma peça cadastrada. |
| RN23 | ItemPeca deve ter fornecedor identificado. |
| RN24 | ItemPeca gera GarantiaPeca. |
| RN25 | ItemServico gera GarantiaServico. |
| RN26 | Garantia da peça começa após finalizar a OS. |
| RN27 | Garantia do serviço varia conforme o tipo de serviço. |
| RN28 | OS pode gerar nenhum, um ou vários pagamentos. |
| RN29 | O sistema deve priorizar rastreabilidade entre cliente, veículo, OS, serviço, peça, fornecedor e garantia. |
| RN30 | O sistema deve funcionar localmente, sem dependência obrigatória de internet. |

---

## 7. Arquitetura do sistema

O sistema segue uma arquitetura monolítica em camadas.

### 7.1 Visão geral

```text
Frontend Angular
        ↓
API REST Spring Boot
        ↓
Camada Controller
        ↓
Camada Service
        ↓
Camada Repository
        ↓
Banco PostgreSQL
```

### 7.2 Camadas do backend

O backend foi organizado com as seguintes camadas:

| Camada | Responsabilidade |
|---|---|
| Model | Representa as entidades persistidas no banco. |
| DTO | Transporta dados entre API e frontend. |
| Mapper | Converte Model para DTO e DTO para Model. |
| Repository | Acessa o banco de dados via Spring Data JPA. |
| Validation | Centraliza validações de campos e regras. |
| Service | Implementa regras de negócio. |
| Controller | Expõe endpoints REST. |
| Response | Padroniza respostas da API. |
| View | Frontend Angular. |

### 7.3 Uso de Generics

O projeto possui estrutura genérica para reduzir repetição de CRUD:

- `BaseModel`;
- `BaseDTO`;
- `IGenericRepository`;
- `IGenericService`;
- `GenericService`;
- `IGenericMapper`;
- `GenericController`;
- `ApiResponse<T>`;
- `PageResponse<T>`.

Exemplo de resposta padronizada:

```json
{
  "success": true,
  "status": 200,
  "message": "Registro localizado com sucesso.",
  "data": {},
  "timestamp": "2026-06-10T20:00:00"
}
```

### 7.4 Inativação lógica

A exclusão no sistema é lógica.

Quando um registro é inativado, ele não é removido fisicamente do banco. O campo `ativo` é alterado para `false`.

Isso preserva:

- Histórico;
- Rastreabilidade;
- Integridade;
- Numeração de OS;
- Evidências de atendimento.

---

## 8. Tecnologias utilizadas

### Backend

- Java 21;
- Spring Boot 3.5.13;
- Spring Web;
- Spring Data JPA;
- Hibernate;
- PostgreSQL Driver;
- Lombok;
- OpenPDF;
- Springdoc OpenAPI/Swagger;
- Maven.

### Frontend

- Angular 22;
- TypeScript;
- RxJS;
- HTML;
- CSS;
- Angular Router;
- Angular Forms;
- Proxy de desenvolvimento para integração com backend.

### Banco de dados

- PostgreSQL;
- Scripts SQL versionados;
- Criação manual de schema;
- Seeds iniciais;
- Scripts de verificação.

### Ferramentas recomendadas

- NetBeans ou IntelliJ IDEA para backend;
- Visual Studio Code para frontend;
- pgAdmin para banco;
- Git e GitHub para versionamento;
- Postman, Insomnia ou Swagger para testar API.

---

## 9. Estrutura de pastas

Estrutura principal do projeto:

```text
car-repair
├── database
│   ├── 01_schema
│   ├── 02_seed
│   ├── 03_verificacoes
│   └── 04_completo
├── docs
│   ├── adr
│   └── relatorios
├── frontend
│   └── oficina-web
├── src
│   ├── main
│   │   ├── java
│   │   │   └── br/com/avcar/oficina
│   │   └── resources
│   └── test
├── pom.xml
└── README.md
```

### 9.1 Backend

```text
src/main/java/br/com/avcar/oficina
├── business
│   ├── garantia
│   ├── notafiscal
│   ├── ordemservico
│   ├── pagamento
│   ├── peca
│   ├── pessoa
│   ├── servico
│   └── veiculo
├── core
│   ├── config
│   ├── controller
│   ├── database
│   ├── designpattern
│   ├── dto
│   ├── estrutura
│   ├── exception
│   ├── mapper
│   ├── model
│   ├── notification
│   ├── repository
│   ├── response
│   ├── service
│   └── validation
└── view
    └── api
```

### 9.2 Frontend

```text
frontend/oficina-web/src/app
├── core
│   ├── interceptors
│   ├── models
│   ├── services
│   └── validation
├── models
├── pages
│   ├── clientes
│   ├── colaboradores
│   ├── configuracoes
│   ├── dashboard
│   ├── empresas-terceirizadas
│   ├── estrutura-dados
│   ├── funcoes
│   ├── garantias
│   ├── itens-os
│   ├── marcas-modelos
│   ├── ordens-servico
│   ├── padroes-projeto
│   ├── pagamentos
│   ├── pecas-fornecedores
│   ├── relatorios
│   ├── servicos
│   └── veiculos
├── app.component.*
├── app.config.ts
└── app.routes.ts
```

---

## 10. Modelo de dados e entidades principais

Entidades principais do sistema:

| Entidade | Finalidade |
|---|---|
| Pessoa | Dados comuns de pessoas. |
| Cliente | Pessoa atendida pela oficina. |
| PessoaFisica | Especialização de Cliente com CPF. |
| PessoaJuridica | Especialização de Cliente com CNPJ. |
| Colaborador | Pessoa que trabalha na oficina. |
| Funcao | Função exercida pelo colaborador. |
| ColaboradorFuncao | Entidade associativa entre colaborador e função. |
| Veiculo | Veículo atendido pela oficina. |
| Marca | Marca do veículo. |
| Modelo | Modelo do veículo. |
| HistoricoProprietario | Histórico de posse do veículo. |
| OrdemServico | Registro principal do atendimento. |
| StatusOrdemServico | Status possíveis da OS. |
| HistoricoStatusOrdem | Histórico de evolução da OS. |
| Servico | Cadastro geral de serviço. |
| ServicoInterno | Especialização de serviço interno. |
| ServicoTerceirizado | Especialização de serviço terceirizado. |
| ItemServico | Serviço executado em uma OS. |
| EmpresaTerceirizada | Empresa externa que executa serviço. |
| ExecucaoServicoTerceirizado | Registro da execução terceirizada. |
| Peca | Cadastro de peça. |
| Fornecedor | Fornecedor de peça. |
| ItemPeca | Peça aplicada em uma OS. |
| GarantiaPeca | Garantia da peça aplicada. |
| GarantiaServico | Garantia do serviço executado. |
| Pagamento | Pagamento vinculado à OS. |
| NotificacaoAuditoria | Auditoria persistente de notificações operacionais. |

### 10.1 Generalizações e especializações

```text
Pessoa → Cliente / Colaborador
Tipo: compartilhada e total
```

Uma pessoa cadastrada pode ser cliente, colaborador ou ambos.

```text
Cliente → PessoaFisica / PessoaJuridica
Tipo: exclusiva e total
```

Todo cliente é pessoa física ou pessoa jurídica, nunca ambos.

```text
Servico → ServicoInterno / ServicoTerceirizado
Tipo: exclusiva e total
```

Todo serviço é interno ou terceirizado, nunca ambos.

---

## 11. Banco de dados

O projeto utiliza PostgreSQL local.

### 11.1 Configuração padrão

Arquivo:

```text
src/main/resources/application.properties
```

Configuração padrão do projeto:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/car_repair
spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=validate
```

A senha deve ser ajustada conforme o PostgreSQL instalado na máquina.

### 11.2 Criação do banco

No pgAdmin ou psql, crie o banco:

```sql
CREATE DATABASE car_repair;
```

### 11.3 Execução dos scripts

Forma recomendada para montar do zero:

```text
1. database/01_schema/01_create_schema.sql
2. database/02_seed/02_seed_inicial.sql
```

Se estiver atualizando uma base antiga para a Etapa 57, execute também:

```text
database/01_schema/03_create_notificacao_auditoria.sql
```

Alternativa para demonstração rápida:

```text
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
```

### 11.4 Por que o Hibernate está como validate?

O projeto usa:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Isso significa que o Hibernate não cria nem altera tabelas automaticamente. Ele apenas valida se o banco físico está compatível com os Models.

Essa decisão foi adotada porque o banco faz parte da entrega acadêmica e deve ser criado por scripts SQL versionados.

---

## 12. Como executar o projeto

### 12.1 Pré-requisitos

Instale:

- JDK 21;
- Maven;
- PostgreSQL;
- Node.js compatível com Angular 22;
- Angular CLI;
- Git.

### 12.2 Configurar Java e Maven

Confirme no terminal:

```bash
java -version
javac -version
mvn -version
```

O Maven deve estar usando Java 21.

Exemplo esperado:

```text
Java version: 21
Java home: C:\Program Files\Java\jdk-21
```

Se estiver usando NetBeans, confira também a configuração do Maven dentro da IDE. Caso o Maven esteja apontando para outro JDK, como JDK 26, o Lombok pode falhar e aparecerem erros do tipo `cannot find symbol` para getters e setters.

### 12.3 Executar backend

Na raiz do projeto:

```bash
cd car-repair
mvn clean install -DskipTests
mvn spring-boot:run
```

O backend sobe por padrão em:

```text
http://localhost:9081
```

### 12.4 Acessar Swagger

Com o backend rodando:

```text
http://localhost:9081/swagger-ui.html
```

Documentação OpenAPI:

```text
http://localhost:9081/api-docs
```

### 12.5 Executar frontend

Em outro terminal:

```bash
cd car-repair/frontend/oficina-web
npm install
npm start
```

O Angular sobe normalmente em:

```text
http://localhost:4200
```

O projeto usa proxy para redirecionar `/api` para o backend.

Arquivo:

```text
frontend/oficina-web/proxy.conf.json
```

### 12.6 Ordem correta de execução

```text
1. Criar banco PostgreSQL.
2. Executar schema.
3. Executar seed.
4. Conferir application.properties.
5. Rodar backend Spring Boot.
6. Rodar frontend Angular.
7. Acessar http://localhost:4200.
```

---

## 13. Acesso às telas do frontend

Rotas principais:

| Tela | Rota |
|---|---|
| Dashboard | `/` |
| Clientes | `/clientes` |
| Funções | `/funcoes` |
| Colaboradores | `/colaboradores` |
| Marcas e Modelos | `/marcas-modelos` |
| Veículos | `/veiculos` |
| Serviços | `/servicos` |
| Empresas Terceirizadas | `/empresas-terceirizadas` |
| Peças e Fornecedores | `/pecas-fornecedores` |
| Ordens de Serviço | `/ordens-servico` |
| Serviços e Peças da OS | `/itens-os` |
| Fila de Atendimento | `/fila-atendimento` |
| Pagamentos | `/pagamentos` |
| Garantias | `/garantias` |
| Relatórios | `/relatorios` |
| Configurações | `/configuracoes` |
| Padrões de Projeto | `/padroes-projeto` |

---

## 14. Endpoints principais da API

### 14.1 Clientes

```text
POST   /api/clientes/pessoa-fisica
POST   /api/clientes/pessoa-juridica
PUT    /api/clientes/pessoa-fisica/{id}
PUT    /api/clientes/pessoa-juridica/{id}
GET    /api/clientes/{id}
GET    /api/clientes
GET    /api/clientes/pesquisar
DELETE /api/clientes/{id}
```

### 14.2 Colaboradores e funções

```text
/api/colaboradores
/api/funcoes
```

### 14.3 Veículos

```text
POST   /api/veiculos
PUT    /api/veiculos/{id}
PATCH  /api/veiculos/{id}/transferir-proprietario
GET    /api/veiculos/{id}
GET    /api/veiculos
GET    /api/veiculos/pesquisar
DELETE /api/veiculos/{id}
```

### 14.4 Marcas e modelos

```text
/api/marcas
/api/modelos
GET /api/modelos/marca/{marcaId}
```

### 14.5 Serviços e empresas terceirizadas

```text
/api/servicos
GET /api/servicos/tipo/{tipoServico}
/api/empresas-terceirizadas
```

### 14.6 Peças e fornecedores

```text
/api/pecas
/api/fornecedores
/api/itens-peca
GET /api/itens-peca/ordem-servico/{idOrdemServico}
```

### 14.7 Ordem de Serviço

```text
POST   /api/ordens-servico
PUT    /api/ordens-servico/{id}
PATCH  /api/ordens-servico/{id}/status
PATCH  /api/ordens-servico/{id}/enviar-para-execucao
PATCH  /api/ordens-servico/{id}/enviar-para-pagamento
GET    /api/ordens-servico/{id}
GET    /api/ordens-servico
GET    /api/ordens-servico/pesquisar
DELETE /api/ordens-servico/{id}
```

### 14.8 Itens de serviço

```text
/api/itens-servico
GET /api/itens-servico/ordem-servico/{idOrdemServico}
GET /api/itens-servico/ordem-servico/{idOrdemServico}/pesquisar
```

### 14.9 Fila, ordenação, busca e recursividade

```text
GET /api/estrutura-dados/ordens-servico/fila-atendimento
GET /api/estrutura-dados/ordens-servico/ordenar
GET /api/estrutura-dados/ordens-servico/pesquisar-linear
GET /api/estrutura-dados/ordens-servico/{idOrdemServico}/total-recursivo
```

### 14.10 Pagamentos

```text
POST   /api/pagamentos
PUT    /api/pagamentos/{id}
PATCH  /api/pagamentos/{id}/status
GET    /api/pagamentos/{id}
GET    /api/pagamentos
GET    /api/pagamentos/ordem-servico/{idOrdemServico}
GET    /api/pagamentos/ordem-servico/{idOrdemServico}/resumo
DELETE /api/pagamentos/{id}
```

### 14.11 Garantias

```text
GET   /api/garantias/pecas
GET   /api/garantias/pecas/{id}
GET   /api/garantias/pecas/item-peca/{idItemPeca}
GET   /api/garantias/pecas/ordem-servico/{idOrdemServico}
PATCH /api/garantias/pecas/{id}/acionar
PATCH /api/garantias/pecas/{id}/encerrar

GET   /api/garantias/servicos
GET   /api/garantias/servicos/{id}
GET   /api/garantias/servicos/item-servico/{idItemServico}
GET   /api/garantias/servicos/ordem-servico/{idOrdemServico}
PATCH /api/garantias/servicos/{id}/acionar
PATCH /api/garantias/servicos/{id}/encerrar
```

### 14.12 Nota/recibo em PDF

```text
GET /api/notas-fiscais/ordens-servico/{id}/pdf
```

### 14.13 Configuração e diagnóstico

```text
GET /api/database/status
```

### 14.14 Notificações e auditoria

```text
POST /api/notificacoes/simular
GET  /api/notificacoes/auditoria
GET  /api/notificacoes/auditoria/referencia?referencia={numeroOs}
```

### 14.15 Padrões de projeto

```text
GET /api/padroes-projeto
```

---

## 15. Padrões de projeto aplicados

O sistema implementa seis padrões de projeto com aplicação funcional.

### 15.1 Singleton

Classe principal:

```text
DatabaseConnectionSingleton
```

Local:

```text
src/main/java/br/com/avcar/oficina/core/designpattern/singleton
```

Aplicação:

- Monitor único do ambiente local de banco de dados;
- Registra último estado da conexão;
- Mede tempo de resposta;
- Conta verificações realizadas;
- Conta falhas consecutivas;
- Registra última mudança de status;
- Usa cache operacional para evitar conexões repetidas.

Valor funcional:

- Ajuda a diagnosticar problemas ao executar o sistema em computadores diferentes;
- Apoia a exigência de funcionamento local;
- Evita espalhar lógica de diagnóstico de banco pela aplicação.

---

### 15.2 Factory Method

Classes principais:

```text
ClienteFactoryMethod
ClientePessoaFisicaFactory
ClientePessoaJuridicaFactory
ClienteCadastroFactory
```

Local:

```text
src/main/java/br/com/avcar/oficina/business/pessoa/designpattern/factory
```

Aplicação:

- Cadastro de Cliente Pessoa Física;
- Cadastro de Cliente Pessoa Jurídica;
- Criação organizada das entidades Pessoa, Cliente, PessoaFisica e PessoaJuridica.

Valor funcional:

- Evita duplicação de lógica de criação;
- Garante que PF e PJ sejam criadas respeitando regras diferentes;
- Representa corretamente a especialização exclusiva e total de Cliente.

---

### 15.3 Adapter

Classe principal:

```text
VeiculoResponseAdapter
```

Local:

```text
src/main/java/br/com/avcar/oficina/business/veiculo/adapter
```

Aplicação:

- Adaptação de dados complexos de veículo para resposta consumível pelo Angular.

Valor funcional:

- Converte dados de veículo, marca, modelo, cliente e histórico de proprietário em DTOs claros;
- Reduz complexidade no frontend;
- Melhora a apresentação do proprietário atual e do histórico.

---

### 15.4 Iterator

Classes principais:

```text
OficinaIterator
FilaAtendimentoIterator
ListaLinearIterator
```

Local:

```text
src/main/java/br/com/avcar/oficina/core/estrutura
```

Aplicação:

- Percurso da fila de atendimento;
- Percurso de lista linear de busca.

Valor funcional:

- Permite percorrer estruturas lineares próprias sem expor nós internos;
- Atende à exigência acadêmica de estrutura de dados;
- Apoia busca e listagem na tela Fila de Atendimento.

---

### 15.5 Template Method

Classe principal:

```text
OrdenadorTemplate
```

Implementações:

```text
OrdenadorOrdemServicoPorDataAbertura
OrdenadorOrdemServicoPorValorTotal
OrdenadorOrdemServicoPorPrioridade
```

Aplicação:

- Ordenação manual das Ordens de Serviço.

Valor funcional:

- Define o esqueleto do algoritmo de ordenação;
- Permite trocar apenas o critério de comparação;
- Evita duplicação do algoritmo para data, valor e prioridade.

---

### 15.6 Decorator

Classes principais:

```text
Notificador
NotificadorOperacional
NotificadorDecorator
NotificadorAuditoriaDecorator
```

Local:

```text
src/main/java/br/com/avcar/oficina/core/designpattern/decorator
```

Aplicação:

- Notificação operacional com auditoria persistente.

Valor funcional:

- Adiciona auditoria à notificação sem alterar a classe base;
- Registra auditoria em banco na tabela `notificacao_auditoria`;
- Gera rastreabilidade para mudanças de status da OS.

---

## 16. Estruturas de Dados I

O sistema implementa recursos exigidos pela disciplina de Estrutura de Dados I.

### 16.1 Estrutura linear

Estrutura:

```text
FilaAtendimento
```

Aplicação:

- Representa a fila de OS em execução;
- Exibe OS no status `EXECUCAO`;
- Permite organizar o atendimento operacional da oficina.

### 16.2 Busca linear

Estrutura:

```text
ListaLinearBusca
```

Aplicação:

- Pesquisa por número da OS, placa ou cliente;
- Demonstra busca linear em coleção própria.

### 16.3 Ordenação manual

Algoritmo:

```text
Ordenação por inserção
```

Critérios:

- Data de abertura;
- Valor total;
- Prioridade.

### 16.4 Função recursiva

Aplicação:

- Cálculo total da OS;
- Soma recursiva dos itens de serviço e itens de peça.

A tela exibe o resultado de forma organizada, com:

- Total de serviços;
- Total de peças;
- Total geral;
- Quantidade de itens de serviço;
- Quantidade de itens de peça;
- Função utilizada;
- Justificativa acadêmica.

---

## 17. Validações e tratamento de erros

O sistema possui camada de validação para impedir dados inconsistentes.

Exemplos de validação:

- CPF válido;
- CNPJ válido;
- Telefone numérico;
- Campos obrigatórios;
- Relações obrigatórias;
- Cliente vinculado à OS;
- Veículo vinculado à OS;
- Colaborador responsável por ItemServico;
- Fornecedor obrigatório para ItemPeca;
- Pagamento compatível com OS;
- Garantia com dados de acionamento e encerramento.

### 17.1 Resposta padronizada

As respostas da API seguem o padrão:

```json
{
  "success": true,
  "status": 200,
  "message": "Mensagem da operação.",
  "data": {},
  "timestamp": "2026-06-10T20:00:00"
}
```

### 17.2 Tratamento no frontend

O frontend possui interceptor para erros de API:

```text
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
```

Ele centraliza mensagens e evita que falhas técnicas sejam exibidas de forma bruta ao usuário.

---

## 18. Relatórios e documentos

### 18.1 PDF da Ordem de Serviço

O sistema gera documento PDF para Ordem de Serviço por meio de OpenPDF.

Endpoint:

```text
GET /api/notas-fiscais/ordens-servico/{id}/pdf
```

O PDF utiliza dados da OS, cliente, veículo, serviços, peças, fornecedores e pagamentos.

### 18.2 Exportação de relatórios

O frontend possui recursos de relatório e exportação para apoio gerencial.

Objetivos:

- Facilitar conferência de dados;
- Apoiar apresentação acadêmica;
- Organizar informações de OS, pagamentos e garantias.

---

## 19. Configurações e diagnóstico local

A tela de Configurações utiliza o endpoint:

```text
GET /api/database/status
```

Esse endpoint usa o Singleton reformulado para monitorar a conexão local.

Informações apresentadas:

- Banco disponível ou indisponível;
- URL do banco;
- Usuário;
- Tempo de resposta;
- Último erro sanitizado;
- Falhas consecutivas;
- Quantidade de verificações;
- Última verificação;
- Última mudança de status;
- Uso de cache.

Esse recurso é útil principalmente porque o sistema pode ser executado em computadores diferentes, com configurações diferentes de PostgreSQL, JDK e Maven.

---

## 20. Scripts SQL e documentação

### 20.1 Scripts SQL

```text
database/01_schema
```

Contém scripts de criação e alteração estrutural.

```text
database/02_seed
```

Contém dados iniciais.

```text
database/03_verificacoes
```

Contém consultas para conferência.

```text
database/04_completo
```

Contém script completo opcional para montagem rápida.

### 20.2 Documentação técnica

```text
docs
```

Contém README por etapa, documentação de endpoints e relatórios técnicos.

```text
docs/adr
```

Contém ADRs, ou seja, registros de decisão arquitetural.

Exemplos de decisões registradas:

- Monolito em camadas;
- Factory Method para cliente PF/PJ;
- Adapter para resposta de veículo;
- Fluxo da Ordem de Serviço;
- Garantias após finalização da OS;
- Estrutura de dados para fila de atendimento;
- Decorator para notificação com auditoria;
- Correção do fluxo `ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO`;
- Reformulação do Singleton e Decorator.

---

## 21. Testes e verificação

### 21.1 Build do backend

```bash
mvn clean install -DskipTests
```

### 21.2 Rodar backend

```bash
mvn spring-boot:run
```

### 21.3 Testar API

Acesse:

```text
http://localhost:9081/swagger-ui.html
```

### 21.4 Testar banco

No pgAdmin, execute:

```text
database/03_verificacoes/03_verificacao_geral_sistema.sql
```

Esse script ajuda a conferir se os dados principais estão presentes.

### 21.5 Testar frontend

```bash
cd frontend/oficina-web
npm install
npm start
```

Acesse:

```text
http://localhost:4200
```

### 21.6 Roteiro de teste do fluxo principal

1. Cadastre ou selecione um cliente;
2. Cadastre ou selecione um veículo vinculado ao cliente;
3. Crie uma Ordem de Serviço;
4. Confirme que ela nasceu em `ORCAMENTO`;
5. Acesse Serviços e Peças da OS;
6. Adicione serviços e peças;
7. Envie o orçamento para execução;
8. Acesse a Fila de Atendimento;
9. Confirme que a OS aparece como `EXECUCAO`;
10. Envie para pagamento;
11. Registre pagamento;
12. Quite a OS;
13. Confirme que a OS foi finalizada;
14. Consulte garantias geradas.

---

## 22. Problemas comuns e soluções

### 22.1 Erro de Lombok: cannot find symbol em getters/setters

Sintoma:

```text
cannot find symbol: method getId()
cannot find symbol: method setAtivo()
```

Causa comum:

- Maven usando JDK diferente do projeto;
- NetBeans configurado com Maven em JDK incorreto;
- Annotation Processing desativado.

Solução:

- Configure o Maven para usar JDK 21;
- Confira `mvn -version`;
- Ative Annotation Processing na IDE.

---

### 22.2 Erro: password authentication failed for user postgres

Sintoma:

```text
FATAL: password authentication failed for user "postgres"
```

Causa:

- Senha do PostgreSQL local diferente da senha configurada no projeto.

Solução:

Edite:

```text
src/main/resources/application.properties
```

Ajuste:

```properties
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
```

---

### 22.3 Erro de tabela inexistente

Sintoma:

```text
relation "nome_tabela" does not exist
```

Causa:

- Banco não foi criado;
- Scripts SQL não foram executados;
- Tabela nova da Etapa 57 não foi aplicada.

Solução:

Execute os scripts:

```text
database/01_schema/01_create_schema.sql
database/02_seed/02_seed_inicial.sql
```

Se estiver atualizando uma base antiga:

```text
database/01_schema/03_create_notificacao_auditoria.sql
```

---

### 22.4 Frontend não atualiza após alteração

Solução:

- Pare o `npm start`;
- Inicie novamente;
- No navegador, use `CTRL + F5`;
- Limpe cache se necessário.

---

### 22.5 Angular com erro de versão do Node

Causa:

- Versão do Node incompatível com Angular 22.

Solução:

- Instale uma versão de Node compatível com Angular 22;
- Rode novamente:

```bash
npm install
npm start
```

---

## 23. Observações para GitHub

### 23.1 O que deve subir

Suba para o GitHub:

- Código Java;
- Código Angular;
- Scripts SQL;
- Documentação;
- ADRs;
- README;
- Assets do frontend;
- Arquivos de configuração do projeto.

### 23.2 O que não deve subir

Não suba:

```text
node_modules
target
.git
.idea
.vscode com configurações pessoais
arquivos temporários
logs locais
backups pessoais
```

### 23.3 Senha do banco

O arquivo `application.properties` contém senha local de desenvolvimento. Para projeto acadêmico local, isso pode ser aceitável, mas para repositório público o ideal é documentar que cada usuário deve ajustar a senha conforme o próprio PostgreSQL.

Sugestão para projetos futuros:

- Usar variáveis de ambiente;
- Criar `application-example.properties`;
- Não versionar senhas reais.

---

## 24. Status do projeto

Estado atual:

```text
Etapa 57 — Reformulação do Singleton e do Decorator
```

Principais entregas já contempladas:

- Backend Spring Boot em camadas;
- Frontend Angular integrado;
- Banco PostgreSQL com scripts;
- Cadastros principais;
- Fluxo completo de OS;
- Fila de Atendimento corrigida;
- Pagamento e finalização;
- Garantias de peças e serviços;
- PDF de OS;
- Relatórios;
- Padrões de projeto;
- Estrutura de dados;
- Auditoria de notificações;
- Diagnóstico local do banco;
- Documentação técnica por etapa.

---

## 25. Créditos acadêmicos

Projeto desenvolvido para fins acadêmicos no contexto do Projeto Integrador de Análise e Desenvolvimento de Sistemas.

Tema:

```text
Sistema para o controle de uma Oficina Mecânica
```

Oficina usada como contexto:

```text
AV CAR AUTO CENTER
```

Objetivo acadêmico:

- Aplicar modelagem de banco de dados;
- Aplicar arquitetura em camadas;
- Aplicar padrões de projeto;
- Aplicar estrutura de dados;
- Implementar backend e frontend integrados;
- Documentar decisões técnicas;
- Demonstrar software funcional.

---

## Licença

Este projeto foi desenvolvido para fins acadêmicos. Caso seja reutilizado ou evoluído, recomenda-se incluir uma licença formal no repositório, como MIT, Apache 2.0 ou outra licença definida pelo grupo.

