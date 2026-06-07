# ADR-046 — Padronização das tabelas de cadastro com ações fixas

## Status

Aceita.

## Contexto

As telas de cadastro do sistema AV CAR AUTO CENTER passaram a conter mais dados por linha e mais ações após a implementação das funcionalidades de edição, inativação e validações por campo. Como consequência, algumas listagens passaram a esconder os botões de ação atrás da rolagem horizontal.

Esse comportamento prejudica a usabilidade, pois o usuário precisa arrastar a tabela para localizar ações essenciais, como editar ou inativar registros.

## Decisão

Foi decidido padronizar as tabelas das telas de cadastro com uma coluna de ações fixa à direita. A coluna de ações possui largura uniforme, botões alinhados e permanece visível mesmo quando a tabela apresentar rolagem horizontal.

Também foi decidido reorganizar as tabelas de Veículos e Colaboradores para agrupar informações relacionadas em células compostas, reduzindo a quantidade de colunas e melhorando a leitura.

## Consequências

### Positivas

- Melhor usabilidade nas telas de cadastro;
- Botões de ação sempre acessíveis;
- Aparência mais uniforme entre os módulos;
- Redução da necessidade de rolagem horizontal;
- Melhor adaptação em telas menores.

### Negativas

- A coluna de ações passa a ocupar largura fixa nas tabelas;
- Algumas informações foram agrupadas em células compostas para preservar legibilidade.

## Módulos impactados

- Clientes;
- Veículos;
- Colaboradores;
- Funções;
- Marcas e Modelos;
- Serviços;
- Empresas Terceirizadas;
- Peças e Fornecedores.
