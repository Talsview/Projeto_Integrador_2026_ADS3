# ADR-042 — Correção de validações em colaboradores e ações de clientes

## Status

Aceita.

## Contexto

Durante os testes de validação da interface, foi identificado que a tela de Colaboradores não apresentava validações visuais campo a campo de forma equivalente à tela de Clientes. Também foi identificado que a tela de Clientes não exibia ações para edição e inativação dos registros cadastrados.

## Decisão

Foi decidido padronizar a tela de Colaboradores com validações por campo e adicionar à tela de Clientes os botões Editar e Inativar. Também foi corrigida a validação de telefone no frontend e no backend para impedir letras ou símbolos indevidos.

## Consequências

- O usuário recebe mensagens de erro mais claras.
- A operação de edição e inativação de clientes fica disponível pela interface.
- O backend continua protegendo a integridade dos dados mesmo que a API seja chamada fora do Angular.
