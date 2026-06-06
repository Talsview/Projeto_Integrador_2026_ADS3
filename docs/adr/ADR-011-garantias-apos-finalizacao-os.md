# ADR-011 — Início automático das garantias após finalização da Ordem de Serviço

## Status

Aprovada.

## Contexto

O modelo de negócio da oficina exige que toda peça aplicada em Ordem de Serviço possua garantia e que todo serviço executado também possua garantia. Entretanto, a garantia não deve começar no momento em que o item é cadastrado, pois a OS ainda pode estar em orçamento ou execução.

A regra validada determina que a garantia começa após a finalização da OS.

## Decisão

Foi decidido que:

```text
ItemPeca gera GarantiaPeca com status AGUARDANDO_FINALIZACAO_OS.
ItemServico gera GarantiaServico com status AGUARDANDO_FINALIZACAO_OS.
Ao alterar a OS para FINALIZADO, o sistema inicia automaticamente as garantias.
```

O início automático define:

```text
dataInicio = data de finalização da OS
dataFim = dataInicio + prazo da garantia
statusGarantia = VIGENTE
```

## Consequências positivas

```text
1. Mantém aderência às regras de negócio RN24, RN25, RN26 e RN27.
2. Evita início antecipado da garantia antes da entrega do veículo.
3. Preserva rastreabilidade entre OS, serviço, peça, fornecedor e garantia.
4. Facilita atendimento futuro ao cliente em caso de defeito.
5. Permite identificar quando a responsabilidade técnica pode ser do fornecedor.
```

## Consequências negativas ou cuidados

```text
1. A finalização da OS passa a ter efeito operacional importante.
2. A OS não deve ser finalizada incorretamente, pois isso inicia garantias.
3. Futuramente, permissões de usuário devem controlar quem pode finalizar OS.
```

## Regra acadêmica associada

```text
Toda peça possui garantia.
Toda peça usada em OS deve ter fornecedor identificado.
Toda garantia de peça começa após a finalização da OS.
Todo serviço possui garantia, com prazo variável conforme o tipo de serviço.
```
