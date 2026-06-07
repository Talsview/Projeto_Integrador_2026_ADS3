# ADR-048 — Filtro de Ordens de Serviço por Status nas Telas Operacionais

## Status
Aceito

## Data
14/06/2026

## Contexto
O sistema AV CAR AUTO CENTER possui fluxo oficial de Ordem de Serviço: Orçamento, Execução, Pagamento e Finalizado. Antes desta decisão, algumas telas exibiam ordens em diferentes status, permitindo seleção de OS que não pertenciam à etapa correta da operação.

## Decisão
Foi decidido filtrar as Ordens de Serviço conforme a finalidade de cada tela:

- Serviços e Peças da OS: somente ORCAMENTO;
- Fila de Atendimento: somente EXECUCAO;
- Pagamentos: somente PAGAMENTO;
- Garantias: filtro por OS somente FINALIZADO.

Também foi decidido que a Data fim do item de serviço pode ser futura quando representar previsão de conclusão, desde que não seja anterior à Data início.

## Justificativa
A decisão reforça a regra de negócio da oficina e reduz erros operacionais. Cada tela passa a trabalhar com a etapa correta do fluxo, evitando alterações indevidas, registros fora de contexto e confusão para o usuário.

## Consequências positivas
- Melhora da usabilidade;
- Redução de erros de seleção;
- Maior aderência ao fluxo de OS;
- Backend mais seguro contra uso indevido via API;
- Interface mais coerente com a rotina real da oficina.

## Consequências negativas / riscos
- O usuário precisa avançar corretamente o status da OS antes de acessar a funcionalidade da próxima etapa;
- Caso uma OS esteja no status errado, ela não aparecerá na tela esperada.

## Mitigações
- Exibir mensagens claras nas telas;
- Manter a tela Ordens de Serviço como local central para avançar o fluxo;
- Documentar o fluxo operacional no README e no DAS.

## Alternativas consideradas
- Exibir todas as OS em todas as telas: descartado por gerar confusão e permitir operações fora da etapa correta.
- Validar apenas no backend: descartado porque prejudica a experiência do usuário.

## Follow-up
Revisar futuramente se haverá necessidade de tela separada de histórico para consultar pagamentos e garantias de OS finalizadas sem permitir alteração.
