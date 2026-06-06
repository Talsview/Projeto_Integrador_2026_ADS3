# Banco de Dados Local - AV CAR AUTO CENTER

## Decisão adotada

O banco físico será criado por script SQL, e a aplicação Spring Boot apenas validará a estrutura existente com:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Essa decisão evita dependência do Hibernate para criação automática das tabelas e facilita a entrega acadêmica do modelo físico.

## Ordem de execução no pgAdmin

```text
1. Criar o banco car_repair no PostgreSQL.
2. Executar database/01_create_schema.sql.
3. Executar database/02_seed_inicial.sql.
4. Iniciar a aplicação Spring Boot.
5. Acessar o Swagger em http://localhost:9081/swagger-ui.html.
```

## Observação

Os campos de identificador usam `BIGSERIAL`, compatível com o uso de `Long` no Java.

## Atualização da Etapa 5

O script `01_create_schema.sql` já contempla as tabelas do módulo de Serviços:

```text
servico
servico_interno
servico_terceirizado
empresa_terceirizada
```

O script `02_seed_inicial.sql` inclui serviços iniciais para testes do Swagger e do Angular, separando serviços internos e terceirizados conforme a generalização/especialização validada no MER.

## Atualização da Etapa 6

O script `01_create_schema.sql` contempla também as tabelas do módulo de Peças:

```text
fornecedor
peca
item_peca
garantia_peca
```

Nesta etapa foram implementadas no Java as entidades `Fornecedor`, `Peca` e `ItemPeca`. A tabela `garantia_peca` permanece no modelo físico e será integrada ao fluxo completo quando a Ordem de Serviço e sua finalização forem implementadas.

Também foram incluídos índices únicos parciais para evitar duplicidade de CNPJ de fornecedor ativo e de código nacional de peça ativa, mantendo compatibilidade com a regra de exclusão lógica.

O script `02_seed_inicial.sql` inclui fornecedores e peças iniciais para facilitar os testes pelo Swagger e pelo Angular.

## Atualização da Etapa 7

O script `01_create_schema.sql` já contempla as tabelas centrais do módulo de Ordem de Serviço:

```text
ordem_servico
status_ordem_servico
historico_status_ordem
item_servico
execucao_servico_terceirizado
```

Nesta etapa essas tabelas foram integradas ao código Java por meio de Models, DTOs, Repositories, Validations, Services, Controllers e Responses padronizadas.

O script `02_seed_inicial.sql` já contém os quatro status oficiais da OS:

```text
ORCAMENTO
EXECUCAO
PAGAMENTO
FINALIZADO
```

Esses registros são necessários para que o fluxo da OS funcione corretamente.

## Atualização da Etapa 8

O script `01_create_schema.sql` passa a contemplar integralmente as tabelas de garantia utilizadas pela aplicação:

```text
garantia_peca
garantia_servico
```

Também foi adicionado o campo abaixo à tabela `peca`:

```text
prazo_garantia_dias INTEGER NOT NULL DEFAULT 90
```

Esse campo permite que a garantia da peça aplicada na OS tenha prazo próprio. A garantia de serviço continua utilizando o campo `prazo_garantia_dias` da tabela `servico`.

As garantias são criadas quando os itens são lançados na OS e iniciadas automaticamente quando a Ordem de Serviço passa para o status `FINALIZADO`.

## Atualização da Etapa 9

O script `01_create_schema.sql` contempla a tabela financeira da Ordem de Serviço:

```text
pagamento
```

Nesta etapa a tabela `pagamento` foi integrada ao código Java por meio de Model, DTO, Repository, Validation, Mapper, Service e Controller REST.

Regras financeiras aplicadas:

```text
Uma Ordem de Serviço pode gerar nenhum, um ou vários pagamentos.
Pagamentos só podem ser registrados quando a OS está no status PAGAMENTO.
Apenas pagamentos com status PAGO abatem o saldo financeiro da OS.
A OS só pode avançar para FINALIZADO quando o total pago for igual ou superior ao valor total da OS.
Pagamentos não podem ser alterados após a finalização da OS.
```

