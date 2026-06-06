# Oficina Web — Frontend Angular

## 1. Objetivo

Este projeto representa a camada **View** do sistema da oficina mecânica AV CAR AUTO CENTER. Ele foi desenvolvido em Angular para consumir a API REST do backend Spring Boot.

## 2. Execução no VS Code

Abra o VS Code na pasta:

```text
C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
```

Instale as dependências:

```powershell
npm.cmd install
```

Execute com proxy para o backend:

```powershell
npm.cmd run start:proxy
```

Acesse:

```text
http://localhost:4200
```

## 3. Backend esperado

O backend Spring Boot deve estar rodando em:

```text
http://localhost:9081
```

Swagger:

```text
http://localhost:9081/swagger-ui.html
```

## 4. Integração com a API

O Angular usa chamadas relativas:

```text
/api
```

O arquivo `proxy.conf.json` redireciona essas chamadas para:

```text
http://localhost:9081/api
```

Exemplo:

```text
/api/clientes → http://localhost:9081/api/clientes
```

## 5. Scripts disponíveis

```json
{
  "start": "ng serve --open",
  "start:proxy": "ng serve --proxy-config proxy.conf.json --open",
  "start:no-open": "ng serve --proxy-config proxy.conf.json",
  "build": "ng build"
}
```

## 6. Telas preparadas

```text
Painel operacional
Clientes
Veículos
Ordens de Serviço
Estrutura de Dados
Padrões de Projeto
```

## 7. Observação importante

O comando `npm.cmd install` deve ser executado nesta pasta, pois é aqui que está o `package.json`.

Certo:

```text
frontend/oficina-web
```

Errado:

```text
frontend
```


## Etapa 32 — Paleta visual de oficina e implementação das logos

Nesta etapa foi aplicada uma identidade visual mais compatível com uma oficina mecânica, utilizando azul automotivo escuro, grafite, cinza metálico e laranja como cor de destaque. O layout superior por categorias foi mantido, mas a aparência foi ajustada para ficar mais profissional e alinhada à marca AV CAR AUTO CENTER.

Alterações principais:

- Implementação da logo completa no cabeçalho superior do Angular.
- Implementação da logo em formato de ícone como favicon da aba do navegador.
- Criação da pasta `frontend/oficina-web/public/assets/branding` para armazenar os arquivos visuais da marca.
- Ajuste da paleta global no `src/styles.css`.
- Reformulação do cabeçalho, menu superior, botões, cards, tabelas, campos e badges.
- Preservação das rotas, services, endpoints e integração Angular com o backend Spring Boot.

Arquivos principais alterados:

- `frontend/oficina-web/src/index.html`
- `frontend/oficina-web/src/app/app.component.html`
- `frontend/oficina-web/src/app/app.component.css`
- `frontend/oficina-web/src/styles.css`
- `frontend/oficina-web/src/app/pages/dashboard/dashboard.component.css`
- `frontend/oficina-web/public/assets/branding/av-car-favicon.png`
- `frontend/oficina-web/public/assets/branding/av-car-logo-horizontal.png`
