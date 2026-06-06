# Padrões de Projeto Aplicados no Sistema

Este documento consolida os seis padrões de projeto exigidos pela disciplina e identifica onde cada um foi aplicado no sistema da oficina mecânica AV CAR AUTO CENTER.

## 1. Singleton

```text
Classe principal: DatabaseConnectionSingleton
Pacote: core/designpattern/singleton
Aplicação: verificação local da conexão com PostgreSQL.
```

Justificativa: centralizar a criação e checagem técnica de conexão local sem espalhar essa responsabilidade pelo sistema.

---

## 2. Factory Method

```text
Classes principais:
ClienteFactoryMethod
ClientePessoaFisicaFactory
ClientePessoaJuridicaFactory
ClienteCadastroFactory
Pacote: business/pessoa/designpattern/factory
Aplicação: cadastro de cliente Pessoa Física e Pessoa Jurídica.
```

Justificativa: criar corretamente cliente PF ou PJ, respeitando a especialização exclusiva e total do modelo.

---

## 3. Adapter

```text
Classe principal: VeiculoResponseAdapter
Pacote: business/veiculo/adapter
Aplicação: montagem de resposta detalhada de Veículo para o Angular.
```

Justificativa: adaptar entidades internas de Veículo, Modelo, Marca, Histórico de Proprietário, Cliente e Pessoa para DTOs próprios de resposta.

---

## 4. Iterator

```text
Classes principais:
OficinaIterator
FilaAtendimentoIterator
ListaLinearIterator
Pacotes: core/estrutura/iterator, core/estrutura/fila e core/estrutura/lista
Aplicação: percurso da fila de atendimento e lista linear de busca.
```

Justificativa: percorrer estruturas lineares customizadas sem expor seus nós internos.

---

## 5. Template Method

```text
Classe principal: OrdenadorTemplate
Implementações:
OrdenadorOrdemServicoPorDataAbertura
OrdenadorOrdemServicoPorValorTotal
OrdenadorOrdemServicoPorPrioridade
Aplicação: ordenação manual de ordens de serviço.
```

Justificativa: definir o esqueleto do algoritmo de ordenação e permitir que subclasses alterem apenas o critério de comparação.

---

## 6. Decorator

```text
Classes principais:
Notificador
NotificadorOperacional
NotificadorDecorator
NotificadorAuditoriaDecorator
Pacote: core/designpattern/decorator
Aplicação: notificação interna com auditoria.
```

Justificativa: adicionar auditoria à notificação sem alterar a classe base de envio operacional.

---

## Endpoint de apoio para apresentação

```text
GET /api/padroes-projeto
```

Esse endpoint lista os seis padrões diretamente pela API, facilitando a apresentação e a validação pelo professor.
