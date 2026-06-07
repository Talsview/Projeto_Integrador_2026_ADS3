# ADR-040 — Validação e formatação de telefone numérico

## Status

Aceita.

## Contexto

Durante a validação da tela de Clientes, foi identificado que o usuário ainda conseguia inserir letras no campo Telefone. Esse comportamento prejudicava a qualidade dos dados cadastrais e poderia gerar inconsistências na comunicação com clientes, colaboradores, fornecedores e empresas terceirizadas.

## Decisão

Foi criada uma função de formatação de telefone no Angular para aceitar somente números e aplicar máscara visual automaticamente. Além disso, a validação do backend foi reforçada para rejeitar chamadas diretas à API que enviem letras ou caracteres inválidos no telefone.

## Consequências

- Melhora a experiência do usuário.
- Reduz erros de cadastro.
- Mantém a integridade mesmo quando a API for chamada fora do Angular.
- Padroniza o telefone no formato `(DD) 99999-9999` ou `(DD) 9999-9999`.
