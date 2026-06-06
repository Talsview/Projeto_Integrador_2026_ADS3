# Banco de Dados — AV CAR AUTO CENTER

A organização do banco permanece separada por finalidade:

```text
database/01_schema       → estrutura física do banco
database/02_seed         → dados iniciais
database/03_verificacoes → conferências e testes
database/04_completo     → script completo opcional
```

## Etapa 31

A Etapa 31 alterou apenas o frontend Angular, reorganizando o menu superior em categorias. Não houve alteração no modelo físico, nos scripts SQL, nas constraints ou nas seeds.
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


## Observação da Etapa 26

A organização dos scripts SQL permanece separada por finalidade:

```text
01_schema       -> criação da estrutura física do banco
02_seed         -> dados iniciais obrigatórios
03_verificacoes -> consultas de conferência e testes
04_completo     -> script completo opcional para montagem rápida
```

Essa separação evita confusão entre criação do banco, carga inicial e testes de validação.


## Etapa 27 — Ajustes visuais sem alteração do banco

A reformulação visual do Angular não alterou o modelo físico, os scripts de schema, os seeds ou os endpoints consumidos pelo frontend.

A organização atual permanece:

```text
database/01_schema       → estrutura do banco
database/02_seed         → dados iniciais
database/03_verificacoes → scripts de conferência
database/04_completo     → script completo opcional
```


## Observação da Etapa 28

A etapa 28 foi uma alteração visual do frontend Angular. Não houve mudança estrutural no banco de dados. Os scripts permanecem organizados por finalidade: schema, seed, verificações e script completo opcional.


## Etapa 30

A etapa 30 alterou apenas a camada visual do Angular, substituindo o menu lateral por abas superiores e ajustando o estilo para uma aparência mais quadrada. Nenhuma tabela, chave estrangeira, constraint ou seed do banco de dados foi alterada nesta etapa.
