# Etapa 21 — Consolidação do Script SQL do Banco de Dados

## 1. Objetivo

Esta etapa organiza os scripts de banco de dados do projeto, criando um arquivo único para facilitar a execução no pgAdmin e evitar que o aluno precise executar vários arquivos em sequência.

## 2. Arquivo principal criado

```text
database/00_SCRIPT_COMPLETO_BANCO.sql
```

Esse arquivo passa a ser o script recomendado para criar o banco físico e inserir os dados iniciais obrigatórios do sistema.

## 3. Conteúdo do script único

O script consolidado contém:

```text
1. Criação das tabelas do modelo físico;
2. Chaves primárias;
3. Chaves estrangeiras;
4. Constraints de integridade;
5. Índices relevantes;
6. Inserts iniciais de status da OS;
7. Inserts iniciais de funções;
8. Inserts iniciais de marcas, modelos, serviços, fornecedores e peças;
9. Consultas rápidas de conferência.
```

## 4. Como executar

No pgAdmin:

```text
1. Criar o banco car_repair;
2. Abrir o Query Tool no banco car_repair;
3. Abrir o arquivo database/00_SCRIPT_COMPLETO_BANCO.sql;
4. Executar o script inteiro;
5. Rodar o backend Spring Boot.
```

## 5. Observação sobre os demais scripts

Os arquivos `01_create_schema.sql` e `02_seed_inicial.sql` foram mantidos por organização histórica e para consulta separada, mas a execução prática deve priorizar o arquivo único.

Os arquivos `03_verificacao...`, `04_verificacao...` e demais scripts de verificação são opcionais e servem apenas para validar etapas específicas do projeto.

## 6. Relação com a arquitetura

A aplicação continua usando:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Assim, o banco é criado manualmente por script SQL e o Spring Data JPA apenas valida se as tabelas estão compatíveis com os Models.

## 7. Benefício acadêmico

A consolidação facilita a apresentação do projeto, reduz erro operacional durante testes, melhora a organização do repositório e demonstra controle do modelo físico do banco de dados.
