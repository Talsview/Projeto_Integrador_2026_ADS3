# ADR-045 — Correção do Menu Superior e Reformulação da Listagem de Clientes

## Status

Aceita.

## Contexto

Após a ampliação do layout e a inclusão de novas ações por linha, foi identificado que o menu superior de categorias podia ficar preso pelo comportamento de rolagem horizontal da barra de navegação. Também foi observado que a listagem de clientes exigia rolagem horizontal para que o usuário encontrasse os botões Editar e Inativar.

## Decisão

Foi decidido alterar o menu superior para abrir as categorias por clique e ajustar o CSS para impedir que os dropdowns fiquem limitados pela barra de navegação. Também foi decidido reformular a tabela de clientes, agrupando dados relacionados em menos colunas e mantendo as ações sempre acessíveis.

## Consequências

- A navegação superior fica mais previsível e acessível.
- O usuário não precisa arrastar a tabela de clientes em telas grandes para encontrar ações.
- A tela de clientes passa a usar uma estrutura de consulta mais adequada para sistema administrativo.
- Em telas pequenas, a listagem ainda permite rolagem horizontal, mas mantém a coluna de ações fixa.
