# ADR-053 — Ajuste de feedback visual na tela de Relatórios e exportação Excel

## Status
Aceito

## Data
15/06/2026

## Contexto
A tela de Relatórios passou a carregar dados automaticamente e exportar uma planilha Excel formatada. Entretanto, a mensagem verde de sucesso aparecia sempre que a tela era aberta ou quando a planilha era exportada, causando poluição visual desnecessária.

## Decisão
Remover o feedback positivo automático da tela de Relatórios. A tela passará a exibir mensagens apenas em situações de aviso ou erro, como falha parcial de algum endpoint. A exportação Excel será tratada como ação direta de download, sem alerta visual após sua conclusão.

## Consequências positivas
- Interface mais limpa.
- Menos mensagens desnecessárias ao usuário.
- Exportação mais natural, semelhante a sistemas administrativos reais.

## Consequências negativas / riscos
- O usuário não receberá uma mensagem textual confirmando a exportação, mas o próprio download do arquivo já representa o feedback da ação.

## Mitigação
Erros e falhas continuam sendo exibidos normalmente quando ocorrerem problemas de comunicação ou indisponibilidade de dados.
