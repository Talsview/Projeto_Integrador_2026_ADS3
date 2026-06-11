# Etapa 61 — Reativação de cadastros, consulta de inativos e filtros de OS concluídas

## Objetivo

Esta etapa melhora a manutenção operacional do sistema, permitindo que registros inativados possam ser consultados e ativados novamente, sem perda de histórico e sem necessidade de recadastro.

## Alterações principais

### 1. Reativação de cadastros

Foram adicionados endpoints e botões de ativação para os módulos que possuem inativação lógica:

- Clientes;
- Colaboradores;
- Funções;
- Veículos;
- Marcas;
- Modelos;
- Serviços;
- Empresas terceirizadas;
- Fornecedores;
- Peças;
- Ordens de Serviço;
- Pagamentos.

A regra adotada foi preservar a exclusão lógica. O registro não é apagado do banco; ele apenas muda o campo `ativo` para `false`. Quando reativado, o mesmo cadastro volta a ter `ativo = true`.

## 2. Consulta de registros inativos

As telas que possuem botão **Inativar** passaram a ter também a opção **Ver inativos**. Essa opção abre uma tela auxiliar com filtros por:

- texto;
- nome;
- documento;
- placa;
- código;
- número de OS;
- status;
- data inicial;
- data final.

A data usada no filtro considera a última alteração do registro, permitindo localizar cadastros inativados ou alterados em determinado período.

## 3. Consulta de OS concluídas

Na tela **Ordens de Serviço**, a área **Consulta de OS** recebeu filtros adicionais:

- status da OS;
- botão rápido **Ver concluídas**;
- data inicial de abertura;
- data final de abertura.

Com isso, é possível localizar rapidamente OS com status `FINALIZADO`, além de filtrar por período.

## 4. Backend

Foi ampliado o repositório genérico com consultas para registros inativos:

- `findByIdAndAtivoFalse`;
- `findAllByAtivoFalse`.

Os controllers passaram a expor:

```text
GET   /inativos
PATCH /{id}/ativar
```

## 5. Frontend

Foi criado o componente reutilizável:

```text
frontend/oficina-web/src/app/shared/components/inativos-panel/inativos-panel.component.ts
```

Esse componente centraliza a exibição de registros inativos, evitando duplicação de tela e mantendo o mesmo padrão visual em todos os módulos.

## 6. Justificativa acadêmica

A atualização reforça rastreabilidade e integridade dos dados. O sistema continua evitando exclusão física, mas agora permite recuperar registros inativados, o que melhora a usabilidade e reduz risco de recadastro duplicado.
