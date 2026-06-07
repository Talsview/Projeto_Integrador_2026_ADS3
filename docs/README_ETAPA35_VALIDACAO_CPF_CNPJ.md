# Etapa 35 — Validação real de CPF e CNPJ

Esta etapa reforça as validações do cadastro de clientes, garantindo que documentos com quantidade correta de dígitos, mas matematicamente inválidos, não sejam aceitos pelo sistema.

## Objetivo

Impedir que o usuário cadastre cliente Pessoa Física com CPF inválido e cliente Pessoa Jurídica com CNPJ inválido. A validação considera o cálculo dos dígitos verificadores, não apenas o tamanho do campo.

## Backend

Arquivo criado:

```text
src/main/java/br/com/avcar/oficina/core/validation/DocumentoValidationUtils.java
```

Arquivo alterado:

```text
src/main/java/br/com/avcar/oficina/business/pessoa/validation/ClienteValidation.java
```

Validações aplicadas:

- CPF obrigatório para Pessoa Física;
- CPF com 11 dígitos;
- CPF não pode ser sequência repetida, como `00000000000` ou `11111111111`;
- CPF deve possuir dígitos verificadores válidos;
- CNPJ obrigatório para Pessoa Jurídica;
- CNPJ com 14 dígitos;
- CNPJ não pode ser sequência repetida;
- CNPJ deve possuir dígitos verificadores válidos;
- verificação de duplicidade continua preservada.

## Frontend Angular

Arquivo criado:

```text
frontend/oficina-web/src/app/core/validation/documento-validation.ts
```

Arquivos alterados:

```text
frontend/oficina-web/src/app/pages/clientes/clientes.component.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.html
frontend/oficina-web/src/styles.css
```

Melhorias aplicadas:

- máscara automática de CPF no campo de Pessoa Física;
- máscara automática de CNPJ no campo de Pessoa Jurídica;
- bloqueio de envio quando CPF/CNPJ for inválido;
- mensagens claras próximas ao campo inválido;
- destaque visual para campos inválidos;
- validação de data de nascimento futura;
- validação de e-mail em formato inválido.

## Exemplos bloqueados

```text
000.000.000-00
111.111.111-11
123.456.789-00
999.999.999-99
00.000.000/0000-00
11.111.111/1111-11
12.345.678/0001-00
```

## Justificativa

A validação foi aplicada tanto no Angular quanto no backend. O Angular melhora a experiência do usuário, informando o problema antes do envio. O backend garante a integridade real da aplicação, impedindo entrada inválida mesmo quando a API for chamada pelo Swagger, Postman ou outro cliente externo.
