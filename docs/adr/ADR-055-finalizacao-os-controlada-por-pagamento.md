# ADR-055 — Finalização da OS controlada pelo módulo Pagamentos

## Status
Aceito

## Data
15/06/2026

## Contexto

A tela de Ordens de Serviço exibia a próxima etapa `PAGAMENTO → FINALIZADO`, permitindo interpretar que o usuário poderia finalizar uma OS diretamente pela área operacional. Isso gerava conflito com a regra financeira do sistema, pois a finalização deve depender da quitação da OS.

## Decisão

A tela de Ordens de Serviço passará a avançar o fluxo somente até **PAGAMENTO**. A transição para **FINALIZADO** será realizada automaticamente pelo módulo **Pagamentos** quando o valor pago for igual ou superior ao valor total da OS.

## Justificativa

A finalização de uma OS possui efeitos relevantes: encerramento do atendimento, início das garantias e liberação do documento final. Por isso, essa etapa deve estar vinculada à regra financeira de pagamento.

## Consequências positivas

- Evita finalização manual indevida.
- Preserva a regra de negócio da quitação financeira.
- Mantém o fluxo mais claro para o usuário.
- Centraliza a finalização no módulo responsável pelo pagamento.

## Consequências negativas / riscos

- O usuário precisa acessar a tela Pagamentos para concluir o fluxo.

## Mitigação

A tela de OS informa que a finalização ocorre automaticamente após a quitação na tela Pagamentos.
