# Como Executar Backend e Frontend

## 1. Pré-requisitos

```text
Java 21
Maven configurado no PATH ou uso de mvn.cmd no Windows
PostgreSQL local
Node.js LTS
npm
Angular CLI instalado globalmente, quando necessário
VS Code para o frontend Angular
NetBeans, IntelliJ ou terminal para o backend
```

## 2. Preparar o banco de dados

No PostgreSQL/pgAdmin, criar o banco:

```sql
CREATE DATABASE car_repair;
```

Executar os scripts nesta ordem:

```text
database/01_create_schema.sql
database/02_seed_inicial.sql
```

Opcionalmente, para verificar dados mínimos:

```text
database/03_verificacao_integracao_frontend.sql
```

## 3. Rodar o backend Spring Boot

No PowerShell:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn.cmd spring-boot:run
```

A aplicação deve subir em:

```text
http://localhost:9081
```

Testes rápidos:

```text
http://localhost:9081/swagger-ui.html
http://localhost:9081/api/database/status
http://localhost:9081/api/clientes
```

## 4. Rodar o frontend Angular no VS Code

No terminal do VS Code:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd run start:proxy
```

A aplicação deve abrir em:

```text
http://localhost:4200
```

## 5. Por que usar start:proxy

O comando abaixo inicia o Angular usando o arquivo `proxy.conf.json`:

```powershell
npm.cmd run start:proxy
```

Isso permite que o Angular chame:

```text
/api/clientes
```

E o proxy redirecione para:

```text
http://localhost:9081/api/clientes
```

## 6. Erros comuns

### npm não encontra package.json

Causa: comando executado na pasta errada.

Correto:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
```

### Swagger não abre

Causa provável: backend não subiu ou erro de banco.

Verificar:

```powershell
mvn.cmd spring-boot:run
```

### Angular abre, mas não carrega dados

Causa provável: backend parado ou banco não criado.

Testar:

```text
http://localhost:9081/api/database/status
```

### relation does not exist

Causa: scripts SQL não foram executados.

Executar:

```text
database/01_create_schema.sql
database/02_seed_inicial.sql
```

## 7. Ordem correta de execução

```text
1. PostgreSQL ativo
2. Banco car_repair criado
3. Scripts SQL executados
4. Backend rodando em localhost:9081
5. Swagger abrindo
6. Frontend rodando em localhost:4200
7. Tela de Clientes consultando /api/clientes
```
