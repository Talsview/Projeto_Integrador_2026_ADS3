# ADR-052 — Correção de Type Guard para Garantias em Relatórios

## Status
Aceito

## Data
15/06/2026

## Contexto
A tela de Relatórios exporta uma planilha contendo garantias de peças e garantias de serviços. Como os dois objetos possuem propriedades diferentes, o TypeScript exige uma identificação segura do tipo antes do acesso a atributos específicos.

## Decisão
Foi criado um type guard específico para diferenciar `GarantiaPeca` e `GarantiaServico` na tela de Relatórios.

## Justificativa
A solução preserva a tipagem forte do Angular, evita conversões inseguras e mantém o código compatível com o compilador em modo estrito.

## Consequências
A exportação da planilha Excel continua funcionando e a listagem de garantias no relatório não apresenta erro de build.
