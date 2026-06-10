# Etapa 54 — Correção da busca da OS sem duplicidade visual

## Objetivo

Esta etapa corrige a tela **Ordens de Serviço** para separar corretamente a função do campo de pesquisa e do campo de seleção. Antes da correção, ao escolher um cliente ou veículo, o mesmo valor permanecia preenchido no campo de busca e também aparecia no `select`, causando duplicidade visual na tela.

## Problema identificado

Na tela de cadastro/edição de Ordem de Serviço, os campos superiores de busca estavam sendo usados como se fossem campos de exibição da seleção. Assim, após localizar e selecionar um cliente ou veículo, a interface apresentava o mesmo dado em dois locais:

```text
Campo de busca preenchido + select preenchido com o mesmo cliente/veículo
```

Esse comportamento deixava a tela menos intuitiva, pois o usuário poderia interpretar que existem dois campos obrigatórios para o mesmo dado.

## Regra de interface adotada

A partir desta etapa, a regra visual passa a ser:

```text
Campo de busca = apenas localizar registros
Campo de seleção = registrar a escolha da OS
```

Dessa forma, após selecionar o cliente ou o veículo, o texto da busca é limpo automaticamente e o valor escolhido permanece somente no campo de seleção inferior.

## Alterações realizadas

- O campo de pesquisa do cliente é limpo após a seleção do cliente.
- O campo de pesquisa do veículo é limpo após a seleção do veículo.
- Ao editar uma OS existente, os campos de pesquisa também permanecem vazios, evitando duplicidade visual.
- A regra de vínculo entre cliente e veículo foi preservada.
- Caso o cliente seja alterado e o veículo selecionado não pertença ao novo proprietário atual, o veículo continua sendo limpo automaticamente.
- Foi adicionada a ação de inativação da OS no componente TypeScript, mantendo compatibilidade com o botão **Inativar** já existente na interface.

## Regra de negócio preservada

A correção não altera o fluxo da Ordem de Serviço. A OS continua seguindo as regras do sistema:

```text
ORÇAMENTO → PAGAMENTO → FINALIZADO
```

A tela de Ordens de Serviço continua responsável pelo registro principal da OS, enquanto a montagem do orçamento e o envio para pagamento permanecem na tela **Itens da OS**.

## Arquivo alterado

```text
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
```

## Resultado esperado

Ao pesquisar e selecionar um cliente ou veículo, o usuário deverá visualizar o valor escolhido apenas no campo de seleção. O campo de busca ficará vazio e pronto para uma nova pesquisa, sem repetir a informação já selecionada.
