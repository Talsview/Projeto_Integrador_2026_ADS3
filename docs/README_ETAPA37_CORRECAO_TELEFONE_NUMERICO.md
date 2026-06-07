# Etapa 37 — Correção da validação de telefone numérico

Esta etapa corrige a validação dos campos de telefone no frontend Angular e no backend Spring Boot.

## Problema identificado

Na tela de Clientes ainda era possível digitar letras no campo Telefone. A validação anterior considerava apenas a quantidade de dígitos, mas não removia imediatamente caracteres inválidos no campo visual.

## Correção aplicada

- Criação da função `formatarTelefone` no Angular.
- Remoção automática de letras e caracteres inválidos durante a digitação.
- Máscara automática no padrão `(62) 99999-9999` ou `(62) 9999-9999`.
- Mensagem de erro mais clara para telefone inválido.
- Validação aplicada em Clientes, Colaboradores, Empresas Terceirizadas e Fornecedores.
- Validação reforçada no backend para rejeitar letras mesmo se a API for chamada por Swagger/Postman.

## Arquivos principais alterados

- `frontend/oficina-web/src/app/core/validation/field-validation.ts`
- `frontend/oficina-web/src/app/pages/clientes/clientes.component.ts`
- `frontend/oficina-web/src/app/pages/clientes/clientes.component.html`
- `frontend/oficina-web/src/app/pages/colaboradores/colaboradores.component.ts`
- `frontend/oficina-web/src/app/pages/colaboradores/colaboradores.component.html`
- `frontend/oficina-web/src/app/pages/empresas-terceirizadas/empresas-terceirizadas.component.ts`
- `frontend/oficina-web/src/app/pages/empresas-terceirizadas/empresas-terceirizadas.component.html`
- `frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.ts`
- `frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.html`
- `src/main/java/br/com/avcar/oficina/core/validation/ValidationUtils.java`
