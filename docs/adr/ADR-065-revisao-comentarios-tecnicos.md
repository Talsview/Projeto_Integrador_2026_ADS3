# ADR-065 — Revisão dos comentários técnicos do código

## Status

Aceita.

## Contexto

Após a inclusão inicial de comentários no código, foi identificado que parte deles ainda estava genérica e pouco útil para estudo, manutenção e apresentação acadêmica. Comentários como "executa a lógica do método" não explicavam claramente a função real do método no sistema.

## Decisão

Revisar os comentários dos métodos para que eles expliquem de forma simples e objetiva:

- a responsabilidade do método;
- a utilidade do método no sistema;
- a relação com regras de negócio quando existir;
- a justificativa dos padrões de projeto quando o método fizer parte de um padrão.

## Consequências positivas

- O código fica mais fácil de estudar.
- A defesa acadêmica dos padrões de projeto fica mais clara.
- Os métodos principais ficam documentados com relação ao domínio da oficina.
- Os comentários passam a agregar entendimento, não apenas ocupar espaço.

## Consequências negativas

- O código fica mais longo visualmente por causa dos comentários.
- Exige manutenção futura para que os comentários continuem coerentes com possíveis mudanças na regra de negócio.

## Observação

A alteração é documental no código-fonte. Nenhuma regra de negócio ou funcionalidade foi modificada nesta etapa.
