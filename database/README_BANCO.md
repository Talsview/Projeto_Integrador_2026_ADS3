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


## Observação da Etapa 32

A Etapa 32 não alterou o modelo físico, scripts SQL ou dados iniciais. As alterações foram concentradas no frontend Angular, especificamente na paleta visual e na aplicação das logos da AV CAR AUTO CENTER.

## Observação — Etapa 33

A Etapa 33 alterou somente a identidade visual do frontend Angular, com implementação de logos responsivas e favicon. Nenhuma tabela, chave, constraint, seed ou regra do banco de dados foi modificada nesta etapa.


## Etapa 34 - Nota Fiscal / Recibo em PDF

Esta etapa não altera a estrutura física do banco de dados. A geração do PDF utiliza dados já existentes nas tabelas de Ordem de Serviço, Cliente, Veículo, ItemServico, ItemPeca, Fornecedor e Pagamento.

---

## Etapa 44 — Seeds completas e verificação SQL unificada

A partir desta etapa, a seed inicial foi ampliada para conter dados mais completos para uso acadêmico, teste do frontend Angular e demonstração do sistema funcionando.

Arquivo atualizado:

```text
database/02_seed/02_seed_inicial.sql
```

A seed agora inclui:

```text
- clientes pessoa física e pessoa jurídica com CPF/CNPJ válidos;
- colaboradores com funções vinculadas;
- marcas e modelos variados;
- veículos com histórico de proprietário;
- serviços internos e terceirizados;
- empresas terceirizadas;
- fornecedores e peças;
- ordens de serviço em orçamento, execução, pagamento e finalizado;
- itens de serviço com colaborador responsável;
- itens de peça com fornecedor identificado;
- garantias de peças e serviços;
- pagamentos parciais e quitados;
- OS apta para teste de PDF/recibo interno.
```

Os scripts antigos de verificação foram unificados em um único arquivo:

```text
database/03_verificacoes/03_verificacao_geral_sistema.sql
```

Esse script substitui os arquivos antigos de verificação e deve ser usado no pgAdmin para conferir:

```text
- conexão com o banco;
- quantidade de registros por tabela;
- dados disponíveis para o Angular;
- clientes PF/PJ;
- colaboradores e funções;
- veículos e proprietários;
- ordens de serviço e status;
- resumo financeiro;
- serviços, peças, fornecedores;
- garantias;
- dados para geração de PDF;
- inconsistências de rastreabilidade.
```

O script completo também foi atualizado:

```text
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
```

Ele contém a criação da estrutura física e a seed completa em um único arquivo.

## Etapa 45 — Atualização para atendimento de garantia

Se o banco já foi criado antes da Etapa 45, execute o script incremental abaixo antes de iniciar o backend com `spring.jpa.hibernate.ddl-auto=validate`:

```text
database/01_schema/02_alter_garantia_atendimento.sql
```

Esse script adiciona às tabelas `garantia_peca` e `garantia_servico` os campos de acionamento e encerramento da garantia.

Para recriação completa do banco, utilize diretamente:

```text
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
```

## Etapa 57 — Tabela de auditoria de notificações

A partir da Etapa 57, o padrão Decorator passou a persistir auditorias operacionais na tabela `notificacao_auditoria`.

Para bancos já existentes, execute:

```text
database/01_schema/03_create_notificacao_auditoria.sql
```

Essa tabela é necessária porque o projeto utiliza `spring.jpa.hibernate.ddl-auto=validate`, ou seja, o backend valida se todas as tabelas mapeadas existem no banco físico.

## Etapa 58 — Peça com fornecedor padrão e valor padrão

A Etapa 58 adicionou rastreabilidade direta entre o cadastro de peça e o fornecedor padrão, além do valor unitário padrão da peça.

Para bancos já existentes, execute antes de iniciar o backend atualizado:

```text
database/01_schema/04_alter_peca_fornecedor_valor.sql
```

Campos adicionados na tabela `peca`:

```text
id_fornecedor_padrao
valor_unitario_padrao
```

Esses campos permitem que, na tela de Itens da OS, ao selecionar uma peça, o sistema carregue automaticamente o fornecedor relacionado e o valor unitário cadastrado.

## Etapa 68 — Seed atualizada para histórico de proprietários por cliente

A seed inicial foi atualizada para refletir a tela atual de **Gestão > Histórico de Proprietários**, que agora trabalha com visão agrupada por cliente.

Arquivo atualizado:

```text
database/02_seed/02_seed_inicial.sql
```

A atualização mantém os cadastros já existentes, mas melhora os registros de `historico_proprietario` para demonstrar corretamente:

```text
- clientes aparecendo uma única vez na consulta de histórico;
- cliente com mais de um veículo atual;
- cliente com veículo atual e posse antiga;
- proprietários anteriores preservados após transferência;
- data de início e data de fim das posses;
- diferença entre posse atual e posse anterior;
- pessoa física e pessoa jurídica no histórico de veículos.
```

O script de verificação geral também recebeu consultas específicas para conferir o histórico consolidado por cliente:

```text
database/03_verificacoes/03_verificacao_geral_sistema.sql
```

O script completo foi regenerado para manter a mesma seed atualizada:

```text
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
```

Para banco novo, basta executar o schema e depois a seed. Para banco já criado com a seed antiga, a nova seed pode ser executada novamente, pois os novos registros históricos foram protegidos com verificações `NOT EXISTS`.

## Etapa 69 — Serviço terceirizado sem colaborador interno obrigatório

A Etapa 69 corrigiu a regra de cadastro de serviços terceirizados na OS. Para serviço interno, o sistema continua exigindo o colaborador responsável da oficina. Para serviço terceirizado, o responsável operacional passa a ser a empresa terceirizada executora, registrada na tabela `execucao_servico_terceirizado`.

Para bancos já existentes, execute antes de iniciar o backend atualizado:

```text
database/01_schema/05_alter_item_servico_colaborador_opcional_terceirizado.sql
```

Esse script altera a coluna `item_servico.id_colaborador` para aceitar `NULL`, permitindo que itens de serviço terceirizado sejam registrados sem colaborador interno. O vínculo com a empresa externa continua obrigatório para serviços terceirizados.

Para banco novo, utilize diretamente:

```text
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
```
