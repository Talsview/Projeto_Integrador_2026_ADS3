# Banco de Dados Local — AV CAR AUTO CENTER

## Organização oficial dos scripts

A partir da Etapa 22, os scripts SQL foram reorganizados para evitar confusão entre criação do banco, carga inicial e verificações de teste.

```text
database
├── 01_schema
│   └── 01_create_schema.sql
├── 02_seed
│   └── 02_seed_inicial.sql
├── 03_verificacoes
│   ├── 03_verificacao_integracao_frontend.sql
│   ├── 04_verificacao_desempenho_integracao.sql
│   ├── 05_verificacao_frontend_telas.sql
│   ├── 06_verificacao_frontend_estados_atualizacao.sql
│   ├── 07_verificacao_correcao_atualizacao_visual.sql
│   ├── 08_verificacao_pagamentos_status_os.sql
│   ├── 09_verificacao_fluxo_automatico_pagamentos.sql
│   ├── 10_verificacao_correcao_compilacao_angular.sql
│   └── 11_verificacao_carregamento_inicial_telas.sql
└── 04_completo
    └── 00_SCRIPT_COMPLETO_BANCO.sql
```

## Forma recomendada para montar o banco do zero

No pgAdmin, crie o banco `car_repair` e execute os scripts nesta ordem:

```text
1. database/01_schema/01_create_schema.sql
2. database/02_seed/02_seed_inicial.sql
```

Depois disso, rode o backend Spring Boot.

## Script completo opcional

O arquivo abaixo permanece disponível apenas como alternativa para demonstração ou recuperação rápida:

```text
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
```

Ele contém criação de tabelas e dados iniciais em um único arquivo. Porém, para organização acadêmica e manutenção do projeto, recomenda-se usar os scripts separados por finalidade.

## Scripts de verificação

Os scripts da pasta abaixo não devem ser usados para criar o banco nem para alimentar dados obrigatórios:

```text
database/03_verificacoes
```

Eles servem apenas para conferência no pgAdmin, por exemplo:

```text
- verificar se existem funções cadastradas;
- verificar se existem marcas, modelos e serviços iniciais;
- conferir se o Angular possui dados para exibir nas telas;
- validar se pagamentos e ordens de serviço estão sendo atualizados.
```

## Configuração do Spring Boot

O projeto usa banco físico criado manualmente por SQL. Por isso, no `application.properties`, a aplicação deve validar a estrutura existente:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Essa decisão evita que o Hibernate crie ou altere tabelas automaticamente, mantendo fidelidade ao modelo físico entregue no projeto.

## Identificadores

Todas as chaves primárias foram definidas como `BIGSERIAL` no PostgreSQL, compatíveis com `Long` no Java.

## Regra de execução

Para evitar erro de tabela inexistente ou dados obrigatórios ausentes, sempre respeite esta ordem:

```text
1. Criar banco car_repair.
2. Executar schema.
3. Executar seed.
4. Rodar backend.
5. Rodar Angular.
```

## Observação sobre dados iniciais

O `02_seed_inicial.sql` inclui registros essenciais para o sistema funcionar, como:

```text
- Status da Ordem de Serviço: ORCAMENTO, EXECUCAO, PAGAMENTO e FINALIZADO;
- Funções iniciais: Mecânico, Atendente, Secretária, Faxineiro, Estoquista e Gerente;
- Marcas e modelos para testes;
- Serviços internos e terceirizados;
- Fornecedores e peças iniciais.
```

Esses registros são importantes para que as telas Angular carreguem opções e consultas logo ao serem abertas.

## Verificação da Etapa 22

Foi adicionado o script:

```text
database/03_verificacoes/11_verificacao_carregamento_inicial_telas.sql
```

Esse script consulta a quantidade de registros das principais tabelas usadas pelo frontend. Ele ajuda a confirmar se existem dados para serem exibidos automaticamente quando o usuário abre as telas de cadastro e consulta.

## Verificação da Etapa 23

Foi adicionado o script:

```text
database/03_verificacoes/12_verificacao_atualizacao_imediata_telas.sql
```

Esse script permite conferir os registros usados nas telas de Clientes e Funções, especialmente após testar cadastros pelo frontend Angular. Ele não cria tabelas nem insere dados; serve apenas para conferência no pgAdmin.

---

# Verificação da Etapa 24 — Atualização Global das Telas

Foi adicionado o script:

```text
database/03_verificacoes/13_verificacao_atualizacao_global_telas.sql
```

Esse script ajuda a conferir, no banco PostgreSQL, se os cadastros feitos pelo frontend foram efetivamente gravados nas tabelas principais.

A organização dos scripts permanece:

```text
database/01_schema       → criação da estrutura do banco
database/02_seed         → dados iniciais
database/03_verificacoes → conferências e validações
database/04_completo     → script completo opcional
```

---

## Verificação da Etapa 25 — Comunicação global com o frontend

Para confirmar se há dados para exibição nas telas do Angular, execute:

```text
database/03_verificacoes/14_verificacao_comunicacao_global_frontend.sql
```

Esse script confere a quantidade de registros ativos nas principais tabelas utilizadas pelas telas de cadastro e de ordens de serviço.
