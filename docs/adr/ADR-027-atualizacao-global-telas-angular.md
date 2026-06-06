# ADR-027 — Atualização Global das Telas Angular após Operações na API

## Status

Aprovada.

## Contexto

Durante os testes do frontend Angular, foi identificado que algumas telas salvavam dados corretamente no backend, mas a tabela somente era atualizada visualmente após uma segunda ação do usuário. Isso afetava a experiência de uso e poderia gerar dúvida sobre a efetiva gravação dos dados.

A correção inicialmente aplicada em Clientes e Funções resolveu o problema nessas telas específicas. Entretanto, o mesmo comportamento foi observado nas demais abas.

## Decisão

Foi decidido aplicar a correção de atualização visual de forma global, centralizando a sincronização no interceptor HTTP da aplicação Angular.

Além disso, as listagens passaram a retornar novas referências de array no `BaseApiService`, evitando reaproveitamento de referências antigas em tabelas.

## Consequências positivas

```text
1. A atualização visual passa a valer para todas as abas.
2. Reduz duplicação de correções individuais nos componentes.
3. Melhora a experiência do usuário ao salvar, editar ou inativar registros.
4. Mantém o frontend mais coerente com o funcionamento esperado de um sistema de cadastro.
5. Preserva a organização por camadas e a comunicação via API REST.
```

## Consequências negativas

```text
1. O interceptor passa a ter uma responsabilidade adicional de sincronização visual.
2. Pode haver pequena sobrecarga após respostas HTTP, considerada aceitável para o escopo local do projeto.
```

## Justificativa acadêmica

A decisão atende ao requisito de apresentar software funcional, com validações e comportamento coerente para o usuário. Como o sistema é destinado a uma oficina mecânica local, a resposta visual imediata é importante para cadastros e operações de ordem de serviço.
