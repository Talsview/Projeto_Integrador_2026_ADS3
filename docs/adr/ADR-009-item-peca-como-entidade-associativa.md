# ADR-009 — Uso de ItemPeca como entidade associativa

## Status

Aprovada.

## Contexto

No domínio da oficina mecânica, uma peça cadastrada não representa, por si só, uma peça aplicada em uma Ordem de Serviço. Ao aplicar uma peça em uma OS, é necessário registrar informações próprias daquele uso, como quantidade, valor unitário, valor total, fornecedor responsável e observações.

Além disso, a rastreabilidade entre OS, peça, fornecedor e garantia é uma regra central do projeto.

## Decisão

Foi decidido representar a peça aplicada por meio da entidade associativa `ItemPeca`.

```text
OrdemServico (1,1) -------- utiliza -------- (0,n) ItemPeca.
Peca (1,1) -------- é aplicada em -------- (0,n) ItemPeca.
Fornecedor (1,1) -------- fornece -------- (0,n) ItemPeca.
ItemPeca (1,1) -------- gera -------- (1,1) GarantiaPeca.
```

## Consequências positivas

```text
1. Preserva a rastreabilidade da peça aplicada.
2. Permite identificar o fornecedor responsável por cada peça usada.
3. Permite registrar quantidade, valor unitário e valor total da aplicação.
4. Prepara a geração de GarantiaPeca após a finalização da OS.
5. Evita relacionamento direto inadequado entre ItemServico e ItemPeca.
```

## Consequências negativas

```text
1. A implementação fica mais detalhada.
2. O módulo de ItemPeca depende da futura integração completa com OrdemServico.
```

## Justificativa acadêmica

A decisão está de acordo com a regra de transformar relacionamentos com dados próprios em entidades associativas, preservando histórico, integridade e rastreabilidade.
