# Etapa 44 — Seeds completas e verificação SQL unificada

## Objetivo

Esta etapa melhora a base inicial do banco de dados e simplifica os scripts de conferência usados no pgAdmin.

A alteração foi motivada pela necessidade de deixar o sistema com dados mais completos para demonstração acadêmica e para testes reais das telas do Angular, especialmente clientes, veículos, colaboradores, funções, serviços, peças, fornecedores, ordens de serviço, pagamentos, garantias e geração de PDF.

## Alterações realizadas

### 1. Seed inicial mais completa

O arquivo abaixo foi reestruturado:

```text
database/02_seed/02_seed_inicial.sql
```

Agora ele contém dados iniciais mais próximos do funcionamento real da oficina:

- status do fluxo da OS;
- funções de colaboradores;
- marcas e modelos de veículos;
- serviços internos e terceirizados;
- empresas terceirizadas;
- fornecedores;
- peças;
- clientes pessoa física e jurídica;
- colaboradores com funções;
- veículos com histórico de proprietário;
- ordens de serviço em diferentes status;
- itens de serviço com colaborador responsável;
- itens de peça com fornecedor identificado;
- garantias de peças e serviços;
- pagamentos parciais e quitados.

Os documentos CPF e CNPJ usados na seed foram escolhidos com dígitos verificadores válidos, para respeitar as validações implementadas no backend e no Angular.

### 2. Cenários cobertos pela seed

A seed agora deixa o banco pronto para testar:

- cliente pessoa física;
- cliente pessoa jurídica;
- veículo com proprietário atual;
- colaborador com uma ou mais funções;
- OS em orçamento;
- OS em execução;
- OS em pagamento;
- OS finalizada;
- OS com pagamento parcial;
- OS quitada;
- OS com peças e fornecedores;
- garantias aguardando finalização;
- garantias vigentes;
- emissão de PDF/recibo interno para OS finalizada e quitada.

### 3. Script de verificação unificado

Os vários scripts antigos da pasta `database/03_verificacoes` foram consolidados em um único arquivo:

```text
database/03_verificacoes/03_verificacao_geral_sistema.sql
```

Esse arquivo reúne as consultas que antes estavam espalhadas por vários scripts, incluindo:

- diagnóstico da conexão com PostgreSQL;
- contagem geral de registros;
- dados necessários para o Angular;
- clientes com tipo PF/PJ;
- colaboradores com funções;
- veículos com proprietário atual;
- ordens de serviço com status atual;
- resumo financeiro;
- serviços por OS;
- peças por OS com fornecedor;
- garantias;
- conferência para PDF;
- alertas de inconsistência de rastreabilidade.

### 4. Script completo atualizado

O arquivo abaixo também foi atualizado:

```text
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
```

Ele agora contém:

```text
1. Criação da estrutura física do banco.
2. Seed inicial completa.
```

Esse arquivo pode ser usado para montar rapidamente o banco em outra máquina.

## Ordem recomendada de execução

Para montagem organizada do banco:

```text
1. database/01_schema/01_create_schema.sql
2. database/02_seed/02_seed_inicial.sql
3. database/03_verificacoes/03_verificacao_geral_sistema.sql
```

Como alternativa, para montagem rápida:

```text
1. database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
2. database/03_verificacoes/03_verificacao_geral_sistema.sql
```

## Observação

O script de verificação não altera dados e não cria tabelas. Ele serve somente para conferência, documentação técnica e evidência de funcionamento.
