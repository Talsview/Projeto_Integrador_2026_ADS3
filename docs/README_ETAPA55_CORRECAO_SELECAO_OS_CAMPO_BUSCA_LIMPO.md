# Etapa 55 — Correção definitiva da seleção da OS com campo de busca limpo

## Objetivo

Esta etapa reforça a correção visual da tela **Ordens de Serviço**, garantindo que o cliente e o veículo selecionados apareçam somente no campo de seleção inferior, e não também no campo de busca.

## Problema identificado

Mesmo após a separação inicial entre campo de pesquisa e campo de seleção, em alguns fluxos de uso o texto pesquisado continuava visível no campo superior após a seleção na combobox. Isso mantinha a duplicidade visual:

```text
Campo de busca: Cliente selecionado
Combobox: Cliente selecionado
```

Esse comportamento deixava a tela menos clara, pois o campo de busca deve servir apenas para localizar registros, enquanto a combobox deve representar o valor efetivamente escolhido para a Ordem de Serviço.

## Regra de interface aplicada

A tela passa a seguir a regra abaixo de forma mais rígida:

```text
Campo de busca = pesquisar/localizar
Combobox = selecionar/registrar na OS
```

Assim, ao interagir com a combobox ou alterar a seleção, o campo de busca é limpo automaticamente.

## Alterações realizadas

- A seleção do cliente passou a utilizar `ngModelChange`, separando explicitamente o evento de escolha do cliente.
- A seleção do veículo passou a utilizar `ngModelChange`, separando explicitamente o evento de escolha do veículo.
- Foram criados métodos específicos para seleção de cliente e veículo na OS.
- Foram criados métodos específicos para limpar o campo de busca do cliente e do veículo.
- A combobox agora também limpa o campo de busca ao receber foco ou clique.
- Foi mantida a regra de que o veículo selecionado precisa pertencer ao cliente informado como proprietário atual.
- Não houve alteração no backend, banco de dados, fluxo de pagamento, garantia ou numeração da OS.

## Arquivos alterados

```text
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html
README.md
docs/README_ETAPA55_CORRECAO_SELECAO_OS_CAMPO_BUSCA_LIMPO.md
docs/adr/ADR-059-correcao-selecao-os-campo-busca-limpo.md
```

## Resultado esperado

Ao selecionar um cliente na combobox, o nome do cliente deve aparecer somente na combobox. O campo superior de busca deve ficar vazio.

Ao selecionar um veículo na combobox, a identificação do veículo deve aparecer somente na combobox. O campo superior de busca do veículo deve ficar vazio.

## Observação técnica

Esta etapa não substitui a Etapa 54. Ela continua a partir dela, apenas reforçando o comportamento da interface para eliminar o caso em que o texto ainda permanecia visível no campo de busca.
