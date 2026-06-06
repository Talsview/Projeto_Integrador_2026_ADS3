# ADR-030 — Reformulação Visual do Frontend Angular

## Status

Aprovada.

## Contexto

O frontend Angular já possuía integração funcional com o backend Spring Boot REST, porém sua interface ainda se apresentava como um painel simples. Para a apresentação do Projeto Integrador, a interface precisava transmitir a ideia de um sistema real de gestão de oficina mecânica, organizado por cadastros, ordens de serviço, financeiro, garantias e fila de atendimento.

## Decisão

Foi decidido reformular o layout global do Angular com sidebar fixa, topbar, menu agrupado e padronização visual de componentes. A tela de Padrões de Projeto foi removida do menu operacional, permanecendo apenas na documentação e no código. A tela técnica de Estrutura de Dados foi renomeada para Fila de Atendimento, mantendo a funcionalidade acadêmica com linguagem mais adequada ao usuário final.

## Consequências

- Melhor usabilidade para apresentação e uso operacional.
- Interface mais profissional e responsiva.
- Organização mais clara entre Cadastros, Atendimento/OS e Gestão.
- Nenhum endpoint do backend foi alterado.
- A integração Angular → Spring Boot REST foi preservada.
