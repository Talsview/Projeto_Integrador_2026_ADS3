# ADR-049 — Combobox pesquisável na abertura de Ordem de Serviço

## Status

Aceito

## Data

15/06/2026

## Contexto

A tela de abertura de Ordem de Serviço possuía campos de seleção simples para Cliente e Veículo. Esse comportamento é aceitável em bases pequenas, mas se torna inadequado em cenários reais com centenas ou milhares de clientes cadastrados.

O sistema AV CAR AUTO CENTER precisa ser funcional em uma oficina mecânica real, priorizando agilidade operacional, rastreabilidade e facilidade de uso.

## Decisão

Foi decidido manter o select tradicional, mas adicionar um campo de pesquisa antes dele. O usuário pode pesquisar o cliente por nome, CPF/CNPJ, telefone ou e-mail, e pesquisar o veículo por placa, modelo ou proprietário.

A pesquisa utiliza os endpoints já existentes de busca do backend, preservando a integração atual da API REST.

## Justificativa

A solução melhora a experiência do usuário sem alterar o modelo de dados e sem quebrar os endpoints existentes. O select continua simples para bases pequenas, enquanto o campo de busca permite trabalhar melhor com bases maiores.

## Consequências positivas

- abertura de OS mais rápida;
- menor chance de selecionar cliente errado;
- melhor usabilidade em bases grandes;
- preservação da regra cliente x veículo;
- baixo impacto no backend.

## Consequências negativas / riscos

- o usuário precisa clicar em Buscar ou pressionar Enter para consultar bases grandes;
- em evolução futura, pode ser interessante implementar autocomplete com debounce.

## Mitigações

O campo possui dica de uso e mantém o select como confirmação explícita da escolha.

## Alternativas consideradas

- Manter somente select simples: rejeitado por baixa usabilidade em bases grandes.
- Criar modal de busca: considerado mais complexo para a etapa atual.
- Autocomplete com debounce: alternativa futura, mas não necessária para a entrega atual.

## Follow-up

Avaliar, em evolução futura, componente reutilizável de autocomplete para todas as telas que utilizem entidades de apoio, como cliente, veículo, peça, serviço, fornecedor e colaborador.
