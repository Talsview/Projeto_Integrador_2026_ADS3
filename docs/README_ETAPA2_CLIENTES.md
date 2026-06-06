# Etapa 2 — Módulo Pessoa e Cliente

## Objetivo

Implementar o primeiro módulo de negócio da API REST da oficina mecânica AV CAR AUTO CENTER, contemplando o cadastro de clientes pessoa física e pessoa jurídica conforme o MER e o modelo lógico validados.

## Entidades implementadas

```text
Pessoa
Cliente
PessoaFisica
PessoaJuridica
```

## Regra de modelagem respeitada

```text
Pessoa especializa em Cliente e Colaborador.
Tipo: compartilhada e total (ct).

Cliente especializa em PessoaFisica e PessoaJuridica.
Tipo: exclusiva e total (xt).
```

Nesta etapa foi implementado o lado de Cliente. O lado de Colaborador será implementado na próxima etapa.

## Camadas criadas

```text
business/pessoa/controller
business/pessoa/dto
business/pessoa/enums
business/pessoa/mapper
business/pessoa/model
business/pessoa/repository
business/pessoa/service
business/pessoa/validation
business/pessoa/designpattern/factory
```

## Endpoints disponíveis

```text
POST   /api/clientes/pessoa-fisica
POST   /api/clientes/pessoa-juridica
PUT    /api/clientes/pessoa-fisica/{id}
PUT    /api/clientes/pessoa-juridica/{id}
GET    /api/clientes/{id}
GET    /api/clientes
GET    /api/clientes/pesquisar?termo=valor
DELETE /api/clientes/{id}
```

## Padrão de projeto aplicado

### Factory Method

O padrão **Factory Method** foi aplicado no cadastro de Cliente para separar a criação de Pessoa Física e Pessoa Jurídica.

Arquivos principais:

```text
ClienteFactoryMethod.java
ClientePessoaFisicaFactory.java
ClientePessoaJuridicaFactory.java
ClienteCadastroFactory.java
```

Justificativa acadêmica: o cliente possui especialização exclusiva e total, ou seja, todo cliente deve ser PessoaFisica ou PessoaJuridica, nunca ambos. O Factory Method permite criar a família correta de objetos sem acoplar a Controller às entidades concretas.

## Observações de implementação

- O identificador padrão foi mantido como `Long`.
- A exclusão de cliente é lógica, por meio do campo `ativo`.
- A entidade `Pessoa` não é inativada automaticamente ao excluir um cliente, pois futuramente a mesma pessoa também poderá ser `Colaborador`.
- A API está preparada para ser consumida pelo Angular.
- As respostas seguem o padrão `ApiResponse` criado na Etapa 1.
