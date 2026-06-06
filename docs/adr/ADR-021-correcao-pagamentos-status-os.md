# ADR-021 — Correção da tela de Pagamentos com validação visual do status da OS

## Status

Aceita.

## Contexto

A regra de domínio do sistema estabelece que a Ordem de Serviço deve seguir o fluxo:

```text
ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO
```

A implementação do backend já impedia corretamente o registro de pagamentos fora da etapa `PAGAMENTO`. Entretanto, a tela Angular permitia preencher e enviar o pagamento para qualquer OS listada, causando erro de regra de negócio.

## Decisão

Foi decidido manter a regra de negócio no backend e melhorar a camada View em Angular para:

```text
1. Exibir o status atual da OS selecionada.
2. Bloquear o botão de salvar pagamento quando a OS não estiver em PAGAMENTO.
3. Permitir avanço controlado do fluxo da OS pela própria tela de Pagamentos.
4. Exibir mensagens detalhadas retornadas pelo backend.
```

## Consequências

A tela de Pagamentos fica mais intuitiva, reduz erros operacionais e preserva o fluxo validado academicamente. O backend continua sendo a fonte de validação da regra de domínio, enquanto o frontend atua como camada de orientação e prevenção de erro.
