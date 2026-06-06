# Etapa 7 — Ordem de Serviço, Status, Histórico de Status e ItemServico

## 1. Objetivo da etapa

Esta etapa implementa o núcleo operacional do sistema da oficina mecânica AV CAR AUTO CENTER. A Ordem de Serviço passa a integrar Cliente, Veículo, Serviço, Colaborador responsável, peças aplicadas e histórico de status.

A implementação respeita o modelo conceitual validado para o projeto:

```text
Cliente (1,1) -------- solicita -------- (0,n) OrdemServico
Veiculo (1,1) -------- recebe -------- (0,n) OrdemServico
OrdemServico (1,1) -------- possui -------- (1,n) HistoricoStatusOrdem
HistoricoStatusOrdem (0,n) -------- compõe -------- (1,1) StatusOrdemServico
OrdemServico (1,1) -------- possui -------- (1,n) ItemServico
Servico (1,1) -------- é utilizado em -------- (0,n) ItemServico
Colaborador (1,1) -------- é responsável por -------- (0,n) ItemServico
ItemServico (1,1) -------- gera -------- (0,1) ExecucaoServicoTerceirizado
```

## 2. Entidades implementadas

```text
OrdemServico
StatusOrdemServico
HistoricoStatusOrdem
ItemServico
ExecucaoServicoTerceirizado
```

## 3. Regras de negócio implementadas

```text
RN06 — Cliente pode solicitar várias OS.
RN07 — Toda OS pertence a um único Cliente.
RN08 — Veículo pode receber várias OS.
RN09 — Toda OS pertence a um único Veiculo.
RN10 — Toda OS deve possuir histórico de status.
RN11 — OS segue: Orçamento, Execução, Pagamento e Finalizado.
RN12 — Toda OS possui pelo menos um ItemServico.
RN13 — Todo ItemServico possui Colaborador responsável.
RN16 — Todo Servico é Interno ou Terceirizado.
RN17 — Serviço terceirizado gera ExecucaoServicoTerceirizado.
RN19 — A oficina permanece responsável pelo serviço terceirizado.
RN20 — OS pode ou não utilizar peças.
RN29 — Priorizar rastreabilidade entre cliente, veículo, OS, serviço, peça, fornecedor e garantia.
```

## 4. Fluxo da Ordem de Serviço

O fluxo implementado segue a ordem obrigatória:

```text
ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO
```

A alteração de status é controlada pelo endpoint:

```text
PATCH /api/ordens-servico/{id}/status
```

Exemplo de corpo da requisição:

```json
{
  "novoStatus": "EXECUCAO",
  "observacao": "Orçamento aprovado pelo cliente."
}
```

O sistema impede que a OS pule etapas ou retorne para status anterior.

## 5. Pré-condições de fluxo

Antes de avançar para `EXECUCAO`, a OS deve possuir pelo menos um `ItemServico`, garantindo aderência à regra de que toda Ordem de Serviço possui pelo menos um serviço executado.

Quando a OS entra em `FINALIZADO`, o sistema registra a `dataFinalizacao`. A criação e ativação das garantias será executada na etapa específica de Garantias.

## 6. ItemServico

O `ItemServico` representa o serviço específico executado dentro de uma OS. Ele registra:

```text
Ordem de Serviço
Serviço cadastrado
Colaborador responsável
Descrição da execução
Quantidade
Valor unitário
Valor total
Data de início
Data de fim
```

Quando o serviço selecionado é terceirizado, o sistema exige a empresa terceirizada e cria o registro de `ExecucaoServicoTerceirizado`.

## 7. Integração com ItemPeca

O módulo de peças foi ajustado para validar a existência da Ordem de Serviço ao cadastrar, atualizar ou inativar um `ItemPeca`. Além disso, a cada alteração em peças ou serviços, o valor total da OS é recalculado.

O total da OS considera:

```text
Total da OS = soma dos ItemServico ativos + soma dos ItemPeca ativos
```

## 8. Endpoints implementados

### Ordem de Serviço

```text
POST   /api/ordens-servico
PUT    /api/ordens-servico/{id}
PATCH  /api/ordens-servico/{id}/status
GET    /api/ordens-servico/{id}
GET    /api/ordens-servico
GET    /api/ordens-servico/pesquisar?termo=valor
DELETE /api/ordens-servico/{id}
```

### ItemServico

```text
POST   /api/itens-servico
PUT    /api/itens-servico/{id}
GET    /api/itens-servico/{id}
GET    /api/itens-servico
GET    /api/itens-servico/ordem-servico/{idOrdemServico}
GET    /api/itens-servico/ordem-servico/{idOrdemServico}/pesquisar?termo=valor
DELETE /api/itens-servico/{id}
```

### StatusOrdemServico

```text
GET    /api/status-ordem-servico/{id}
GET    /api/status-ordem-servico
GET    /api/status-ordem-servico/pesquisar?termo=valor
```

## 9. Observação acadêmica

Esta etapa consolida a rastreabilidade principal do sistema. A partir dela, é possível acompanhar qual cliente solicitou a OS, qual veículo recebeu atendimento, quais serviços foram executados, qual colaborador foi responsável, quais peças foram aplicadas e qual é o status atual da ordem.
