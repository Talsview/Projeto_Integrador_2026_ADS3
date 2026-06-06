# ADR-014 — Aplicação do padrão Decorator em notificação interna com auditoria

## Status

Aceita.

## Contexto

O Projeto Integrador exige a aplicação dos seis padrões de projeto estudados na disciplina. Até a etapa anterior, o sistema já possuía Singleton, Factory Method, Adapter, Iterator e Template Method. Faltava consolidar o padrão Decorator em um ponto real e coerente do sistema.

O sistema da oficina mecânica prioriza rastreabilidade, histórico e controle operacional. Alterações de status de Ordem de Serviço são eventos importantes, pois representam a evolução do fluxo Orçamento, Execução, Pagamento e Finalizado.

## Decisão

Aplicar o padrão Decorator no mecanismo de notificação interna do sistema.

A estrutura definida foi:

```text
Notificador                 → Component
NotificadorOperacional      → ConcreteComponent
NotificadorDecorator        → Decorator
NotificadorAuditoriaDecorator → ConcreteDecorator
```

A classe `NotificacaoService` monta a cadeia:

```text
new NotificadorAuditoriaDecorator(new NotificadorOperacional())
```

A classe `OrdemServicoService` aciona o `NotificacaoService` após a alteração de status da OS.

## Justificativa

O Decorator permite adicionar auditoria à notificação sem modificar a implementação base de envio operacional. Essa solução mantém baixo acoplamento, preserva a separação de responsabilidades e deixa clara a aplicação acadêmica do padrão.

## Consequências positivas

```text
1. O sexto padrão de projeto fica aplicado em uma funcionalidade real.
2. A alteração de status da OS passa a gerar notificação auditável.
3. O sistema continua funcionando localmente, sem dependência obrigatória de internet.
4. A solução facilita a demonstração pelo Swagger.
5. A rastreabilidade do sistema é reforçada.
```

## Consequências negativas ou limitações

```text
1. A notificação ainda é interna e simulada, não havendo envio por e-mail ou aplicativo externo.
2. Caso futuramente a oficina queira envio real por e-mail, WhatsApp ou SMS, será necessário criar novos componentes ou decoradores.
```

## Decisão final

A aplicação do Decorator em notificação interna com auditoria foi aprovada por ser coerente com o domínio da oficina mecânica, atender à exigência acadêmica e manter a arquitetura monolítica em camadas.
