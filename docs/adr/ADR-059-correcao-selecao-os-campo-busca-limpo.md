# ADR-059 — Correção da seleção da OS com campo de busca limpo

**Status:** Aceito  
**Data:** 15/06/2026  
**Autores:** Davi Martins Alexandre; Caio Vinicius Souza Martins

## 1. Contexto

A tela **Ordens de Serviço** possui campos de pesquisa para localizar clientes e veículos e campos de seleção para registrar a escolha na OS. Após a correção anterior, ainda foi identificado um caso visual em que o texto pesquisado permanecia no campo superior mesmo depois da seleção na combobox.

## 2. Decisão

Foi decidido reforçar a separação entre pesquisa e seleção. A escolha do cliente e do veículo passa a ser tratada por métodos específicos vinculados ao evento `ngModelChange`, e os campos de busca são limpos também quando a combobox recebe foco ou clique.

## 3. Justificativa

A decisão melhora a clareza operacional da tela. O usuário passa a visualizar o cliente e o veículo selecionados somente no local correto, ou seja, no campo de seleção. O campo de busca permanece apenas como mecanismo auxiliar de localização.

## 4. Consequências positivas

- Eliminação da duplicidade visual entre busca e combobox.
- Melhor usabilidade na tela de Ordem de Serviço.
- Separação mais clara entre pesquisar e selecionar.
- Preservação da regra cliente → veículo.
- Menor risco de confusão durante o cadastro de uma nova OS.

## 5. Consequências negativas / riscos

- O texto digitado no campo de busca é limpo quando o usuário interage com a combobox.

## 6. Mitigações

- O dado selecionado continua visível na combobox.
- O campo de busca permanece disponível para nova pesquisa.
- A alteração não afeta a persistência dos dados nem as regras de negócio da OS.

## 7. Alternativas consideradas

- Manter apenas a limpeza no evento `change`.
- Utilizar um componente autocomplete único.
- Remover a combobox e manter apenas a busca.

A alternativa adotada foi manter a estrutura atual, porém reforçar os eventos de seleção e limpeza para preservar a estabilidade do sistema.

## 8. Referências

- Regras de negócio do sistema AV CAR AUTO CENTER.
- Tela de Ordem de Serviço.
- Etapa 54 — Correção da busca da OS sem duplicidade visual.
