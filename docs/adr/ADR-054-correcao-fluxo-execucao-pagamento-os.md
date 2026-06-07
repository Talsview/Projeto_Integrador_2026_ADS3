# ADR-054 — Correção do fluxo entre Execução e Pagamento da Ordem de Serviço

## Status
Aceito

## Data
15/06/2026

## Contexto
O sistema possui fluxo obrigatório de Ordem de Serviço: Orçamento, Execução, Pagamento e Finalizado. Foi identificado que a interface permitia escolher manualmente o novo status, criando risco de conflito quando uma OS em execução tentava ser confirmada novamente como execução.

## Decisão
A alteração de status na interface passa a ser tratada como avanço sequencial automático, calculando a próxima etapa com base no status atual da OS.

## Justificativa
Essa decisão reduz erro operacional, evita seleção indevida de status e representa melhor o funcionamento real da oficina, em que a OS avança por etapas definidas.

## Consequências positivas
- Evita conflito ao avançar de Execução para Pagamento.
- Mantém a regra de negócio do backend intacta.
- Deixa a interface mais clara para o usuário.
- Preserva rastreabilidade do histórico de status.

## Riscos e mitigação
O usuário deixa de escolher livremente o status. Isso é intencional, pois a regra do domínio exige fluxo sequencial sem retorno e sem salto de etapas.

## Alternativas consideradas
Manter o select manual e apenas trocar o valor padrão. A alternativa foi rejeitada porque ainda permitiria erro humano.
