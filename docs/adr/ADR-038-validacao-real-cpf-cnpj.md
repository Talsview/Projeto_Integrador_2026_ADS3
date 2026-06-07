# ADR-038 — Validação real de CPF e CNPJ

## Status

Aprovada.

## Contexto

O cadastro de clientes aceita Pessoa Física e Pessoa Jurídica. Antes desta etapa, CPF e CNPJ eram validados principalmente pelo tamanho do campo. Essa abordagem não é suficiente, pois documentos como `00000000000`, `11111111111` ou números aleatórios com 11 dígitos poderiam ser aceitos mesmo sem serem válidos.

## Decisão

Foi criada uma validação centralizada para documentos brasileiros, considerando o cálculo dos dígitos verificadores de CPF e CNPJ.

A validação foi aplicada em duas camadas:

- frontend Angular, para orientar o usuário durante o preenchimento;
- backend Spring Boot, para garantir a integridade das regras de negócio.

## Arquivos envolvidos

```text
src/main/java/br/com/avcar/oficina/core/validation/DocumentoValidationUtils.java
src/main/java/br/com/avcar/oficina/business/pessoa/validation/ClienteValidation.java
frontend/oficina-web/src/app/core/validation/documento-validation.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.html
frontend/oficina-web/src/styles.css
```

## Consequências positivas

- O sistema deixa de aceitar CPF matematicamente inválido.
- O sistema deixa de aceitar CNPJ matematicamente inválido.
- A validação permanece segura mesmo fora da interface Angular.
- As mensagens de erro ficam mais claras para o usuário.
- O cadastro de cliente passa a ficar mais coerente com um sistema real de oficina.

## Consequências negativas

- Em ambientes de teste, será necessário utilizar CPF ou CNPJ válido para cadastrar clientes.
- Dados fictícios aleatórios com 11 ou 14 dígitos deixam de ser aceitos.
