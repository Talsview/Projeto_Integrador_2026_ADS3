# Etapa 25 — Correção global da comunicação do Angular com o backend

## Objetivo

Esta etapa corrige de forma global o problema em que algumas telas do Angular somente exibiam dados do banco após o usuário clicar em outro botão, digitar em algum campo ou executar uma ação manual.

O comportamento esperado passa a ser:

```text
1. Usuário abre a tela.
2. Angular consulta automaticamente o backend.
3. Backend consulta o PostgreSQL.
4. A tabela da tela é atualizada imediatamente.
5. Após salvar, editar, inativar ou acionar um registro, a lista é recarregada automaticamente.
```

## Problema identificado

A tela de Clientes já havia sido corrigida com atualização explícita da tabela após a resposta do backend. Porém, outras telas ainda mantinham chamadas assíncronas isoladas, compartilhamento inadequado do estado `carregando` e ausência de sincronização visual explícita após respostas HTTP.

Foram encontrados os seguintes problemas recorrentes:

```text
- Componentes chamavam a API, mas não forçavam nova referência de lista em todas as respostas.
- Algumas telas faziam várias consultas simultâneas usando o mesmo indicador carregando.
- As operações de salvar chamavam listar depois, mas sem encadear corretamente o fluxo da requisição.
- Alguns endpoints específicos não usavam parâmetro anti-cache.
- O interceptor global estava tentando resolver atualização visual de forma genérica demais.
```

## Correção aplicada

Foram ajustadas as telas para usar carregamento inicial completo, atualização explícita da interface e recarregamento correto após operações.

Telas corrigidas:

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
Serviços e Peças da OS
Pagamentos
Garantias
Fila de Atendimento
```

## Alterações técnicas

```text
1. BaseApiService passou a aceitar respostas ApiResponse, PageResponse, arrays diretos e objetos diretos.
2. BaseApiService passou a expor opcoesSemCache para endpoints específicos.
3. Todas as consultas GET usam Cache-Control, Pragma, Expires e parâmetro _t.
4. Componentes com múltiplas consultas iniciais usam forkJoin.
5. Operações de salvar, editar, excluir, acionar e encerrar usam switchMap para recarregar a lista após a ação.
6. As listas são atribuídas com nova referência usando spread operator.
7. As telas usam ChangeDetectorRef para atualizar a interface imediatamente após resposta da API.
8. O interceptor global foi simplificado para tratar erros e não assumir responsabilidade visual de todos os componentes.
```

## Arquivos principais ajustados

```text
frontend/oficina-web/src/app/core/services/base-api.service.ts
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
frontend/oficina-web/src/app/core/services/modelo-api.service.ts
frontend/oficina-web/src/app/core/services/item-servico-api.service.ts
frontend/oficina-web/src/app/core/services/item-peca-api.service.ts
frontend/oficina-web/src/app/core/services/garantia-api.service.ts
frontend/oficina-web/src/app/core/services/pagamento-api.service.ts
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
frontend/oficina-web/src/app/pages/veiculos/veiculos.component.ts
frontend/oficina-web/src/app/pages/colaboradores/colaboradores.component.ts
frontend/oficina-web/src/app/pages/marcas-modelos/marcas-modelos.component.ts
frontend/oficina-web/src/app/pages/servicos/servicos.component.ts
frontend/oficina-web/src/app/pages/empresas-terceirizadas/empresas-terceirizadas.component.ts
frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts
frontend/oficina-web/src/app/pages/garantias/garantias.component.ts
frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.ts
```

## Validação executada

Foi executada validação TypeScript com:

```bash
./node_modules/.bin/tsc --noEmit -p tsconfig.app.json
```

Resultado: sem erros de TypeScript.

Observação: o `ng build` não foi executado neste ambiente porque a versão local do Node.js é inferior à mínima exigida pelo Angular 22. No ambiente do usuário, onde o Angular já está rodando, deve-se executar normalmente:

```powershell
npm.cmd install
npm.cmd start
```

## Resultado esperado

Ao abrir qualquer tela operacional, os dados já devem aparecer automaticamente. O usuário não deve precisar clicar em “Listar todos” para forçar a tabela a exibir registros já existentes no banco.
