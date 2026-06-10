# Etapa 56 — Correção da Fila de Atendimento e do fluxo Orçamento → Execução → Pagamento

## Objetivo

Corrigir a regra operacional da tela **Fila de Atendimento** e do botão de conclusão de orçamento na tela **Itens da OS**.

A regra validada nesta etapa é:

```text
ORÇAMENTO → EXECUÇÃO → PAGAMENTO → FINALIZADO
```

Assim, uma Ordem de Serviço criada nasce em **ORÇAMENTO**. Após o lançamento dos serviços e peças do orçamento, ela deve ir para **EXECUÇÃO**, entrando na Fila de Atendimento. Somente depois da execução do serviço ela deve seguir para **PAGAMENTO**. A finalização permanece vinculada à quitação financeira no módulo de Pagamentos.

## Problema identificado

A tela de Itens da OS enviava o orçamento diretamente para **PAGAMENTO**, pulando a etapa de **EXECUÇÃO**. Com isso, a Fila de Atendimento não representava corretamente o atendimento operacional da oficina.

Além disso, o resultado do cálculo recursivo era exibido em formato JSON bruto, dificultando a leitura pelo usuário.

## Correções aplicadas

### 1. Tela Itens da OS

- O botão foi alterado de **Enviar orçamento para pagamento** para **Enviar orçamento para execução**.
- O backend passou a registrar o status **EXECUÇÃO** após a conclusão do orçamento.
- A OS deixa de ir diretamente para o módulo Pagamentos.
- A mensagem de sucesso agora informa que a OS entrou na Fila de Atendimento.

### 2. Backend da Ordem de Serviço

- Criado o método `enviarOrcamentoParaExecucao`.
- Mantido o endpoint antigo como compatibilidade, mas direcionando para a regra correta.
- O status registrado após conclusão do orçamento passou a ser `EXECUCAO`.
- A data de aprovação continua sendo registrada ao entrar em execução.

### 3. Fila de Atendimento

- A fila passa a exibir somente OS com status **EXECUCAO**.
- A fila representa clientes/veículos que já tiveram o orçamento montado e estão aguardando a execução do serviço.
- Foi adicionado botão para enviar a OS para **PAGAMENTO** após a execução.
- A tabela passou a possuir ações operacionais por linha.

### 4. Cálculo recursivo

- O resultado deixou de aparecer como JSON bruto.
- A tela agora apresenta cards com:
  - total de serviços;
  - total de peças;
  - total geral;
  - quantidade de itens de serviço;
  - quantidade de itens de peça;
  - função recursiva utilizada;
  - justificativa acadêmica.

## Arquivos alterados

```text
src/main/java/br/com/avcar/oficina/business/ordemservico/controller/OrdemServicoController.java
src/main/java/br/com/avcar/oficina/business/ordemservico/service/OrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/ordemservico/estrutura/service/EstruturaDadosOrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/pagamento/service/PagamentoService.java
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
frontend/oficina-web/src/app/models/ordem-servico.model.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.html
frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.ts
frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.html
frontend/oficina-web/src/styles.css
README.md
docs/README_ETAPA56_CORRECAO_FILA_FLUXO_EXECUCAO.md
docs/adr/ADR-060-correcao-fila-fluxo-execucao.md
```

## Como testar

1. Cadastre uma nova OS.
2. Verifique que ela nasce em **ORÇAMENTO**.
3. Acesse **Itens da OS**.
4. Inclua pelo menos um serviço e, se necessário, peças.
5. Clique em **Enviar orçamento para execução**.
6. Acesse **Fila de Atendimento**.
7. Confirme que a OS aparece na fila com status **EXECUCAO**.
8. Clique em **Enviar para pagamento**.
9. Acesse **Pagamentos** e registre o pagamento.
10. Ao quitar o valor total, a OS será finalizada automaticamente.

## Resultado esperado

```text
Cadastro da OS        → ORÇAMENTO
Conclusão do orçamento → EXECUÇÃO
Conclusão da execução  → PAGAMENTO
Quitação financeira    → FINALIZADO
```
