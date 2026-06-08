# ADR-057 — Envio do orçamento para pagamento pela tela de Itens da OS

**Status:** Aceito  
**Data:** 15/06/2026  
**Autores:** Davi Martins Alexandre; Caio Vinicius Souza Martins

## 1. Contexto

A tela principal de Ordens de Serviço possuía um botão genérico de fluxo. Na prática, isso deixava a operação confusa, pois o usuário montava o orçamento em outra tela, mas precisava voltar para Ordens de Serviço para avançar o status. Além disso, a tela de Ordens de Serviço não deve realizar pagamento nem finalizar OS.

## 2. Decisão

Foi decidido remover o botão de fluxo da tela de Ordens de Serviço e criar uma ação específica na tela **Itens da Ordem de Serviço**: **Enviar orçamento para pagamento**.

## 3. Justificativa

A tela de Itens da OS é o local onde o orçamento é realmente montado, pois nela são incluídos os serviços, peças, colaboradores responsáveis e fornecedores. Portanto, faz mais sentido que o envio para pagamento ocorra exatamente após a conclusão desses itens.

## 4. Consequências positivas

- Fluxo mais intuitivo para o usuário.
- Menos confusão entre avanço operacional e pagamento.
- Pagamento continua centralizado no módulo financeiro.
- Melhor aderência à rotina real da oficina.

## 5. Consequências negativas / riscos

- O fluxo visual deixa de usar o botão genérico de avanço de status.
- Usuários antigos precisam entender que o envio para pagamento agora fica na tela de Itens da OS.

## 6. Mitigações

- O botão recebeu texto claro: **Enviar orçamento para pagamento**.
- A tela de Ordens de Serviço teve o botão de fluxo removido para evitar ação duplicada.
- A documentação foi atualizada.

## 7. Alternativas consideradas

- Manter o botão Fluxo na tela de Ordens de Serviço.
- Criar um wizard com várias etapas.
- Finalizar a OS diretamente pela tela de Itens.

A alternativa adotada foi mais simples, objetiva e coerente com a separação dos módulos.

## 8. Referências

- Regras de negócio do sistema AV CAR AUTO CENTER.
- Fluxo operacional de Ordem de Serviço.
- Modelo de domínio da oficina mecânica.
