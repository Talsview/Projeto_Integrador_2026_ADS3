# ADR-034 — Menu superior organizado por categorias no Angular

## Status

Aceita.

## Contexto

A versão anterior do frontend exibia muitas opções diretamente na barra superior, gerando excesso visual e rolagem horizontal. Embora o menu superior fosse mais adequado do que a sidebar para a preferência do projeto, ainda era necessário organizar as opções em grupos.

O sistema possui módulos de naturezas diferentes, como cadastros, operação de ordens de serviço e gestão. Dessa forma, exibir todos os links no mesmo nível reduzia a clareza visual.

## Decisão

Foi adotado um menu superior por categorias, exibindo inicialmente apenas:

```text
Início
Operação
Cadastros
Gestão
```

Cada categoria apresenta suas opções em menu suspenso.

## Consequências positivas

```text
1. Redução de poluição visual.
2. Melhor agrupamento dos módulos por finalidade.
3. Navegação mais próxima de sistemas administrativos tradicionais.
4. Menor necessidade de rolagem horizontal.
5. Maior facilidade de uso para usuários da oficina.
```

## Consequências negativas

```text
1. O usuário precisa abrir a categoria para acessar algumas telas.
2. Em telas muito pequenas, o menu ainda pode exigir rolagem horizontal, mas em menor quantidade.
```

## Impacto técnico

A alteração foi feita apenas no componente global do Angular. Rotas, services, endpoints, controllers e banco de dados foram preservados.
