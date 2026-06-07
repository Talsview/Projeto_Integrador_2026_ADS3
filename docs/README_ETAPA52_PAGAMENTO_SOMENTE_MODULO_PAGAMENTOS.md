# Etapa 52 — Pagamento somente pelo módulo Pagamentos

## Decisão
A tela de Ordens de Serviço não realiza pagamento e não finaliza Ordem de Serviço.

## Regra implementada
O fluxo operacional ficou separado por responsabilidade:

- Ordens de Serviço: ORCAMENTO -> EXECUCAO -> PAGAMENTO.
- Pagamentos: registra valores recebidos quando a OS já está em PAGAMENTO.
- Backend: finaliza automaticamente a OS somente após quitação financeira.

## Correções realizadas
- A tela de OS em status PAGAMENTO exibe apenas orientação operacional.
- O botão Avançar etapa não aparece para OS em PAGAMENTO.
- O backend não avança automaticamente ORCAMENTO/EXECUCAO para PAGAMENTO ao tentar cadastrar pagamento.
- O cadastro de pagamento agora exige que a OS já esteja na etapa PAGAMENTO.

## Justificativa
A regra evita que uma OS ainda em orçamento ou execução seja quitada indevidamente. A etapa financeira deve ocorrer apenas após o avanço operacional correto da OS.
