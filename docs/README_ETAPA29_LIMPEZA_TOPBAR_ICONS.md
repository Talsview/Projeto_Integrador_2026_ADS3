# Etapa 29 — Limpeza da barra superior e remoção de ícones decorativos

## Objetivo

Esta etapa ajusta o frontend Angular para deixar a interface mais profissional e mais próxima de um sistema administrativo real de oficina mecânica.

## Alterações realizadas

```text
1. Removida a busca global da barra superior.
2. Removido o ícone de notificação.
3. Removido o botão de modo escuro que era apenas decorativo.
4. Removidos emojis da tela inicial.
5. Substituídos símbolos decorativos por siglas funcionais.
6. Mantida a estrutura profissional com sidebar escura, topbar limpa e conteúdo operacional.
7. Mantida a comunicação com o backend Spring Boot REST.
```

## Resultado esperado

A interface fica mais objetiva e menos poluída, mantendo foco em cadastros, ordens de serviço, pagamentos, garantias e fila de atendimento.

## Arquivos principais alterados

```text
frontend/oficina-web/src/app/app.component.html
frontend/oficina-web/src/app/app.component.ts
frontend/oficina-web/src/app/app.component.css
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.html
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.ts
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.css
```
