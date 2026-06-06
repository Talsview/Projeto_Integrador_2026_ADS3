# Etapa 12 — Frontend Angular e integração inicial com a API REST

## 1. Objetivo

Esta etapa cria a primeira versão da camada **View** em Angular, conectada à API REST do backend Spring Boot. A proposta atende à organização por camadas exigida no Projeto Integrador, mantendo a separação entre interface, controllers, responses, DTOs, services, validations, repositories e banco de dados.

## 2. Decisão técnica

O frontend foi criado no diretório:

```text
frontend/oficina-web
```

A aplicação Angular consome a API local em:

```text
http://localhost:9081/api
```

O Swagger permanece em:

```text
http://localhost:9081/swagger-ui.html
```

## 3. Justificativa acadêmica

A camada View deixou de ser representada por Java Swing e passou a ser representada pelo Angular. Essa decisão melhora a separação visual da aplicação, permite comunicação padronizada por JSON e facilita a validação dos endpoints por Swagger/OpenAPI.

A documentação oficial do Angular informa que a Angular CLI permite criar, desenvolver, testar e manter aplicações Angular diretamente pela linha de comando. Além disso, a documentação atual lista o Angular 22 como versão suportada e informa a compatibilidade com Node.js, TypeScript e RxJS.

## 4. Estrutura criada

```text
frontend/oficina-web
├── angular.json
├── package.json
├── proxy.conf.json
├── src
│   ├── app
│   │   ├── core
│   │   │   ├── interceptors
│   │   │   ├── models
│   │   │   └── services
│   │   ├── models
│   │   ├── pages
│   │   │   ├── dashboard
│   │   │   ├── clientes
│   │   │   ├── veiculos
│   │   │   ├── ordens-servico
│   │   │   ├── estrutura-dados
│   │   │   └── padroes-projeto
│   │   ├── app.component.*
│   │   ├── app.config.ts
│   │   └── app.routes.ts
│   ├── environments
│   ├── index.html
│   ├── main.ts
│   └── styles.css
```

## 5. Telas iniciais implementadas

```text
Painel operacional
Clientes
Veículos
Ordens de Serviço
Estrutura de Dados I
Padrões de Projeto
```

## 6. Serviços HTTP criados

```text
BaseApiService
DashboardService
ClienteApiService
VeiculoApiService
OrdemServicoApiService
ApiErrorInterceptor
```

## 7. Integrações iniciais com endpoints

```text
GET /api/database/status
GET /api/padroes-projeto
GET /api/clientes
GET /api/clientes/pesquisar
POST /api/clientes/pessoa-fisica
POST /api/clientes/pessoa-juridica
GET /api/veiculos
GET /api/veiculos/pesquisar
GET /api/ordens-servico
GET /api/ordens-servico/pesquisar
PATCH /api/ordens-servico/{id}/status
GET /api/estrutura-dados/ordens-servico/fila-atendimento
GET /api/estrutura-dados/ordens-servico/ordenar
GET /api/estrutura-dados/ordens-servico/pesquisar-linear
GET /api/estrutura-dados/ordens-servico/{id}/total-recursivo
```

## 8. Como executar

Primeiro, executar o backend:

```bash
mvn spring-boot:run
```

Depois, executar o frontend:

```bash
cd frontend/oficina-web
npm install
npm start
```

A tela ficará disponível em:

```text
http://localhost:4200
```

## 9. Observações importantes

O frontend criado nesta etapa é uma base inicial. As telas de clientes, veículos e ordens de serviço já consomem endpoints reais, mas ainda poderão ser refinadas nas próximas etapas com formulários completos, seleção assistida de cliente/veículo/serviço, edição, exclusão lógica e tratamento visual mais detalhado.

## 10. Contribuição para o projeto

Esta etapa consolida a transição para uma aplicação com backend REST e frontend desacoplado. O sistema permanece monolítico no backend, mas a View fica preparada para evolução visual e integração com todos os módulos já implementados.
