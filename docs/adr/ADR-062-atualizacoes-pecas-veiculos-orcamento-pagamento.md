# ADR-062 — Atualizações em peças, fornecedores, veículos, orçamento e pagamentos

## Status

Aprovada.

## Contexto

Durante os testes operacionais do sistema, foram identificadas necessidades de melhoria em quatro pontos: rastreabilidade de peças e fornecedores, usabilidade no cadastro de veículos, cálculo automático de valores no orçamento da OS e simplificação da tela de pagamentos.

A regra de negócio do projeto estabelece que toda peça aplicada em OS deve possuir fornecedor identificado. Antes desta decisão, o fornecedor era escolhido manualmente no item de peça, aumentando risco de erro operacional.

Também foi observado que a quilometragem do veículo iniciava com zero, podendo induzir ao cadastro incorreto, e que a tela de pagamentos possuía botão redundante de marcação de pagamento.

## Decisão

Foi decidido:

1. Relacionar `Peca` com um `Fornecedor` padrão.
2. Adicionar valor unitário padrão à peça.
3. Preencher automaticamente fornecedor e valor quando a peça for selecionada na OS.
4. Preencher automaticamente valor base quando o serviço for selecionado na OS.
5. Recalcular total de serviço e peça ao alterar quantidade.
6. Criar busca de cliente no cadastro de veículo antes da seleção do proprietário atual.
7. Deixar a quilometragem inicial em branco no formulário de veículo.
8. Remover o botão `Marcar pago` da tela de pagamentos.

## Consequências positivas

- Reduz erro manual na escolha do fornecedor.
- Aumenta a rastreabilidade entre peça, fornecedor e OS.
- Melhora o fluxo de orçamento.
- Evita orçamento com valores esquecidos.
- Melhora a usabilidade no cadastro de veículo.
- Evita o cadastro acidental de quilometragem zero.
- Simplifica a tela de pagamentos.

## Consequências técnicas

Foi necessário alterar o modelo físico da tabela `peca`, adicionando:

```text
id_fornecedor_padrao
valor_unitario_padrao
```

Como o Hibernate está configurado com `ddl-auto=validate`, foi criado script incremental:

```text
database/01_schema/04_alter_peca_fornecedor_valor.sql
```

## Justificativa acadêmica

A decisão fortalece os requisitos de rastreabilidade, integridade e organização do sistema. A peça continua sendo aplicada por meio de `ItemPeca`, mas agora o cadastro da peça já possui o fornecedor de referência, evitando inconsistências no orçamento e facilitando a defesa do modelo perante a regra de negócio de garantia da peça.
