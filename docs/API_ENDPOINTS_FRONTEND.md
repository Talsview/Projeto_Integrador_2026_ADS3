# Endpoints da API para Integração com o Frontend Angular

## 1. Configuração base

Backend:

```text
http://localhost:9081
```

Frontend Angular:

```text
http://localhost:4200
```

No Angular, usar sempre caminho relativo:

```text
/api
```

Exemplo:

```typescript
this.http.get('/api/clientes')
```

O proxy do Angular redireciona para:

```text
http://localhost:9081/api/clientes
```

## 2. Formato padrão de resposta

```json
{
  "success": true,
  "status": 200,
  "message": "Operação executada com sucesso.",
  "data": {},
  "timestamp": "2026-06-06T00:00:00"
}
```

Quando a resposta for uma lista paginada:

```json
{
  "success": true,
  "status": 200,
  "message": "Registros localizados com sucesso.",
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

## 3. Clientes

### Listar clientes

```text
GET /api/clientes
```

### Pesquisar clientes

```text
GET /api/clientes/pesquisar?termo=valor
```

### Buscar cliente por ID

```text
GET /api/clientes/{id}
```

### Cadastrar pessoa física

```text
POST /api/clientes/pessoa-fisica
```

Exemplo de JSON:

```json
{
  "nome": "João da Silva",
  "telefone": "(62) 99999-9999",
  "email": "joao@email.com",
  "endereco": "Rua Exemplo, Goiânia-GO",
  "cpf": "000.000.000-00",
  "rg": "1234567",
  "dataNascimento": "1995-01-15"
}
```

### Cadastrar pessoa jurídica

```text
POST /api/clientes/pessoa-juridica
```

Exemplo de JSON:

```json
{
  "nome": "Empresa Exemplo LTDA",
  "telefone": "(62) 3333-3333",
  "email": "contato@empresa.com",
  "endereco": "Avenida Exemplo, Goiânia-GO",
  "cnpj": "00.000.000/0001-00",
  "razaoSocial": "Empresa Exemplo LTDA",
  "nomeFantasia": "Empresa Exemplo",
  "inscricaoEstadual": "123456789"
}
```

## 4. Veículos

```text
GET    /api/veiculos
GET    /api/veiculos/{id}
GET    /api/veiculos/pesquisar?termo=valor
POST   /api/veiculos
PUT    /api/veiculos/{id}
PATCH  /api/veiculos/{id}/transferir-proprietario
DELETE /api/veiculos/{id}
```

## 5. Ordens de Serviço

```text
GET    /api/ordens-servico
GET    /api/ordens-servico/{id}
GET    /api/ordens-servico/pesquisar?termo=valor
POST   /api/ordens-servico
PUT    /api/ordens-servico/{id}
PATCH  /api/ordens-servico/{id}/status
DELETE /api/ordens-servico/{id}
```

## 6. Estrutura de Dados

```text
GET /api/estrutura-dados/ordens-servico/fila-atendimento
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=DATA_ABERTURA
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=VALOR_TOTAL
GET /api/estrutura-dados/ordens-servico/ordenar?criterio=PRIORIDADE
GET /api/estrutura-dados/ordens-servico/pesquisar-linear?termo=valor
GET /api/estrutura-dados/ordens-servico/{idOrdemServico}/total-recursivo
```

## 7. Padrões de Projeto

```text
GET /api/padroes-projeto
POST /api/notificacoes/simular
```

## 8. Verificação da API

```text
GET /api/database/status
```

Esse endpoint deve ser usado para verificar se o backend e o banco local estão disponíveis.
