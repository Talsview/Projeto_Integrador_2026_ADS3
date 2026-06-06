# Etapa 31 — Menu superior com categorias

## Objetivo

Esta etapa ajusta a navegação superior do frontend Angular para organizar as opções em categorias, evitando que todas as abas fiquem exibidas lado a lado na barra principal.

## Alteração aplicada

A navegação superior passou a exibir apenas os grupos principais:

```text
Início
Operação
Cadastros
Gestão
```

Ao posicionar o cursor ou focar em uma categoria, o sistema exibe as opções relacionadas em um menu suspenso.

## Categorias organizadas

### Operação

```text
Ordens de Serviço
Serviços e Peças da OS
Fila de Atendimento
Pagamentos
Garantias
```

### Cadastros

```text
Clientes
Veículos
Colaboradores
Funções
Marcas e Modelos
Serviços
Empresas Terceirizadas
Peças e Fornecedores
```

### Gestão

```text
Relatórios
Configurações
```

## Benefícios

```text
1. A barra superior ficou mais limpa.
2. As opções agora estão agrupadas por finalidade operacional.
3. O layout ficou mais próximo de um sistema administrativo tradicional.
4. Foi eliminada a rolagem horizontal excessiva na navegação principal.
5. O usuário acessa os módulos por categorias, facilitando o entendimento do sistema.
6. A comunicação com o backend, services e rotas Angular foi preservada.
```

## Arquivos alterados

```text
frontend/oficina-web/src/app/app.component.html
frontend/oficina-web/src/app/app.component.css
README.md
docs/README_ETAPA31_MENU_SUPERIOR_CATEGORIAS.md
docs/adr/ADR-034-menu-superior-categorias-angular.md
database/README_BANCO.md
```

## Observação

Essa alteração é exclusivamente visual e estrutural no menu. Nenhum endpoint, service ou regra de negócio do backend foi alterado.
