# Etapa 43 — Padronização das listagens de cadastro e ações fixas

## Objetivo

A etapa corrige o comportamento visual das tabelas das telas de cadastro, garantindo que os botões de ação, como **Editar** e **Inativar**, fiquem sempre visíveis e alinhados na mesma direção.

## Problema identificado

Após a inclusão de novas colunas, validações e ações de edição/inativação, algumas tabelas passaram a exigir rolagem horizontal para acessar os botões. Isso prejudicava a usabilidade principalmente nas telas de Clientes, Veículos e Colaboradores.

## Solução aplicada

Foram padronizadas as tabelas de cadastro com:

- coluna de ações fixa à direita;
- botões sempre visíveis;
- largura uniforme da coluna de ações;
- quebra controlada de texto nas demais colunas;
- agrupamento de informações em células compostas;
- manutenção de rolagem horizontal somente para dados, sem esconder ações;
- comportamento uniforme entre as telas de cadastro.

## Telas ajustadas

- Clientes;
- Veículos;
- Colaboradores;
- Funções;
- Marcas e Modelos;
- Serviços;
- Empresas Terceirizadas;
- Peças e Fornecedores.

## Arquivos alterados

- `frontend/oficina-web/src/styles.css`
- `frontend/oficina-web/src/app/pages/clientes/clientes.component.html`
- `frontend/oficina-web/src/app/pages/veiculos/veiculos.component.html`
- `frontend/oficina-web/src/app/pages/colaboradores/colaboradores.component.html`
- `frontend/oficina-web/src/app/pages/funcoes/funcoes.component.html`
- `frontend/oficina-web/src/app/pages/marcas-modelos/marcas-modelos.component.html`
- `frontend/oficina-web/src/app/pages/servicos/servicos.component.html`
- `frontend/oficina-web/src/app/pages/empresas-terceirizadas/empresas-terceirizadas.component.html`
- `frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.html`

## Resultado esperado

As listagens das telas de cadastro passam a apresentar os botões **Editar** e **Inativar** sempre visíveis, mesmo quando houver muitas informações na tabela. Em telas menores, a tabela pode continuar tendo rolagem, mas a coluna de ações permanece fixa à direita.
