# ADR-064 — Reativação de cadastros, filtros de inativos e consulta de OS concluídas

## Status
Aceito.

## Contexto

O sistema já possuía inativação lógica em vários cadastros, preservando histórico. Entretanto, após a inativação, o usuário não possuía uma forma prática de localizar e reativar o cadastro pela interface.

Além disso, a consulta de Ordem de Serviço precisava de filtros mais objetivos para visualizar OS concluídas.

## Decisão

Foi decidido incluir:

- consulta de registros inativos em todas as telas que possuem inativação;
- botão de ativação para recuperar registros inativados;
- componente Angular reutilizável para exibir inativos com filtro por texto e data;
- endpoints REST `GET /inativos` e `PATCH /{id}/ativar`;
- filtro local por status e período na consulta de OS;
- botão rápido para exibir OS finalizadas.

## Consequências

### Positivas

- O usuário pode desfazer uma inativação sem recriar cadastro.
- A integridade histórica é preservada.
- O sistema evita duplicidade causada por recadastro desnecessário.
- A consulta de OS finalizadas fica mais objetiva.
- A solução reaproveita um componente visual comum para todos os cadastros.

### Negativas

- O backend passou a possuir mais endpoints por módulo.
- A interface recebeu mais elementos operacionais, exigindo organização visual.

## Justificativa

A decisão está alinhada à regra do projeto de preservar histórico, rastreabilidade e integridade. A reativação complementa a inativação lógica e torna o sistema mais seguro para uso real em uma oficina mecânica local.
