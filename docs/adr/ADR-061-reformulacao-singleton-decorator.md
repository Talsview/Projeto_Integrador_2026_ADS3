# ADR-061 — Reformulação do Singleton e do Decorator

## Status

Aprovado.

## Contexto

O sistema já possuía seis padrões de projeto implementados. Entretanto, na revisão técnica foi identificado que o **Singleton** e o **Decorator** poderiam agregar mais valor operacional.

O Singleton verificava a conexão local com o banco, mas retornava apenas uma informação simples de disponibilidade. O Decorator adicionava dados de auditoria ao resultado da notificação, mas ainda não persistia esse registro.

## Decisão

Reformular os dois padrões:

1. O `DatabaseConnectionSingleton` passa a atuar como monitor único do ambiente local de banco, com controle de último estado, falhas consecutivas, tempo de resposta, cache operacional e último erro sanitizado.
2. O `NotificadorAuditoriaDecorator` passa a persistir a auditoria das notificações na tabela `notificacao_auditoria`.
3. O catálogo de padrões passa a informar a evidência funcional de cada padrão.
4. A tela de Configurações passa a exibir informações adicionais do Singleton.
5. A rota de Padrões de Projeto passa a ficar disponível no menu de Gestão.

## Consequências positivas

- O Singleton passa a ter utilidade prática para diagnóstico local do sistema.
- O Decorator passa a gerar histórico persistente e consultável.
- Mudanças de status da OS geram rastreabilidade adicional.
- A apresentação acadêmica fica mais defensável, pois os padrões têm evidência funcional.

## Consequências negativas

- É necessário criar a tabela `notificacao_auditoria` no banco existente antes de iniciar o backend, pois o projeto usa validação de schema pelo JPA.

## Regra de negócio preservada

A mudança não altera o fluxo principal da Ordem de Serviço. O Decorator apenas acrescenta rastreabilidade às notificações geradas por eventos operacionais.
