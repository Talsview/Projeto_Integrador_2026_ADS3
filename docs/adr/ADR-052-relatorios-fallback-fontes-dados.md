# ADR-052 — Fallback por fonte de dados na tela de Relatórios

Status: Aceito  
Data: 15/06/2026

## Contexto
A tela de Relatórios consome várias fontes do backend ao mesmo tempo. Quando uma dessas chamadas falhava, todo o relatório era interrompido e a interface exibia erro genérico.

## Decisão
Aplicar tratamento de erro individual em cada fonte de dados do relatório, retornando lista vazia para a fonte que falhar e mantendo o carregamento das demais.

## Consequências positivas
- A tela não fica inutilizada por falha parcial.
- O usuário recebe mensagem mais clara.
- A exportação continua possível quando houver dados suficientes.

## Riscos
- Relatórios podem ser gerados com dados parciais se algum endpoint falhar.

## Mitigação
A interface passa a exibir aviso informando quais fontes não foram carregadas.
