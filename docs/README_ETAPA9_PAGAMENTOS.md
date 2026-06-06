# Etapa 9 — Pagamento

## 1. Objetivo da etapa

Esta etapa implementa o módulo de `Pagamento`, responsável por registrar os valores pagos pelo cliente em uma Ordem de Serviço da oficina mecânica AV CAR AUTO CENTER.

A implementação respeita o modelo conceitual validado, no qual:

```text
OrdemServico (1,1) -------- gera -------- (0,n) Pagamento
```

Leitura acadêmica:

```text
Cada Pagamento pertence a uma única OrdemServico.
Uma OrdemServico pode gerar nenhum, um ou vários Pagamentos.
```

## 2. Entidades e classes implementadas

Foram criados os seguintes componentes:

```text
business/pagamento/model/PagamentoModel.java
business/pagamento/dto/PagamentoDTO.java
business/pagamento/dto/ResumoPagamentoOrdemServicoDTO.java
business/pagamento/enums/FormaPagamento.java
business/pagamento/enums/StatusPagamento.java
business/pagamento/mapper/PagamentoMapper.java
business/pagamento/repository/IPagamentoRepository.java
business/pagamento/validation/PagamentoValidation.java
business/pagamento/service/PagamentoService.java
business/pagamento/controller/PagamentoController.java
```

## 3. Regras de negócio implementadas

```text
RN28 — OS pode gerar nenhum, um ou vários pagamentos.
RN29 — Priorizar rastreabilidade entre cliente, veículo, OS, serviço, peça, fornecedor, garantia e pagamento.
```

Além dessas regras consolidadas, foram aplicadas regras financeiras específicas:

```text
RF-PAG-01 — Pagamento deve estar vinculado a uma Ordem de Serviço ativa.
RF-PAG-02 — Pagamento deve possuir forma de pagamento.
RF-PAG-03 — Pagamento deve possuir valor maior que zero.
RF-PAG-04 — Pagamento só pode ser registrado quando a OS está no status PAGAMENTO.
RF-PAG-05 — Apenas pagamentos com status PAGO compõem o total pago da OS.
RF-PAG-06 — Pagamentos PENDENTE, CANCELADO e ESTORNADO não quitam a OS.
RF-PAG-07 — A OS só pode avançar para FINALIZADO quando o total pago for igual ou superior ao valor total da OS.
RF-PAG-08 — OS finalizada não permite inclusão, alteração ou inativação de pagamentos.
```

## 4. Formas de pagamento

As formas de pagamento foram padronizadas em enum:

```text
DINHEIRO
PIX
CARTAO_DEBITO
CARTAO_CREDITO
TRANSFERENCIA
BOLETO
OUTRO
```

## 5. Status do pagamento

Os status financeiros foram padronizados em enum:

```text
PENDENTE
PAGO
CANCELADO
ESTORNADO
```

Apenas `PAGO` reduz o saldo pendente da Ordem de Serviço.

## 6. Integração com o fluxo da Ordem de Serviço

A finalização da OS agora passa a depender da situação financeira.

Fluxo operacional esperado:

```text
1. OS é criada como ORCAMENTO.
2. OS avança para EXECUCAO após possuir ItemServico.
3. OS avança para PAGAMENTO após a execução.
4. Pagamentos são registrados.
5. Sistema calcula o total pago.
6. OS só avança para FINALIZADO se estiver quitada.
7. Ao finalizar, as garantias de peças e serviços são iniciadas automaticamente.
```

Essa integração mantém coerência entre execução, pagamento, finalização e início das garantias.

## 7. Endpoints REST criados

```text
POST   /api/pagamentos
PUT    /api/pagamentos/{id}
PATCH  /api/pagamentos/{id}/status?statusPagamento=PAGO
GET    /api/pagamentos/{id}
GET    /api/pagamentos
GET    /api/pagamentos/ordem-servico/{idOrdemServico}
GET    /api/pagamentos/ordem-servico/{idOrdemServico}/pesquisar?termo=valor
GET    /api/pagamentos/ordem-servico/{idOrdemServico}/resumo
DELETE /api/pagamentos/{id}
```

## 8. Exemplo de JSON para cadastro

```json
{
  "idOrdemServico": 1,
  "formaPagamento": "PIX",
  "valorPago": 250.00,
  "statusPagamento": "PAGO",
  "observacao": "Pagamento realizado via PIX no balcão da oficina."
}
```

## 9. Resumo financeiro da OS

O endpoint abaixo retorna a situação financeira consolidada da Ordem de Serviço:

```text
GET /api/pagamentos/ordem-servico/{idOrdemServico}/resumo
```

Retorno esperado:

```json
{
  "idOrdemServico": 1,
  "numeroOs": "OS-001",
  "valorTotalOrdemServico": 600.00,
  "valorPago": 600.00,
  "valorPendente": 0.00,
  "quitada": true
}
```

## 10. Banco de dados

A tabela física integrada é:

```text
pagamento
```

Campos principais:

```text
id_pagamento
id_ordem_servico
forma_pagamento
valor_pago
data_pagamento
status_pagamento
observacao
ativo
data_hora_criacao
data_hora_atualizacao
```

A tabela possui chave estrangeira para `ordem_servico`, restrição de valor positivo, restrição para status de pagamento e restrição para as formas de pagamento aceitas.

## 11. Observação acadêmica

A etapa reforça a rastreabilidade financeira da oficina, permitindo acompanhar quais pagamentos foram feitos, qual forma foi utilizada, qual valor foi efetivamente quitado e se a Ordem de Serviço pode ou não ser finalizada.

Com isso, a finalização da OS deixa de ser apenas uma mudança de status e passa a depender de uma regra de integridade operacional: a OS deve estar financeiramente quitada para ser encerrada e iniciar as garantias.
