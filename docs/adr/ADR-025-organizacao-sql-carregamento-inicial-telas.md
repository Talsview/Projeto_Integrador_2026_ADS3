# ADR-025 — Organização dos Scripts SQL e Carregamento Inicial Automático das Telas Angular

## Status

Aprovada.

## Contexto

Durante os testes do sistema, foram identificados dois problemas práticos:

```text
1. Os scripts SQL estavam acumulados na pasta database, dificultando diferenciar criação de tabelas, carga inicial e scripts de verificação.
2. Algumas telas Angular recebiam os dados da API, mas só atualizavam visualmente depois de uma nova interação do usuário.
```

Esse comportamento prejudicava a experiência de uso e poderia causar a impressão de que o backend estava lento ou que os dados não estavam sendo carregados.

## Decisão

Foi decidido organizar os scripts SQL em subpastas por finalidade:

```text
01_schema       → criação das tabelas, constraints, índices e chaves estrangeiras.
02_seed         → dados iniciais obrigatórios para funcionamento do sistema.
03_verificacoes → consultas auxiliares para validação do banco e do frontend.
04_completo     → script completo opcional para demonstração ou recriação rápida.
```

Também foi decidido ajustar o interceptor global do Angular para garantir atualização visual após respostas HTTP, usando `NgZone` e `ApplicationRef.tick()`.

## Consequências positivas

```text
1. O banco fica mais organizado para apresentação acadêmica.
2. Fica claro quais scripts devem ser executados para montar o banco do zero.
3. Os scripts de verificação não se misturam mais com scripts estruturais.
4. As telas Angular passam a carregar dados automaticamente ao abrir.
5. A comunicação entre frontend e backend fica mais previsível para testes.
```

## Consequências negativas

```text
1. O projeto passa a manter cópias organizadas dos scripts, além dos arquivos antigos preservados por compatibilidade.
2. O interceptor Angular passa a forçar atualização visual após cada resposta HTTP, o que é adequado para este projeto acadêmico, mas deve ser revisto em aplicações muito grandes.
```

## Justificativa acadêmica

A decisão melhora a clareza da implementação, facilita a avaliação do modelo físico e reforça o funcionamento correto da camada View em Angular consumindo a API REST do backend monolítico em camadas.
