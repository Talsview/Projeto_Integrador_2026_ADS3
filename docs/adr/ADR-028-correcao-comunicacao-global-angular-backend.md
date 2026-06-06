# ADR-028 — Correção global da comunicação Angular/Backend

## Status

Aprovada.

## Contexto

Durante os testes do frontend Angular, observou-se que algumas telas não exibiam os dados do banco automaticamente ao serem abertas. Em alguns casos, os registros somente apareciam depois de digitar em um campo, clicar em outro botão ou executar nova ação de tela.

Esse comportamento prejudicava a usabilidade do sistema e dava a impressão de falha na comunicação entre Angular, backend Spring Boot e PostgreSQL.

## Decisão

Foi decidido corrigir o fluxo de comunicação de forma global, mantendo a regra:

```text
consulta no backend → recebimento da resposta → nova referência de lista → atualização explícita da tela
```

Também foi decidido remover do interceptor a responsabilidade de forçar atualização visual global, deixando os componentes controlarem a própria atualização, pois cada tela possui dependências e listas específicas.

## Consequências

### Positivas

```text
- As telas passam a carregar dados automaticamente ao abrir.
- Após salvar, editar, excluir ou acionar registros, a tabela é recarregada imediatamente.
- A comunicação com o backend fica mais previsível.
- Os componentes ficam mais explícitos quanto ao estado de carregamento e atualização.
- O BaseApiService fica mais tolerante a diferentes formatos de resposta.
```

### Negativas

```text
- Alguns componentes ficaram com código um pouco mais explícito.
- O uso de ChangeDetectorRef aumenta a responsabilidade da camada de apresentação.
```

## Justificativa acadêmica

A decisão reforça a camada View da arquitetura em camadas, garantindo que o Angular represente corretamente o estado persistido no banco de dados. Essa correção também favorece a validação do sistema funcionando, conforme exigência do Projeto Integrador.
