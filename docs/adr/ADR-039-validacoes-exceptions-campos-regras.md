# ADR-039 — Validações, exceptions e regras de entrada dos campos

## Status
Aceita.

## Contexto
O sistema AV CAR AUTO CENTER já possuía a lógica principal de cadastro, ordens de serviço, pagamentos, garantias e geração de PDF. Entretanto, a aplicação precisava reforçar as validações de entrada para impedir informações incompatíveis com o domínio, como CPF inválido, CNPJ inválido, datas futuras, valores negativos, pagamento acima do saldo pendente e garantia acionada fora das regras.

## Decisão
Foram aplicadas validações em duas camadas:

1. Frontend Angular, para impedir erros antes do envio e melhorar a experiência do usuário.
2. Backend Spring Boot, para garantir integridade mesmo quando a API for acessada por Swagger, Postman ou outro cliente.

Também foi criado o utilitário `ValidationUtils` para centralizar validações comuns e reduzir duplicidade de código.

## Consequências
- O usuário recebe mensagens mais claras.
- A API fica mais protegida contra dados inválidos.
- As regras de negócio ficam mais consistentes.
- A manutenção fica mais simples, pois validações comuns ficam centralizadas.

## Pontos de atenção
Validações mais específicas podem ser expandidas futuramente, como relacionamento obrigatório entre proprietário atual e veículo em uma OS ou políticas fiscais oficiais, caso o sistema evolua para emissão fiscal autorizada.