Também foi corrigida a duplicidade da coluna `id_empresa_terceirizada` na tabela `execucao_servico_terceirizado` e incluída a restrição `ck_pagamento_forma` para padronizar as formas de pagamento aceitas.

## Atualização da Etapa 13

Não houve criação de novas tabelas nesta etapa. A atualização foi voltada à integração do frontend Angular com o backend Spring Boot.

Foi adicionado o script auxiliar:

```text
database/03_verificacao_integracao_frontend.sql
```

Esse script não altera dados e não altera a estrutura do banco. Ele apenas consulta registros importantes para conferir se o ambiente está pronto para o Angular consumir a API REST.

Ordem recomendada para testes completos:

```text
1. Criar o banco car_repair.
2. Executar database/01_create_schema.sql.
3. Executar database/02_seed_inicial.sql.
4. Executar database/03_verificacao_integracao_frontend.sql para conferência.
5. Rodar o backend em http://localhost:9081.
6. Rodar o Angular em http://localhost:4200 usando npm.cmd run start:proxy.
```

## Atualização da Etapa 14

Não houve criação de novas tabelas nesta etapa. A atualização foi voltada à melhoria de desempenho percebido na comunicação entre Angular e backend.

Foi adicionado o script auxiliar:

```text
database/04_verificacao_desempenho_integracao.sql
```

Esse script não altera dados e não altera estrutura. Ele apenas consulta o banco para confirmar se o PostgreSQL está respondendo rapidamente antes do teste no Angular.

Também foi ajustado o endpoint:

```text
GET /api/database/status
```

Agora ele retorna:

```text
available
mensagem
tempoRespostaMs
```

Esse ajuste facilita identificar se a lentidão está no frontend, no backend ou na conexão local com o PostgreSQL.

---

# Etapa 15 — Verificação de dados para telas Angular

Foi adicionado o script:

```text
database/05_verificacao_frontend_telas.sql
```

Esse script não altera dados. Ele apenas consulta a quantidade de registros nas principais tabelas usadas pelas telas Angular, ajudando a verificar se existem dados suficientes para testar cadastros, consultas, OS, itens, pagamentos e garantias.

## Etapa 16 — Verificação da atualização automática do frontend

Foi adicionado o script:

```text
database/06_verificacao_frontend_estados_atualizacao.sql
```

Esse script auxilia a conferência dos registros criados pelo Angular após operações de cadastro, alteração ou exclusão lógica. Ele pode ser executado no pgAdmin para validar se a tabela do frontend está refletindo os dados persistidos no PostgreSQL.

---

# Verificação da Etapa 17 — Atualização visual do Angular

Arquivo adicionado:

```text
07_verificacao_correcao_atualizacao_visual.sql
```

Este script permite verificar no PostgreSQL se os registros cadastrados pelo frontend estão sendo persistidos corretamente. A correção visual foi aplicada no Angular, por meio do interceptor global da API e da configuração explícita de detecção de mudanças.


---

# Verificação da Etapa 18 — Pagamentos e status atual da OS

Arquivo adicionado:

```text
08_verificacao_pagamentos_status_os.sql
```

Este script consulta o status atual das Ordens de Serviço. Ele ajuda a confirmar se a OS selecionada está em `PAGAMENTO`, que é a condição necessária para registrar pagamento no sistema.

A tela Angular de Pagamentos agora também exibe esse status e impede o envio de pagamento quando a OS ainda está em `ORCAMENTO` ou `EXECUCAO`.


## Script 09 — Verificação do fluxo automático de pagamentos

O arquivo `09_verificacao_fluxo_automatico_pagamentos.sql` permite conferir no banco o status atual das Ordens de Serviço e os pagamentos registrados após a correção da Etapa 19.

---

## Etapa 20 — Correção de compilação Angular

A Etapa 20 não altera tabelas, constraints ou dados do banco de dados.

Foi criado apenas o script de verificação documental:

```text
database/10_verificacao_correcao_compilacao_angular.sql
```

Essa etapa corrige exclusivamente a herança dos services Angular especializados, removendo duplicidade da propriedade `tempoLimiteMs`.
