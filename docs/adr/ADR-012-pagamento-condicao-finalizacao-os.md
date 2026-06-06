# ADR-012 — Pagamento como condição para finalização da Ordem de Serviço

## Status

Aceita.

## Contexto

O fluxo de negócio validado para a oficina mecânica define que a Ordem de Serviço segue as etapas:

```text
ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO
```

Além disso, as garantias de peças e serviços devem iniciar somente após a finalização da OS. Portanto, a finalização precisa representar que a execução foi concluída e que a etapa financeira foi resolvida.

## Decisão

Foi decidido que o módulo `Pagamento` será implementado como entidade própria vinculada à `OrdemServico`, permitindo que uma OS possua nenhum, um ou vários pagamentos.

Também foi decidido que a OS somente poderá avançar para `FINALIZADO` quando o somatório dos pagamentos com status `PAGO` for igual ou superior ao valor total da OS.

## Consequências positivas

```text
1. Melhora a rastreabilidade financeira da OS.
2. Permite pagamentos parciais.
3. Permite controlar pagamentos pendentes, cancelados ou estornados.
4. Impede encerramento indevido de OS sem quitação.
5. Mantém coerência com o início automático das garantias após finalização.
```

## Consequências negativas ou cuidados

```text
1. O usuário deve avançar a OS para PAGAMENTO antes de registrar pagamentos.
2. A alteração do valor total da OS deve ocorrer antes da finalização.
3. Pagamentos incorretos precisam ser corrigidos antes de finalizar a OS.
```

## Padrões e camadas envolvidas

```text
Model: PagamentoModel
DTO: PagamentoDTO e ResumoPagamentoOrdemServicoDTO
Repository: IPagamentoRepository
Validation: PagamentoValidation
Service: PagamentoService
Controller: PagamentoController
Response: ApiResponse e PageResponse
View: Angular consumindo endpoints REST
```
