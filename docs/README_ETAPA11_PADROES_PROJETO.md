# Etapa 11 — Decorator, Notificação Interna e Consolidação dos Padrões de Projeto

## 1. Objetivo da etapa

Esta etapa teve como objetivo implementar o sexto padrão de projeto exigido pela disciplina e consolidar a documentação técnica dos seis padrões aplicados no sistema da oficina mecânica AV CAR AUTO CENTER.

O padrão implementado nesta etapa foi o **Decorator**, aplicado ao mecanismo de notificação interna e auditoria operacional do sistema.

---

## 2. Padrão Decorator aplicado

### Local de aplicação

```text
src/main/java/br/com/avcar/oficina/core/designpattern/decorator
```

### Classes criadas

```text
Notificador
NotificadorOperacional
NotificadorDecorator
NotificadorAuditoriaDecorator
```

### Classes de apoio

```text
core/notification/dto/NotificacaoDTO
core/notification/dto/NotificacaoResultadoDTO
core/notification/service/NotificacaoService
core/notification/controller/NotificacaoController
```

### Aplicação prática

O padrão Decorator foi aplicado para que uma notificação operacional simples pudesse receber um comportamento adicional de auditoria sem alteração da classe base.

A cadeia montada foi:

```text
NotificadorOperacional
        ↓ decorado por
NotificadorAuditoriaDecorator
```

Assim, quando o sistema altera o status de uma Ordem de Serviço, uma notificação interna é gerada e auditada.

---

## 3. Integração com Ordem de Serviço

A classe `OrdemServicoService` foi ajustada para acionar o `NotificacaoService` sempre que ocorre alteração de status da OS.

Trecho conceitual:

```text
Alteração de status da OS
        ↓
Registro no HistoricoStatusOrdem
        ↓
NotificacaoService
        ↓
NotificadorAuditoriaDecorator
        ↓
NotificadorOperacional
```

Essa solução reforça a rastreabilidade do sistema, pois mudanças operacionais relevantes passam a gerar registro de notificação auditável.

---

## 4. Endpoint criado para demonstração no Swagger

```text
POST /api/notificacoes/simular
```

Esse endpoint permite demonstrar o Decorator sem depender de alteração real de uma OS.

Exemplo de corpo da requisição:

```json
{
  "titulo": "Teste de notificação",
  "mensagem": "Notificação interna gerada pelo sistema.",
  "modulo": "ORDEM_SERVICO",
  "referencia": "OS-1001",
  "canal": "INTERNO_LOCAL"
}
```

---

## 5. Endpoint de catálogo dos padrões de projeto

Também foi criado um endpoint acadêmico para listar onde cada padrão foi aplicado:

```text
GET /api/padroes-projeto
```

Esse endpoint ajuda na apresentação e na avaliação, pois mostra diretamente pelo Swagger os seis padrões de projeto exigidos pela disciplina.

---

# 6. Consolidação dos seis padrões de projeto

| Padrão | Local de aplicação | Classe principal | Justificativa |
|---|---|---|---|
| Singleton | Verificação local da conexão com PostgreSQL | `DatabaseConnectionSingleton` | Centraliza a checagem de conexão local. |
| Factory Method | Cadastro de Cliente PF/PJ | `ClienteCadastroFactory` / `ClienteFactoryMethod` | Seleciona a fábrica correta conforme o tipo do cliente. |
| Adapter | Resposta de Veículo para Angular | `VeiculoResponseAdapter` | Adapta entidades internas para DTOs de resposta. |
| Iterator | Fila e lista linear da OS | `OficinaIterator`, `FilaAtendimentoIterator`, `ListaLinearIterator` | Percorre estruturas próprias sem expor implementação interna. |
| Template Method | Ordenação manual de OS | `OrdenadorTemplate` | Define o esqueleto fixo da ordenação e permite variar o critério. |
| Decorator | Notificação com auditoria | `NotificadorAuditoriaDecorator` | Adiciona auditoria à notificação sem alterar a classe base. |

---

## 7. Relação com as regras do projeto

A aplicação do Decorator respeita os princípios gerais definidos para o sistema:

```text
Rastreabilidade
Integridade operacional
Funcionamento local
Organização em camadas
Separação de responsabilidades
Evidência acadêmica dos padrões de projeto
```

O sistema não depende de serviço externo para enviar notificações. A notificação é interna e local, adequada ao contexto de uma oficina mecânica que deve funcionar sem dependência obrigatória de internet.

---

## 8. Resultado da etapa

Com esta etapa, o backend passa a possuir os seis padrões de projeto exigidos:

```text
Singleton
Adapter
Iterator
Template Method
Factory Method
Decorator
```

Além disso, os padrões ficam documentados no código, em arquivo técnico e em endpoint próprio para consulta via Swagger.
