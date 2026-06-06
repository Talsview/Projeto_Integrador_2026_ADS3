# Como executar backend e frontend

## 1. Banco de dados

Criar o banco local no PostgreSQL:

```text
car_repair
```

Executar os scripts:

```text
database/01_create_schema.sql
database/02_seed_inicial.sql
```

## 2. Backend Spring Boot

Na raiz do projeto:

```bash
mvn spring-boot:run
```

Endereços:

```text
API: http://localhost:9081/api
Swagger: http://localhost:9081/swagger-ui.html
Status do banco: http://localhost:9081/api/database/status
```

## 3. Frontend Angular

```bash
cd frontend/oficina-web
npm install
npm start
```

Endereço:

```text
http://localhost:4200
```

## 4. Fluxo de validação recomendado

```text
1. Abrir Swagger e testar /api/database/status.
2. Abrir Angular em localhost:4200.
3. Verificar status do banco no Painel.
4. Acessar Clientes e listar ou cadastrar cliente.
5. Acessar Veículos e listar os veículos cadastrados.
6. Acessar Ordens de Serviço e consultar o fluxo das OS.
7. Acessar Padrões de Projeto para evidenciar os seis padrões.
8. Acessar Estrutura de Dados para demonstrar fila, ordenação, busca e recursividade.
```
