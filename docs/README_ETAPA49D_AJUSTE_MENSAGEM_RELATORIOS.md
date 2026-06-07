# Etapa 49D — Ajuste de mensagens na tela de Relatórios

## Objetivo
Remover a mensagem verde automática exibida ao abrir ou atualizar a tela de Relatórios, mantendo apenas avisos de falha quando algum endpoint não responder.

## Ajustes realizados

- A tela de Relatórios não exibe mais mensagem de sucesso ao carregar dados.
- A exportação da planilha Excel realiza apenas o download do arquivo, sem exibir alerta verde após a ação.
- Avisos e erros continuam sendo exibidos quando houver falha real de comunicação com algum endpoint.
- A geração da planilha Excel formatada foi mantida.

## Arquivos alterados

- `frontend/oficina-web/src/app/pages/relatorios/relatorios.component.ts`
- `frontend/oficina-web/src/app/pages/relatorios/relatorios.component.html`
