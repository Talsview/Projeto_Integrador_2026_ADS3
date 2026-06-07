# ADR-043 — Ajuste de layout após validações por campo

## Status

Aceita.

## Contexto

Após a implementação das validações por campo e inclusão de novas ações nas tabelas, algumas telas passaram a apresentar componentes visualmente comprimidos. Isso ocorreu principalmente porque a mesma classe de grid era utilizada tanto no layout principal das páginas quanto em grids internos de formulários.

## Decisão

Foi decidido ajustar o CSS global do frontend Angular para diferenciar o grid principal das páginas dos grids internos dos cards. Também foram criadas regras específicas para tabelas e células de ações, evitando que botões dentro de tabelas herdem o mesmo comportamento flex usado em filtros e formulários.

## Consequências

- O sistema fica mais legível em telas grandes.
- Em telas médias, os cards são empilhados automaticamente.
- Tabelas com muitas colunas não quebram o layout.
- As ações das tabelas ficam alinhadas e fáceis de usar.
- O layout continua responsivo e compatível com o padrão visual da AV CAR AUTO CENTER.
