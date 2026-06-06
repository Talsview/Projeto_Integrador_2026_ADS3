# Etapa 6 — Módulo Peça, Fornecedor e ItemPeca

## 1. Objetivo da etapa

Esta etapa implementa a base de rastreabilidade das peças utilizadas nas Ordens de Serviço da oficina mecânica AV CAR AUTO CENTER. O objetivo é permitir o cadastro de peças, o cadastro dos fornecedores responsáveis e o registro de peças aplicadas em uma OS por meio da entidade associativa `ItemPeca`.

A implementação respeita o MER validado, no qual:

```text
OrdemServico (1,1) -------- utiliza -------- (0,n) ItemPeca.
Peca (1,1) -------- é aplicada em -------- (0,n) ItemPeca.
Fornecedor (1,1) -------- fornece -------- (0,n) ItemPeca.
ItemPeca (1,1) -------- gera -------- (1,1) GarantiaPeca.
```

Nesta etapa, a garantia da peça ainda não é implementada como fluxo completo, pois ela depende da finalização da Ordem de Serviço. Entretanto, o modelo físico e o vínculo do `ItemPeca` já ficam preparados para que `GarantiaPeca` seja gerada em etapa posterior.

---

## 2. Entidades implementadas

```text
Fornecedor
Peca
ItemPeca
```

### 2.1 Fornecedor

Representa a empresa ou pessoa responsável pelo fornecimento da peça utilizada na OS. A identificação do fornecedor é obrigatória no `ItemPeca`, pois a responsabilidade por defeito de peça em garantia pode ser do fornecedor, embora a oficina continue realizando o atendimento inicial ao cliente.

### 2.2 Peca

Representa o cadastro das peças usadas pela oficina. A peça considera informações como nome, código nacional, marca da peça, modelo aplicável, ano do veículo e ano do modelo.

### 2.3 ItemPeca

Representa a peça efetivamente aplicada em uma Ordem de Serviço. É uma entidade associativa porque o relacionamento possui dados próprios, como quantidade, valor unitário, valor total e observações.

---

## 3. Regras de negócio atendidas

```text
RN20 — OS pode ou não utilizar peças.
RN21 — Peça usada na OS deve ser registrada como ItemPeca.
RN22 — ItemPeca deve estar vinculado a Peca cadastrada.
RN23 — ItemPeca deve ter Fornecedor identificado.
RN24 — ItemPeca gera GarantiaPeca em etapa posterior.
RN26 — Garantia da peça começa após finalizar a OS.
RN29 — O sistema deve priorizar rastreabilidade entre cliente, veículo, OS, serviço, peça, fornecedor e garantia.
```

---

## 4. Arquivos criados

### DTOs

```text
src/main/java/br/com/avcar/oficina/business/peca/dto/FornecedorDTO.java
src/main/java/br/com/avcar/oficina/business/peca/dto/PecaDTO.java
src/main/java/br/com/avcar/oficina/business/peca/dto/ItemPecaDTO.java
```

### Models

```text
src/main/java/br/com/avcar/oficina/business/peca/model/FornecedorModel.java
src/main/java/br/com/avcar/oficina/business/peca/model/PecaModel.java
src/main/java/br/com/avcar/oficina/business/peca/model/ItemPecaModel.java
```

### Repositories

```text
src/main/java/br/com/avcar/oficina/business/peca/repository/IFornecedorRepository.java
src/main/java/br/com/avcar/oficina/business/peca/repository/IPecaRepository.java
src/main/java/br/com/avcar/oficina/business/peca/repository/IItemPecaRepository.java
```

### Mappers

```text
src/main/java/br/com/avcar/oficina/business/peca/mapper/FornecedorMapper.java
src/main/java/br/com/avcar/oficina/business/peca/mapper/PecaMapper.java
src/main/java/br/com/avcar/oficina/business/peca/mapper/ItemPecaMapper.java
```

### Validations

```text
src/main/java/br/com/avcar/oficina/business/peca/validation/FornecedorValidation.java
src/main/java/br/com/avcar/oficina/business/peca/validation/PecaValidation.java
src/main/java/br/com/avcar/oficina/business/peca/validation/ItemPecaValidation.java
```

### Services

```text
src/main/java/br/com/avcar/oficina/business/peca/service/FornecedorService.java
src/main/java/br/com/avcar/oficina/business/peca/service/PecaService.java
src/main/java/br/com/avcar/oficina/business/peca/service/ItemPecaService.java
```

### Controllers

```text
src/main/java/br/com/avcar/oficina/business/peca/controller/FornecedorController.java
src/main/java/br/com/avcar/oficina/business/peca/controller/PecaController.java
src/main/java/br/com/avcar/oficina/business/peca/controller/ItemPecaController.java
```

---

## 5. Endpoints implementados

### Fornecedores

```text
POST   /api/fornecedores
PUT    /api/fornecedores/{id}
GET    /api/fornecedores/{id}
GET    /api/fornecedores
GET    /api/fornecedores/pesquisar?termo=valor
DELETE /api/fornecedores/{id}
```

### Peças

```text
POST   /api/pecas
PUT    /api/pecas/{id}
GET    /api/pecas/{id}
GET    /api/pecas
GET    /api/pecas/pesquisar?termo=valor
DELETE /api/pecas/{id}
```

### Itens de Peça

```text
POST   /api/itens-peca
PUT    /api/itens-peca/{id}
GET    /api/itens-peca/{id}
GET    /api/itens-peca
GET    /api/itens-peca/ordem-servico/{idOrdemServico}
GET    /api/itens-peca/ordem-servico/{idOrdemServico}/pesquisar?termo=valor
DELETE /api/itens-peca/{id}
```

---

## 6. Ajustes no banco de dados

Foram atualizados os scripts:

```text
database/01_create_schema.sql
database/02_seed_inicial.sql
```

Ajustes realizados:

```text
1. Correção definitiva da duplicidade da coluna id_empresa_terceirizada em execucao_servico_terceirizado.
2. Inclusão de índice único parcial para CNPJ de fornecedor ativo.
3. Inclusão de índice único parcial para código nacional de peça ativa.
4. Inclusão de fornecedores e peças iniciais para testes no Swagger e no Angular.
```

---

## 7. Observação acadêmica

O `ItemPeca` foi implementado como entidade associativa, pois o vínculo entre `OrdemServico`, `Peca` e `Fornecedor` possui dados próprios. Essa solução evita perda de rastreabilidade e permite responder perguntas essenciais da oficina, como:

```text
1. Qual peça foi aplicada em determinada OS?
2. Qual fornecedor entregou a peça usada?
3. Qual foi a quantidade aplicada?
4. Qual foi o valor unitário e total da peça?
5. Qual garantia deverá ser gerada após a finalização da OS?
```

O relacionamento direto com `OrdemServicoModel` será concluído quando o módulo de Ordem de Serviço for implementado. Nesta etapa, o campo `idOrdemServico` mantém compatibilidade com o banco físico e prepara a integração entre os módulos.
