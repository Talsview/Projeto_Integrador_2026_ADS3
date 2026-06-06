# ADR-018 — Ampliação do Frontend Angular com Telas Operacionais

## Status

Aceita.

## Contexto

Após a integração inicial entre Angular e backend Spring Boot REST, verificou-se que o frontend ainda possuía poucas telas para apresentação do sistema completo da oficina mecânica.

Como o sistema possui muitos módulos no backend, a camada View precisava contemplar os principais fluxos de operação: cadastros básicos, veículos, ordens de serviço, itens da OS, pagamentos e garantias.

## Decisão

Foi decidido ampliar o frontend Angular, criando telas para os principais módulos já implementados no backend.

As telas foram organizadas em rotas próprias, consumindo a API por meio de serviços HTTP centralizados. Foi mantido o uso do proxy Angular para redirecionamento de `/api` para o backend local na porta `9081`.

## Consequências positivas

```text
A apresentação do sistema passa a demonstrar melhor o escopo completo.
O Angular deixa de ser apenas um painel inicial e passa a representar a camada View do projeto.
Os módulos centrais da oficina ficam acessíveis por menu lateral.
A comunicação com o backend continua organizada por services.
A separação por models TypeScript melhora a manutenção.
```

## Consequências negativas ou cuidados

```text
Algumas telas ainda precisam de validações visuais mais avançadas.
Os formulários dependem do backend e do banco físico estarem corretamente configurados.
A usabilidade pode ser refinada em etapa posterior.
```

## Justificativa acadêmica

Esta decisão reforça a arquitetura em camadas exigida pelo projeto integrador, mantendo o Angular como camada **View**, o Spring Boot como backend monolítico em camadas e o PostgreSQL como banco local.
