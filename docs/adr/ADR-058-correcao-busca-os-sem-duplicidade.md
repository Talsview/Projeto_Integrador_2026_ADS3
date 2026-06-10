# ADR-058 — Correção da busca da OS sem duplicidade visual

**Status:** Aceito  
**Data:** 15/06/2026  
**Autores:** Davi Martins Alexandre; Caio Vinicius Souza Martins

## 1. Contexto

A tela **Ordens de Serviço** possui campos de busca para localizar clientes e veículos em bases maiores. Entretanto, após a seleção, o sistema preenchia o campo de busca com o mesmo texto exibido no campo de seleção. Isso gerava duplicidade visual e reduzia a clareza da tela.

## 2. Decisão

Foi decidido que os campos de pesquisa não devem armazenar a seleção final. Eles serão utilizados apenas para localizar registros. A escolha válida da OS permanecerá somente no `select` correspondente.

## 3. Justificativa

A separação entre busca e seleção torna a interface mais clara e reduz ambiguidades. O usuário passa a entender que o campo superior serve para pesquisar e que o campo inferior representa o dado efetivamente registrado na Ordem de Serviço.

## 4. Consequências positivas

- Interface mais intuitiva.
- Eliminação da duplicidade visual.
- Menor risco de interpretação incorreta dos campos.
- Preservação da regra de vínculo entre cliente e veículo.
- Melhor organização da tela de cadastro/edição de OS.

## 5. Consequências negativas / riscos

- O campo de busca fica vazio após a seleção, o que pode exigir adaptação inicial do usuário.

## 6. Mitigações

- O valor escolhido continua visível no `select` inferior.
- Os textos auxiliares da tela continuam indicando que o campo superior serve para pesquisa.
- A lógica de validação continua destacando o campo de seleção quando cliente ou veículo não forem informados.

## 7. Alternativas consideradas

- Manter o texto nos dois campos.
- Substituir o `select` por um único componente autocomplete.
- Remover os campos de busca e manter apenas listas completas.

A alternativa adotada foi a mais simples e compatível com a estrutura já existente.

## 8. Referências

- Regras de negócio do sistema AV CAR AUTO CENTER.
- Fluxo operacional de Ordem de Serviço.
- Padrão de rastreabilidade entre cliente, veículo e OS.
