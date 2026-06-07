# ADR-048 — Atendimento operacional de garantia

**Status:** Aceito  
**Data:** 15/06/2026  
**Autores:** Davi Martins Alexandre e equipe

## 1. Contexto

O sistema já permitia consultar, acionar e encerrar garantias de peças e serviços. Entretanto, o acionamento apenas alterava o status da garantia, sem registrar o motivo, o defeito relatado, o responsável pela análise ou a solução aplicada no encerramento.

Como a oficina permanece responsável perante o cliente, mesmo quando a responsabilidade técnica pode ser do fornecedor, a garantia precisa registrar informações suficientes para rastreabilidade operacional.

## 2. Decisão

Foi decidido implementar um fluxo operacional de atendimento de garantia, com modal de acionamento e encerramento no Angular e novos campos persistidos no PostgreSQL.

## 3. Justificativa

A decisão melhora a rastreabilidade entre cliente, OS, item de serviço, peça, fornecedor e garantia, além de tornar o sistema mais próximo do funcionamento real de uma oficina mecânica.

## 4. Consequências positivas

- Acionamento de garantia passa a registrar motivo e defeito relatado.
- Encerramento passa a registrar solução aplicada.
- Peças mantêm responsabilidade vinculada a fornecedor, oficina ou ambos.
- A tela deixa claro o próximo passo operacional.
- O backend impede transições inválidas de status.

## 5. Consequências negativas / riscos

- O banco existente precisa receber novas colunas.
- A tela de garantia fica mais completa e exige mais informações do usuário.

## 6. Mitigações

Foi criado script incremental `02_alter_garantia_atendimento.sql` para bancos já existentes. O script completo também foi atualizado.

## 7. Alternativas consideradas

Manter apenas alteração de status foi rejeitado por não registrar histórico suficiente do atendimento.

## 8. Follow-up

Uma evolução futura pode criar uma entidade própria de histórico de acionamentos, permitindo múltiplos acionamentos para a mesma garantia.
