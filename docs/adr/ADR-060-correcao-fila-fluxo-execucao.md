# ADR-060 — Correção da Fila de Atendimento e do fluxo de execução da OS

## Status

Aceita.

## Contexto

Foi identificado que a tela de Itens da OS encaminhava a Ordem de Serviço diretamente de **ORÇAMENTO** para **PAGAMENTO** após a montagem do orçamento. Esse comportamento pulava a etapa de **EXECUÇÃO**, prejudicando a Fila de Atendimento e contrariando o fluxo operacional definido para a oficina.

A Fila de Atendimento deve representar os serviços que já tiveram orçamento montado e estão aguardando execução antes do pagamento.

## Decisão

A partir desta etapa, o fluxo operacional aplicado será:

```text
ORÇAMENTO → EXECUÇÃO → PAGAMENTO → FINALIZADO
```

Foram tomadas as seguintes decisões:

1. A tela **Itens da OS** não envia mais a OS diretamente para pagamento.
2. Após a conclusão do orçamento, a OS passa para **EXECUÇÃO**.
3. A **Fila de Atendimento** exibe somente OS em **EXECUÇÃO**.
4. A própria fila permite encaminhar a OS para **PAGAMENTO** após a execução.
5. A tela **Pagamentos** continua responsável pela quitação financeira.
6. A finalização da OS continua automática após pagamento suficiente para quitar a ordem.
7. O cálculo recursivo passa a ser exibido em layout estruturado, e não mais como JSON bruto.

## Consequências

### Positivas

- O fluxo da OS fica coerente com o processo real da oficina.
- A Fila de Atendimento passa a ter função operacional clara.
- A tela de Pagamentos recebe apenas OS que já passaram pela execução.
- O sistema evita pular etapas indevidamente.
- O cálculo recursivo fica mais compreensível para apresentação acadêmica.

### Impactos técnicos

- Foi criada a operação de envio do orçamento para execução.
- O endpoint antigo foi mantido por compatibilidade, mas executa a nova regra correta.
- A fila foi ajustada para buscar status `EXECUCAO`.
- O frontend recebeu ação de envio para pagamento após execução.
- O tipo do retorno recursivo foi formalizado no frontend.

## Justificativa acadêmica

A decisão preserva as regras de negócio do sistema e reforça a rastreabilidade entre orçamento, execução, pagamento e finalização. Também melhora a evidenciação do uso de Estrutura de Dados I, pois a fila passa a representar uma fila real de atendimento operacional.
