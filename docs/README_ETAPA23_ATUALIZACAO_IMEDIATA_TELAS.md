# Etapa 23 — Atualização Imediata das Tabelas após Cadastro

## 1. Objetivo

Esta etapa tem como objetivo corrigir a experiência de uso do frontend Angular, garantindo que as tabelas de consulta sejam atualizadas imediatamente após operações como salvar, editar ou inativar registros.

O problema foi identificado durante os testes nas telas de Clientes e Funções: o registro era salvo no backend, porém a tabela só mostrava a informação atualizada depois de o usuário clicar novamente em outro botão.

## 2. Problema observado

O comportamento anterior prejudicava o fluxo operacional da oficina, pois o usuário recebia a mensagem de sucesso, mas não enxergava imediatamente o novo registro na tabela.

Exemplo do problema:

```text
1. Usuário cadastra um cliente.
2. Backend salva o cliente corretamente.
3. Frontend exibe mensagem de sucesso.
4. Tabela não mostra imediatamente o novo cliente.
5. Usuário precisa clicar em “Listar todos” para a tabela atualizar.
```

## 3. Correção aplicada

A correção foi feita em duas frentes.

### 3.1 Correção na camada de serviço Angular

O `BaseApiService` passou a enviar parâmetros e cabeçalhos de não cache nas consultas GET.

```text
Cache-Control: no-cache
Pragma: no-cache
_t=<timestamp atual>
```

Dessa forma, após uma gravação, a consulta de atualização busca novamente os dados no backend, evitando reaproveitamento de resposta anterior pelo navegador ou pelo proxy de desenvolvimento.

### 3.2 Correção nos componentes de tela

As telas de Clientes e Funções passaram a seguir o fluxo encadeado:

```text
salvar no backend → consultar novamente no backend → substituir a lista exibida na tela
```

Esse fluxo evita chamadas soltas e garante que a tabela seja atualizada somente após a nova consulta ser concluída.

## 4. Arquivos alterados

```text
frontend/oficina-web/src/app/core/services/base-api.service.ts
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.html
frontend/oficina-web/src/app/pages/funcoes/funcoes.component.ts
frontend/oficina-web/src/app/pages/funcoes/funcoes.component.html
README.md
database/README_BANCO.md
docs/adr/ADR-026-atualizacao-imediata-tabelas-angular.md
database/03_verificacoes/12_verificacao_atualizacao_imediata_telas.sql
```

## 5. Resultado esperado

Ao abrir uma tela de consulta, os registros existentes no banco devem aparecer automaticamente.

Ao salvar um cliente ou função, o usuário deve observar:

```text
1. Botão muda para estado de salvamento.
2. Registro é salvo no backend.
3. Tabela é sincronizada automaticamente com o banco.
4. Mensagem de sucesso é exibida.
5. O novo registro aparece na tabela sem clique adicional.
```

## 6. Observação acadêmica

A alteração melhora a usabilidade e a confiabilidade da camada View, pois o comportamento da interface passa a representar imediatamente o estado persistido no banco de dados. Isso reforça os requisitos não funcionais de clareza, consistência visual, integridade operacional e rastreabilidade das operações realizadas no sistema da oficina mecânica.
