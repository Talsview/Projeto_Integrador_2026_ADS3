# ADR-031 — Ajuste visual conforme modelo de referência

## Status

Aceita.

## Contexto

Após a estabilização da comunicação entre Angular, backend Spring Boot e PostgreSQL, foi identificada a necessidade de aproximar a interface visual dos modelos de referência enviados, com aparência mais profissional e adequada a um sistema real de gestão de oficina mecânica.

## Decisão

Foi decidido reformular o layout global do Angular com:

- sidebar escura fixa;
- topbar com busca e usuário;
- cards operacionais;
- tela inicial com visão geral da oficina;
- tabelas e formulários padronizados;
- menu organizado por operação, cadastros e gestão;
- manutenção dos endpoints e services já existentes.

## Consequências

- O sistema passa a ter aparência mais próxima de um software administrativo profissional.
- A interface continua consumindo dados reais do backend.
- A camada visual fica mais preparada para apresentação acadêmica e uso operacional.
- A manutenção continua preservando a arquitetura em camadas e a separação entre frontend e backend.
