# Etapa 22 — Organização dos Scripts SQL e Carregamento Inicial Automático das Telas

## 1. Objetivo

Esta etapa corrige dois pontos identificados durante os testes do sistema:

```text
1. Organizar os scripts SQL em grupos claros: schema, seed, verificações e script completo opcional.
2. Corrigir o comportamento visual do Angular para que as telas carreguem automaticamente os dados do backend ao serem abertas.
```

## 2. Organização dos scripts SQL

Os arquivos de banco foram reorganizados da seguinte forma:

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

## 3. Padrão de execução recomendado

Para criar o banco do zero, recomenda-se executar apenas:

```text
1. database/01_schema/01_create_schema.sql
2. database/02_seed/02_seed_inicial.sql
```

Os scripts da pasta `03_verificacoes` são usados apenas para consulta e conferência no pgAdmin.

## 4. Correção no carregamento das telas Angular

Durante o teste, foi observado que algumas telas carregavam os dados do backend, mas a tabela só era redesenhada depois de uma interação manual, como clicar em outro botão ou digitar em algum campo.

Para corrigir esse comportamento, o interceptor global da API foi ajustado para:

```text
1. Executar as respostas HTTP dentro do NgZone do Angular.
2. Forçar uma atualização visual após cada retorno HTTP.
3. Evitar que a tela fique presa em mensagens como “Carregando...” mesmo depois de a API responder.
```

## 5. Ajuste no comando npm start

O comando `npm start` agora utiliza automaticamente o proxy do Angular:

```json
"start": "ng serve --proxy-config proxy.conf.json --open"
```

Com isso, tanto `npm.cmd start` quanto `npm.cmd run start:proxy` apontam corretamente `/api` para o backend em:

```text
http://localhost:9081
```

## 6. Resultado esperado

Ao abrir uma tela como `Funções`, `Clientes`, `Veículos`, `Serviços` ou `Pagamentos`, o sistema deve:

```text
1. Chamar automaticamente o endpoint de listagem correspondente.
2. Exibir os dados já cadastrados no banco sem exigir clique adicional.
3. Atualizar a tabela após operações de salvar, editar, excluir ou consultar.
```

## 7. Arquivos alterados

```text
frontend/oficina-web/package.json
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
database/README_BANCO.md
database/01_schema/01_create_schema.sql
database/02_seed/02_seed_inicial.sql
database/03_verificacoes/11_verificacao_carregamento_inicial_telas.sql
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
README.md
docs/README_ETAPA22_ORGANIZACAO_SQL_CARREGAMENTO_TELAS.md
docs/adr/ADR-025-organizacao-sql-carregamento-inicial-telas.md
```

## 8. Como testar

Backend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn.cmd spring-boot:run
```

Frontend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

Depois, abrir uma tela como:

```text
http://localhost:4200/funcoes
```

A tabela deve carregar automaticamente os dados existentes no PostgreSQL.
