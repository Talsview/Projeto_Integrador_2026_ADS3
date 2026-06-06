# Etapa 14 — Ajuste de desempenho da comunicação Angular/Backend

## 1. Objetivo

Esta etapa corrige a percepção de lentidão na tela inicial do Angular, principalmente no bloco **Verificação da API**.

O problema identificado estava concentrado em dois pontos:

```text
1. O Angular deixava o botão em estado "Verificando..." quando ocorria erro de comunicação, pois o carregamento era finalizado apenas no complete da requisição.
2. A verificação de banco no backend utilizava conexão JDBC mantida em memória, podendo gerar demora ou retorno inconsistente quando o PostgreSQL estava indisponível.
```

## 2. Correções realizadas no Angular

Foram atualizados os arquivos:

```text
frontend/oficina-web/src/app/core/models/database-status.model.ts
frontend/oficina-web/src/app/core/services/dashboard.service.ts
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.ts
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.html
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.css
```

Melhorias aplicadas:

```text
Timeout de 3 segundos nas chamadas do dashboard.
Tratamento amigável quando o backend não responde.
Uso de finalize para encerrar o estado de carregamento tanto em sucesso quanto em erro.
Exibição de mensagem clara quando o backend ou PostgreSQL não estiver disponível.
Exibição do tempo de resposta informado pelo backend.
Carregamento dos padrões de projeto sem travar a tela inicial se o backend estiver fora do ar.
```

## 3. Correções realizadas no backend

Foram atualizados/criados os arquivos:

```text
src/main/java/br/com/avcar/oficina/core/database/DatabaseStatusResult.java
src/main/java/br/com/avcar/oficina/core/database/DatabaseConnectionChecker.java
src/main/java/br/com/avcar/oficina/core/designpattern/singleton/DatabaseConnectionSingleton.java
src/main/java/br/com/avcar/oficina/view/api/DatabaseStatusController.java
```

Melhorias aplicadas:

```text
A verificação da conexão passou a usar conexão curta, fechada automaticamente.
Foi definido login timeout de 2 segundos para evitar espera excessiva.
O endpoint /api/database/status passou a retornar available, tempoRespostaMs e mensagem.
O padrão Singleton foi mantido, mas agora atua como ponto único de verificação rápida da conexão local.
```

## 4. Endpoint ajustado

```http
GET /api/database/status
```

Exemplo de resposta esperada:

```json
{
  "success": true,
  "status": 200,
  "message": "Backend e PostgreSQL local disponíveis.",
  "data": {
    "available": true,
    "tempoRespostaMs": 38,
    "mensagem": "Backend e PostgreSQL local disponíveis."
  },
  "timestamp": "2026-06-06T12:00:00"
}
```

## 5. Justificativa acadêmica

Esta etapa reforça requisitos não funcionais do sistema, especialmente desempenho percebido, usabilidade, robustez e diagnóstico operacional. Como a oficina deve funcionar localmente, é necessário que o usuário consiga identificar rapidamente se o problema está no frontend, no backend ou na conexão com o banco PostgreSQL.

## 6. Como testar

Backend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn.cmd spring-boot:run
```

Frontend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd run start:proxy
```

Acessar:

```text
http://localhost:4200
```

Clicar em **Verificar banco**. O retorno deve ocorrer rapidamente, sem deixar o botão travado em "Verificando...".
