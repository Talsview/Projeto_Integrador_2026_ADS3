# Etapa 24 — Atualização Global das Telas Angular

## 1. Objetivo

Esta etapa corrige de forma global o comportamento observado nas telas do frontend Angular, em que alguns cadastros eram salvos no backend, mas a tabela somente era atualizada visualmente após uma nova ação do usuário, como clicar em outro botão, pesquisar ou listar novamente.

O problema já havia sido corrigido nas telas de Clientes e Funções. Nesta etapa, a estratégia foi aplicada de forma geral para todas as abas do sistema.

## 2. Problema identificado

O backend estava persistindo os dados corretamente no PostgreSQL. Porém, em determinadas telas, o Angular não redesenhava a interface imediatamente após a resposta da API. Dessa forma, o usuário tinha a impressão de que o cadastro ainda estava carregando ou que a listagem não havia sido atualizada.

Esse comportamento prejudicava a usabilidade do sistema de oficina, pois o usuário espera que, ao salvar um cliente, veículo, serviço, peça, fornecedor, colaborador, ordem de serviço, item da OS, pagamento ou garantia, a tabela correspondente seja atualizada automaticamente.

## 3. Correção aplicada

A correção foi centralizada no interceptor HTTP global do Angular.

Agora, após cada resposta HTTP da API, o frontend força uma sincronização visual segura da aplicação, garantindo que os dados retornados pelo backend sejam refletidos imediatamente na tela.

Além disso, o `BaseApiService` passou a retornar novas referências de array nas consultas, evitando que a tabela mantenha uma referência antiga e não redesenhe visualmente os registros.

## 4. Abas beneficiadas

A correção afeta todas as telas operacionais do sistema:

```text
Clientes
Funções
Colaboradores
Marcas e Modelos
Veículos
Serviços
Empresas Terceirizadas
Peças e Fornecedores
Ordens de Serviço
Itens da OS
Pagamentos
Garantias
Fila de Atendimento
```

## 5. Organização visual do sistema

O frontend também permanece organizado como um sistema operacional de oficina mecânica, e não como um dashboard genérico.

O menu lateral está dividido em dois grupos principais:

```text
Cadastros
- Clientes
- Veículos
- Colaboradores
- Funções
- Marcas e Modelos
- Serviços
- Empresas Terceirizadas
- Peças e Fornecedores

Ordens de Serviço
- Abertura e consulta de OS
- Serviços e peças da OS
- Fila de atendimento
- Pagamentos
- Garantias
```

A aba antiga de Padrões de Projeto foi removida do menu principal. A antiga aba de Estrutura de Dados foi renomeada para Fila de Atendimento, mantendo a funcionalidade acadêmica sem expor um nome técnico ao usuário final.

## 6. Arquivos alterados

```text
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
frontend/oficina-web/src/app/core/services/base-api.service.ts
frontend/oficina-web/src/app/core/services/item-peca-api.service.ts
frontend/oficina-web/src/app/core/services/item-servico-api.service.ts
frontend/oficina-web/src/app/core/services/modelo-api.service.ts
frontend/oficina-web/src/app/core/services/pagamento-api.service.ts
frontend/oficina-web/src/app/core/services/garantia-api.service.ts
frontend/oficina-web/src/app/app.component.html
frontend/oficina-web/src/app/app.component.css
frontend/oficina-web/src/app/app.routes.ts
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.html
```

## 7. Resultado esperado

Ao abrir uma aba, os dados cadastrados no banco devem ser exibidos automaticamente.

Ao salvar, alterar, inativar, acionar ou encerrar qualquer registro, a respectiva tabela deve ser atualizada sem exigir novo clique do usuário.

## 8. Teste recomendado

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

Após iniciar o Angular, recomenda-se atualizar o navegador com `CTRL + F5` para evitar uso de arquivos antigos em cache.
