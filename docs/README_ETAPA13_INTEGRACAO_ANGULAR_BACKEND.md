# Etapa 13 — Integração do Angular com o Backend Spring Boot

## 1. Objetivo da etapa

Esta etapa teve como objetivo ajustar a camada **View em Angular** para ser executada no **VS Code** e consumir corretamente a API REST do backend Spring Boot.

O foco não foi criar novas entidades de banco de dados, mas estabilizar a comunicação entre:

```text
Angular no VS Code
    ↓ HTTP/JSON
Proxy de desenvolvimento Angular
    ↓
Backend Spring Boot REST
    ↓
PostgreSQL local
```

## 2. Contexto acadêmico

O Projeto Integrador exige a implementação de um sistema Java para controle de Ordens de Serviço de uma oficina mecânica, com arquitetura em camadas, API, validações, padrões de projeto e estrutura de dados aplicada.

Nesta refatoração, a camada de apresentação passa a ser o Angular, mantendo o backend como monólito modular em Spring Boot.

## 3. Ajustes realizados no Angular

Foram realizados os seguintes ajustes técnicos:

```text
1. Configuração do environment para usar /api em desenvolvimento.
2. Uso do proxy.conf.json para redirecionar chamadas Angular para o backend em localhost:9081.
3. Ajuste do ApiResponse para aceitar data/dados e message/mensagem.
4. Ajuste do BaseApiService para extrair listas paginadas retornadas pelo backend.
5. Correção do carregamento da tela de Clientes para não travar em caso de erro.
6. Padronização do fluxo de execução com npm.cmd, adequado ao ambiente Windows do aluno.
7. Atualização da versão do frontend para 0.13.0.
8. Inclusão de script npm start:no-open para executar sem abrir navegador automaticamente.
```

## 4. Arquivos alterados

```text
frontend/oficina-web/package.json
frontend/oficina-web/src/environments/environment.ts
frontend/oficina-web/src/environments/environment.development.ts
frontend/oficina-web/src/app/core/models/api-response.model.ts
frontend/oficina-web/src/app/core/services/base-api.service.ts
frontend/oficina-web/src/app/core/services/cliente-api.service.ts
frontend/oficina-web/src/app/core/services/dashboard.service.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.html
```

## 5. Como executar o backend

Na raiz do projeto:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn.cmd spring-boot:run
```

O backend deverá ficar disponível em:

```text
http://localhost:9081
```

Swagger:

```text
http://localhost:9081/swagger-ui.html
```

Endpoint de verificação:

```text
http://localhost:9081/api/database/status
```

## 6. Como executar o frontend no VS Code

No terminal do VS Code:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd run start:proxy
```

A aplicação Angular deverá abrir em:

```text
http://localhost:4200
```

## 7. Observação sobre o caminho correto do Angular

O comando `npm.cmd install` deve ser executado na pasta que possui `package.json`.

Caminho correto:

```text
C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
```

Caminho incorreto:

```text
C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend
```

A pasta `frontend` contém o diretório do projeto Angular, mas não é ela própria o projeto Angular.

## 8. Teste de integração realizado pela tela de Clientes

A tela de Clientes foi preparada para consumir os endpoints:

```text
GET  /api/clientes
GET  /api/clientes/pesquisar?termo=valor
POST /api/clientes/pessoa-fisica
POST /api/clientes/pessoa-juridica
```

O backend retorna os dados no formato padronizado:

```json
{
  "success": true,
  "status": 200,
  "message": "Clientes localizados com sucesso.",
  "data": {
    "content": [],
    "page": 0,
    "size": 20,
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "last": true
  },
  "timestamp": "2026-06-06T00:00:00"
}
```

O Angular extrai `data.content` quando a resposta é paginada.

## 9. Banco de dados

Não houve alteração estrutural no banco nesta etapa. As tabelas continuam sendo criadas pelos scripts anteriores:

```text
database/01_create_schema.sql
database/02_seed_inicial.sql
```

Foi adicionado apenas o script auxiliar:

```text
database/03_verificacao_integracao_frontend.sql
```

Esse script serve para verificar se existem dados mínimos para testar o frontend.

## 10. Conclusão

A Etapa 13 prepara o sistema para apresentação prática com backend e frontend rodando simultaneamente. O Angular passa a consumir a API por proxy, reduzindo problemas de CORS durante o desenvolvimento e deixando o fluxo mais simples para execução no VS Code.
