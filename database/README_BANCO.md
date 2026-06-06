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
