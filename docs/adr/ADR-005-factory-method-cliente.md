# ADR-005 — Aplicação do Factory Method no Cadastro de Cliente

## Status

Aceita.

## Contexto

O modelo de dados da oficina define que Cliente se especializa em PessoaFisica e PessoaJuridica de forma exclusiva e total. Portanto, todo cliente deve obrigatoriamente ser classificado como pessoa física ou jurídica, nunca ambos.

## Decisão

Aplicar o padrão de projeto Factory Method no módulo Cliente, criando fábricas concretas para Pessoa Física e Pessoa Jurídica.

## Implementação

```text
ClienteFactoryMethod
ClientePessoaFisicaFactory
ClientePessoaJuridicaFactory
ClienteCadastroFactory
```

## Consequências

- A Controller não conhece diretamente as entidades concretas criadas.
- O Service fica mais organizado e com menor acoplamento.
- A regra de especialização exclusiva do Cliente fica explícita no código.
- O projeto passa a ter o segundo padrão de projeto exigido pela disciplina.
