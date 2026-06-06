# Etapa 28 — Ajuste visual conforme modelo de referência

## Objetivo

Esta etapa reformula o frontend Angular para aproximar a interface do modelo visual enviado como referência, mantendo a comunicação já validada com o backend Spring Boot REST e com o banco PostgreSQL local.

## Alterações realizadas

- Layout global ajustado para sidebar escura fixa, topbar clara, busca global, atalhos e identificação do usuário.
- Menu lateral reorganizado com aparência de sistema administrativo profissional.
- Tela inicial redesenhada com cards operacionais, fluxo das ordens de serviço, ações rápidas, últimas OS abertas, resumo financeiro e indicadores auxiliares.
- Cards, tabelas, botões, badges, alertas e formulários padronizados com estilo mais próximo dos modelos visuais enviados.
- Mantida a nomenclatura operacional “Fila de Atendimento” no lugar de “Estrutura de Dados”.
- A tela de Padrões de Projeto continua removida do menu operacional.
- Mantida a integração existente com os services Angular e endpoints atuais do backend.

## Arquivos principais alterados

```text
frontend/oficina-web/src/app/app.component.html
frontend/oficina-web/src/app/app.component.css
frontend/oficina-web/src/app/app.component.ts
frontend/oficina-web/src/styles.css
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.html
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.css
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.ts
frontend/oficina-web/package.json
```

## Validação técnica

Foi executada validação TypeScript:

```bash
./node_modules/.bin/tsc --noEmit -p tsconfig.app.json
```

Resultado: sem erros de TypeScript.

## Como executar

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

Após iniciar, usar `CTRL + F5` no navegador para evitar cache de versões anteriores.
