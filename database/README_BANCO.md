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
