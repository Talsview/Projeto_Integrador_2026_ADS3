# ADR-056 — Pagamento somente pelo módulo Pagamentos

## Status
Aceito

## Data
15/06/2026

## Contexto
O sistema possui o fluxo de Ordem de Serviço ORCAMENTO -> EXECUCAO -> PAGAMENTO -> FINALIZADO. A tela de Ordens de Serviço deve controlar apenas o avanço operacional até PAGAMENTO. O pagamento é uma operação financeira e deve ocorrer somente no módulo Pagamentos.

## Decisão
Foi decidido que:

- a tela Ordens de Serviço avança somente até PAGAMENTO;
- a tela Ordens de Serviço não registra pagamento;
- a tela Ordens de Serviço não finaliza OS;
- o backend só aceita cadastro de pagamento se a OS já estiver em PAGAMENTO;
- a finalização da OS continua automática após quitação no módulo Pagamentos.

## Consequências positivas
- Separa fluxo operacional e financeiro.
- Evita quitação indevida de OS em orçamento ou execução.
- Mantém rastreabilidade entre status, pagamento e finalização.
- Preserva a regra de negócio de que a OS só finaliza após pagamento suficiente.

## Consequências negativas / riscos
- O usuário precisa entender que pagamento pertence a outra tela.

## Mitigação
A tela de Ordens de Serviço exibe mensagem clara quando a OS está em PAGAMENTO, orientando o uso do módulo Pagamentos.
