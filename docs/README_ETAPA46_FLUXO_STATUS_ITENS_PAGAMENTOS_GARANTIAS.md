# Etapa 46 — Fluxo por Status nas Telas Operacionais

## Objetivo
Ajustar as telas operacionais para que cada uma exiba apenas as Ordens de Serviço compatíveis com a etapa correta do fluxo da oficina.

Fluxo oficial da OS:

```text
ORÇAMENTO → EXECUÇÃO → PAGAMENTO → FINALIZADO
```

## Ajustes implementados

### 1. Itens da Ordem de Serviço
A tela de Serviços e Peças da OS passou a exibir apenas Ordens de Serviço com status **ORCAMENTO**.

Justificativa: serviços e peças são adicionados durante a elaboração do orçamento. Após a OS avançar para execução, pagamento ou finalização, esses itens não devem ser alterados livremente.

Também foi corrigida a validação da **Data fim** do serviço. A data de início continua não podendo ser futura, porém a data fim pode ser futura quando representar uma previsão de conclusão. A regra mantida é que a data fim não pode ser anterior à data de início.

### 2. Pagamentos
A tela de Pagamentos passou a exibir apenas Ordens de Serviço com status **PAGAMENTO**.

Justificativa: a etapa financeira deve ocorrer apenas quando a OS já saiu da execução e está aguardando quitação.

### 3. Garantias
O filtro por OS na tela de Garantias passou a exibir apenas Ordens de Serviço com status **FINALIZADO**.

Justificativa: as garantias de peças e serviços só iniciam após a finalização da Ordem de Serviço.

### 4. Fila de Atendimento
A tela de Fila de Atendimento passou a exibir apenas Ordens de Serviço com status **EXECUCAO**.

Justificativa: a fila operacional deve representar OS em etapa de execução, facilitando a organização do atendimento técnico.

### 5. Backend
O backend foi reforçado para impedir que itens de serviço e itens de peça sejam cadastrados, atualizados ou removidos fora da etapa **ORCAMENTO**.

## Arquivos principais alterados

```text
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.html
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.ts
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.html
frontend/oficina-web/src/app/pages/garantias/garantias.component.ts
frontend/oficina-web/src/app/pages/garantias/garantias.component.html
frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.ts
frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.html
src/main/java/br/com/avcar/oficina/business/ordemservico/service/OrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/ordemservico/service/ItemServicoService.java
src/main/java/br/com/avcar/oficina/business/ordemservico/validation/ItemServicoValidation.java
src/main/java/br/com/avcar/oficina/business/peca/service/ItemPecaService.java
```
