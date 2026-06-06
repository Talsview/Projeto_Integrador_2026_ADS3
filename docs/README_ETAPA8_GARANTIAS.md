# Etapa 8 — Garantias de Peças e Serviços

## 1. Objetivo da etapa

Esta etapa implementa o módulo de garantias da oficina mecânica AV CAR AUTO CENTER, integrando as entidades `GarantiaPeca` e `GarantiaServico` ao fluxo da Ordem de Serviço.

O objetivo principal é garantir rastreabilidade entre:

```text
Cliente
Veículo
OrdemServico
ItemServico
ItemPeca
Fornecedor
GarantiaPeca
GarantiaServico
```

## 2. Entidades implementadas

```text
GarantiaPeca
GarantiaServico
```

Também foram criados os enums:

```text
StatusGarantia
ResponsabilidadeGarantiaPeca
```

## 3. Regra principal implementada

A garantia é criada quando a peça ou o serviço é incluído na OS, inicialmente com status:

```text
AGUARDANDO_FINALIZACAO_OS
```

Quando a Ordem de Serviço muda para o status:

```text
FINALIZADO
```

as garantias são iniciadas automaticamente, recebendo:

```text
dataInicio = data de finalização da OS
dataFim = dataInicio + prazo da garantia
statusGarantia = VIGENTE
```

## 4. Garantia de peça

A garantia de peça está vinculada a `ItemPeca`.

Regras aplicadas:

```text
Toda peça aplicada em OS gera uma GarantiaPeca.
A garantia de peça começa após a finalização da OS.
A responsabilidade padrão da garantia de peça é do fornecedor.
A oficina continua responsável pelo atendimento ao cliente.
O prazo de garantia da peça é definido no cadastro da peça.
```

Foi adicionado ao cadastro de peça o campo:

```text
prazoGarantiaDias
```

## 5. Garantia de serviço

A garantia de serviço está vinculada a `ItemServico`.

Regras aplicadas:

```text
Todo serviço executado em OS gera uma GarantiaServico.
A garantia de serviço começa após a finalização da OS.
O prazo de garantia varia conforme o serviço cadastrado.
```

O prazo utilizado vem do campo já existente em `Servico`:

```text
prazoGarantiaDias
```

## 6. Endpoints implementados

### Garantias de peças

```text
GET    /api/garantias/pecas/{id}
GET    /api/garantias/pecas
GET    /api/garantias/pecas/item-peca/{idItemPeca}
GET    /api/garantias/pecas/ordem-servico/{idOrdemServico}
PATCH  /api/garantias/pecas/{id}/acionar
PATCH  /api/garantias/pecas/{id}/encerrar
```

### Garantias de serviços

```text
GET    /api/garantias/servicos/{id}
GET    /api/garantias/servicos
GET    /api/garantias/servicos/item-servico/{idItemServico}
GET    /api/garantias/servicos/ordem-servico/{idOrdemServico}
PATCH  /api/garantias/servicos/{id}/acionar
PATCH  /api/garantias/servicos/{id}/encerrar
```

## 7. Arquivos principais criados

```text
business/garantia/controller/GarantiaController.java
business/garantia/dto/AcionamentoGarantiaDTO.java
business/garantia/dto/GarantiaPecaDTO.java
business/garantia/dto/GarantiaServicoDTO.java
business/garantia/enums/StatusGarantia.java
business/garantia/enums/ResponsabilidadeGarantiaPeca.java
business/garantia/mapper/GarantiaPecaMapper.java
business/garantia/mapper/GarantiaServicoMapper.java
business/garantia/model/GarantiaPecaModel.java
business/garantia/model/GarantiaServicoModel.java
business/garantia/repository/IGarantiaPecaRepository.java
business/garantia/repository/IGarantiaServicoRepository.java
business/garantia/service/GarantiaService.java
business/garantia/validation/GarantiaValidation.java
```

## 8. Arquivos alterados

```text
business/peca/model/PecaModel.java
business/peca/dto/PecaDTO.java
business/peca/mapper/PecaMapper.java
business/peca/validation/PecaValidation.java
business/peca/service/ItemPecaService.java
business/ordemservico/service/ItemServicoService.java
business/ordemservico/service/OrdemServicoService.java
database/01_create_schema.sql
database/02_seed_inicial.sql
database/README_BANCO.md
README.md
```

## 9. Situação após a etapa

A Ordem de Serviço agora possui integração completa com garantias. Ao finalizar a OS, o sistema inicia automaticamente as garantias de peças e serviços, preservando rastreabilidade e atendendo às regras de negócio do projeto.

## 10. Próxima etapa recomendada

```text
Etapa 9 — Pagamento
```

A próxima etapa deve implementar o controle de pagamentos da OS, permitindo registrar pagamentos pendentes, pagos, cancelados, valores recebidos e validação do fluxo antes da finalização.
