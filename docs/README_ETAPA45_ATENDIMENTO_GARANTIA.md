# Etapa 45 — Atendimento operacional de garantia

Esta etapa transforma o acionamento de garantia em um fluxo operacional completo, alinhado às regras de negócio da oficina mecânica AV CAR AUTO CENTER.

## Objetivo

Antes desta etapa, o botão **Acionar** apenas alterava o status da garantia para `ACIONADA`. Agora o sistema registra as informações do atendimento, permitindo acompanhar o motivo do acionamento, defeito relatado, responsável pela análise, responsabilidade inicial, solução aplicada e encerramento.

## Fluxo implementado

```text
VIGENTE
  -> Acionar garantia
  -> ACIONADA
  -> Encerrar garantia
  -> ENCERRADA
```

## Backend

Foram adicionados campos em `garantia_peca` e `garantia_servico`:

```text
data_acionamento
motivo_acionamento
descricao_defeito
responsavel_analise
data_encerramento
solucao_aplicada
custo_assumido_por
atendimento_realizado
```

A garantia de peça mantém ainda o campo `responsabilidade`, que pode ser `OFICINA`, `FORNECEDOR` ou `AMBOS`.

## Regras de validação

- Somente garantia `VIGENTE` pode ser acionada.
- Garantia expirada, encerrada ou aguardando finalização da OS não pode ser acionada.
- Somente garantia `ACIONADA` pode ser encerrada.
- Acionamento exige data, motivo, defeito relatado e responsável pela análise.
- Peça exige responsabilidade inicial no acionamento.
- Encerramento exige data, solução aplicada e indicação de quem assumiu o custo.
- Datas de acionamento e encerramento não podem ser futuras.
- Data de acionamento não pode ser anterior ao início da garantia.
- Data de encerramento não pode ser anterior à data de acionamento.

## Frontend Angular

A tela `Garantias` agora possui modal para:

- registrar acionamento;
- registrar encerramento;
- exibir data de acionamento e responsável pela análise na listagem;
- bloquear ações incompatíveis com o status atual.

## Banco de dados

Para bancos já criados antes desta etapa, execute:

```text
database/01_schema/02_alter_garantia_atendimento.sql
```

Para bancos novos, o script completo já contém as novas colunas:

```text
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
```
