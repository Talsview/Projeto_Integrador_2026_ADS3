# ADR-026 — Atualização imediata das tabelas no Angular após operações de cadastro

## Status

Aprovada.

## Contexto

Durante os testes do frontend Angular, foi observado que algumas telas não refletiam imediatamente os dados salvos no banco de dados. O registro era persistido corretamente pelo backend Spring Boot, mas a tabela da interface só era atualizada depois de uma nova ação manual do usuário, como clicar em “Listar todos”.

Esse comportamento poderia gerar dúvida operacional, pois o usuário poderia acreditar que o cadastro não foi realizado.

## Decisão

Foi decidido alterar o fluxo das telas críticas para que operações de gravação sejam seguidas por uma nova consulta ao backend.

O fluxo adotado passa a ser:

```text
POST/PUT/DELETE → GET atualizado → substituição da lista da tabela → feedback visual
```

Também foi decidido adicionar estratégia de não cache nas consultas GET da classe `BaseApiService`, usando cabeçalhos HTTP e parâmetro `_t` com timestamp.

## Consequências positivas

```text
- A tabela passa a refletir imediatamente os dados salvos no banco.
- O usuário não precisa clicar novamente em “Listar todos”.
- Reduz-se a sensação de lentidão ou travamento da interface.
- A camada View fica mais coerente com o estado real da API.
- A solução permanece compatível com o backend monolítico em camadas.
```

## Consequências negativas

```text
- Após cada gravação há uma consulta adicional ao backend.
- Há pequeno aumento no tráfego HTTP local.
```

Como o sistema foi projetado para uso local e o volume de dados da oficina é moderado, esse custo é aceitável em troca de maior clareza e confiabilidade visual.
