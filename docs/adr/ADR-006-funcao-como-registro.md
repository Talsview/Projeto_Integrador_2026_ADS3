# ADR-006 — Representar Mecânico e demais cargos como registros de Funcao

## Status

Aceita.

## Contexto

O MER e as regras de negócio do sistema da oficina mecânica indicam que um colaborador pode possuir uma ou mais funções. Também foi definido que papéis como Mecânico, Atendente, Secretária, Faxineiro, Estoquista e Gerente não devem ser criados como entidades independentes.

Criar uma entidade específica para Mecânico geraria duplicidade conceitual, dificultaria a manutenção e enfraqueceria a rastreabilidade entre colaborador, função e item de serviço.

## Decisão

Representar Mecânico, Atendente, Secretária, Faxineiro, Estoquista, Gerente e demais cargos como registros da tabela `funcao`.

O vínculo entre colaborador e função será feito pela entidade associativa `colaborador_funcao`, que possui dados próprios como `data_inicio`, `data_fim`, `ativo`, `data_hora_criacao` e `data_hora_atualizacao`.

## Consequências positivas

```text
1. Permite que um colaborador tenha uma ou mais funções.
2. Evita criar entidades desnecessárias como Mecanico.
3. Preserva histórico das funções exercidas.
4. Mantém o modelo fiel ao MER validado.
5. Facilita a ligação futura entre ItemServico e Colaborador responsável.
```

## Consequências negativas

```text
1. A tela de colaborador precisa permitir selecionar uma ou mais funções.
2. As validações precisam garantir que todo colaborador tenha pelo menos uma função ativa.
```
