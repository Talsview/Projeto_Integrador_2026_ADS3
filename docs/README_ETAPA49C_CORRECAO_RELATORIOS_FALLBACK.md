# Etapa 49C — Correção da tela de Relatórios com fallback por fonte de dados

## Objetivo
Corrigir a tela de Relatórios para que uma falha em um endpoint específico não quebre toda a página.

## Alterações
- A carga do relatório passou a tratar cada fonte de dados separadamente.
- Clientes, veículos, OS, pagamentos e garantias agora possuem fallback individual.
- Se algum endpoint falhar, a tela exibe aviso parcial e mantém os dados que conseguir carregar.
- A mensagem `Failed to fetch` deixou de ser exibida isoladamente ao usuário.
- A exportação Excel continua disponível quando houver dados suficientes carregados.

## Arquivos alterados
- `frontend/oficina-web/src/app/pages/relatorios/relatorios.component.ts`
- `frontend/oficina-web/src/app/pages/relatorios/relatorios.component.html`
- `frontend/oficina-web/src/styles.css`
