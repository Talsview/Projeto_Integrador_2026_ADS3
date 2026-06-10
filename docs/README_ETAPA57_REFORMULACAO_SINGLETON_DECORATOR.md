# Etapa 57 — Reformulação do Singleton e do Decorator

## Objetivo

Reformular os padrões **Singleton** e **Decorator** para que eles deixem de ter caráter apenas demonstrativo e passem a agregar funcionalidade operacional real ao sistema da oficina.

## Problema identificado

Durante a revisão dos seis padrões de projeto, foi constatado que:

- o **Singleton** existia para verificação simples do banco, mas poderia oferecer diagnóstico mais útil para execução local;
- o **Decorator** adicionava auditoria ao DTO de notificação, porém ainda não persistia essa auditoria em banco.

## Solução aplicada

### Singleton

A classe `DatabaseConnectionSingleton` foi reformulada como monitor único do ambiente local de banco de dados.

Agora ela registra:

- último resultado da verificação;
- tempo de resposta do PostgreSQL;
- quantidade de verificações realizadas;
- quantidade de falhas consecutivas;
- data/hora da última verificação;
- data/hora da última mudança de status;
- último erro sanitizado;
- indicação de uso de cache operacional.

Com isso, a tela **Configurações** passa a exibir informações mais úteis para diagnóstico em computadores diferentes.

### Decorator

O `NotificadorAuditoriaDecorator` foi reformulado para persistir auditoria em banco.

Agora, quando uma notificação operacional é gerada, especialmente em mudanças de status da Ordem de Serviço, o sistema grava um registro na tabela:

```text
notificacao_auditoria
```

Essa alteração transforma o Decorator em um recurso de rastreabilidade real, pois as notificações deixam de existir apenas em memória/DTO e passam a compor histórico consultável.

## Novos endpoints

```text
GET  /api/notificacoes/auditoria
GET  /api/notificacoes/auditoria/referencia?referencia={numeroOs}
POST /api/notificacoes/simular
```

## Scripts de banco

Foi incluído o script:

```text
database/01_schema/03_create_notificacao_auditoria.sql
```

Também foram atualizados:

```text
database/01_schema/01_create_schema.sql
database/04_completo/00_SCRIPT_COMPLETO_BANCO.sql
database/03_verificacoes/03_verificacao_geral_sistema.sql
```

Como o projeto usa `spring.jpa.hibernate.ddl-auto=validate`, a tabela `notificacao_auditoria` deve existir no banco antes de iniciar o backend atualizado.

## Arquivos alterados

```text
src/main/java/br/com/avcar/oficina/core/designpattern/singleton/DatabaseConnectionSingleton.java
src/main/java/br/com/avcar/oficina/core/database/DatabaseConnectionChecker.java
src/main/java/br/com/avcar/oficina/core/database/DatabaseStatusResult.java
src/main/java/br/com/avcar/oficina/core/designpattern/decorator/NotificadorAuditoriaDecorator.java
src/main/java/br/com/avcar/oficina/core/notification/service/NotificacaoService.java
src/main/java/br/com/avcar/oficina/core/notification/controller/NotificacaoController.java
src/main/java/br/com/avcar/oficina/core/notification/dto/NotificacaoResultadoDTO.java
src/main/java/br/com/avcar/oficina/core/notification/dto/NotificacaoAuditoriaDTO.java
src/main/java/br/com/avcar/oficina/core/notification/model/NotificacaoAuditoriaModel.java
src/main/java/br/com/avcar/oficina/core/notification/repository/INotificacaoAuditoriaRepository.java
src/main/java/br/com/avcar/oficina/core/designpattern/catalog/controller/PadraoProjetoController.java
src/main/java/br/com/avcar/oficina/core/designpattern/catalog/dto/PadraoProjetoDTO.java
frontend/oficina-web/src/app/core/services/dashboard.service.ts
frontend/oficina-web/src/app/core/models/database-status.model.ts
frontend/oficina-web/src/app/models/padrao-projeto.model.ts
frontend/oficina-web/src/app/pages/configuracoes/configuracoes.component.ts
frontend/oficina-web/src/app/pages/configuracoes/configuracoes.component.html
frontend/oficina-web/src/app/pages/padroes-projeto/padroes-projeto.component.html
frontend/oficina-web/src/app/app.routes.ts
frontend/oficina-web/src/app/app.component.ts
```

## Parecer técnico

Após esta etapa, o **Singleton** passa a apoiar diagnóstico local do sistema e o **Decorator** passa a gerar rastreabilidade persistente. Assim, os dois padrões ficam mais coerentes com a proposta acadêmica e funcional do projeto.
